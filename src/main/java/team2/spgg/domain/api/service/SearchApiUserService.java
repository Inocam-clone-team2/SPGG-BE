package team2.spgg.domain.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team2.spgg.domain.api.dto.searchapiuser.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "searchCash")
public class SearchApiUserService {
    private final Integer count = 10;
    @Value("${riot.api.key}") // application.properties에 정의된 riot.api.key 값을 읽어옵니다.
    public String apiKey;

    @Autowired
    private CacheManager cacheManager;

    public ResponseEntity<?> getRefreshSummoner(String summonerName) {
        cacheManager.getCache("searchcash").evict(summonerName);
        return getSummoner(summonerName);
    }

    @Cacheable(key = "#summonerName")
    public ResponseEntity<FinalResponseDto> getSummoner(String summonerName) {

        log.info("소환사 검색 시작");
        String apiUrl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName + "?api_key=" + apiKey;
        UserAverageDto userAverage = new UserAverageDto();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SummonerDto> responseEntity = restTemplate.getForEntity(apiUrl, SummonerDto.class);
        SummonerDto summonerDto = responseEntity.getBody();
        List<MatchDto> dataList = getMatch(summonerDto.getPuuid(), userAverage);
        userAverage.updateAverKda(userAverage, count);
        refactorForResponse(userAverage, summonerDto);
        FinalResponseDto finalResponseDto = FinalResponseDto.builder()
                .summoner(summonerDto)
                .matchInfo(dataList)
                .userAverage(userAverage).build();
        return ResponseEntity.ok(finalResponseDto);
    }

    public List<MatchDto> getMatch(String puuid, UserAverageDto userAverageDto) {
        log.info("MatchList 검색 시작");
        String apiUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid + "/ids"
                + "?type=ranked&start=0&count=" + count + "&api_key=" + apiKey;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String[]> MatchId = restTemplate.getForEntity(apiUrl, String[].class);
        String[] dataArray = MatchId.getBody();
        return findMatchById(dataArray, puuid, userAverageDto);

    }

//    public List<MatchDto> getMatch(String puuid, UserAverageDto userAverageDto, String matchId) {
//        log.info("갱신할 MatchList 검색 시작");
//        String apiUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid + "/ids"
//                + "?type=ranked&start=0&count=" + count + "&api_key=" + apiKey;
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String[]> MatchId = restTemplate.getForEntity(apiUrl, String[].class);
//        String[] dataArray = MatchId.getBody();
//        OptionalInt matchIdIndexOptional = IntStream.range(0, dataArray.length)
//                .filter(i -> dataArray[i].equals(matchId))
//                .findFirst();
//        int matchIdIndex = matchIdIndexOptional.orElse(10);
//        String[] refreshArray = Arrays.copyOfRange(dataArray, 0, matchIdIndex);
//        return findMatchById(refreshArray, puuid, userAverageDto);
//    }

    public List<MatchDto> findMatchById(String[] matchIds, String puuid, UserAverageDto userAverageDto) {

        List<MatchDto> matchDtos = new ArrayList<>();
        for (String matchId : matchIds) {
            log.info("매치 단건 상세조회 시작");
            String apiUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchId + "?api_key=" + apiKey;
            RestTemplate restTemplate = new RestTemplate();
            MatchDto matchDto = restTemplate.getForObject(apiUrl, MatchDto.class);
            matchDto.getInfo().updateGameTime(System.currentTimeMillis());
            List<ParticipantDto> participantDtoList = findParticipantByPuuid(matchDto.getInfo(), matchDto.getInfo().getParticipants(), puuid, userAverageDto);
            matchDto.getInfo().updateParticipantsList(participantDtoList);
            matchDtos.add(matchDto);
        }
        return matchDtos;
    }

    private Integer findSubRune(ParticipantDto participantDto) {
        return participantDto.getPerks().getStyles().get(1).getStyle();
    }

    private Integer findMainRune(ParticipantDto participantDto) {
        return participantDto.getPerks().getStyles().get(0).getSelections().get(0).getPerk();
    }

    public List<ParticipantDto> findParticipantByPuuid(InfoDto infoDto, List<ParticipantDto> participants, String puuid, UserAverageDto userAverageDto) {
        List<ParticipantDto> participantDtoList = (participants.stream()
                .map(participant -> {
                    if (participant.getPuuid().equals(puuid)) {
                        ParticipantDto searchUserInfo = participant;
                        infoDto.updateSearchUserInfo(refactorForSearchUser(userAverageDto, searchUserInfo));
                        if (!userAverageDto.getPositionList().containsKey(participant.getTeamPosition())) {
                            userAverageDto.getPositionList().put(participant.getTeamPosition(), PositionCountDto.builder()
                                    .count(1).build());
                        } else {
                            userAverageDto.getPositionList().get(participant.getTeamPosition()).addPositionCount();
                        }
                        if (!userAverageDto.getPlayChampionList().containsKey(participant.getChampionName())) {
                            userAverageDto.getPlayChampionList().put(participant.getChampionName(), RecentCountDto.builder()
                                    .killCount(participant.getKills())
                                    .deathCount(participant.getDeaths())
                                    .assistCount(participant.getAssists())
                                    .winCount(participant.getWin() ? 1 : 0)
                                    .loseCount(participant.getWin() ? 0 : 1)
                                    .build());
                        } else {
                            userAverageDto.getPlayChampionList().get(participant.getChampionName()).addCount(participant);
                        }
                    }
                    ParticipantDto participantDto = new ParticipantDto(participant.getSummonerName(), participant.getChampionName(), participant.getTeamId());
                    participantDto.updateTeamToString();
                    return participantDto;
                })
                .toList());
        return participantDtoList;
    }

    private ParticipantDto refactorForSearchUser(UserAverageDto userAverageDto, ParticipantDto participant) {
        userAverageDto.addUserTotalKda(participant.getWin(), participant.getKills(), participant.getDeaths(), participant.getAssists());
        if (participant.getDeaths() == 0) {
            participant.updateIsPerpect();
        } else {
            participant.updateKda();
        }
        participant.updateCs();
        participant.updateRune(findMainRune(participant), findSubRune(participant));
        participant.updateSpell(
                placeSpellNameBySpellId(participant.getSummoner1Id()), placeSpellNameBySpellId(participant.getSummoner2Id()));
        return participant;
    }

    public String placeSpellNameBySpellId(Integer spellId) {
        String spellName;
        switch (spellId) {
            case 1:
                spellName = "SummonerBoost";
                break;
            case 3:
                spellName = "SummonerExhaust";
                break;
            case 4:
                spellName = "SummonerFlash";
                break;
            case 6:
                spellName = "SummonerHaste";
                break;
            case 7:
                spellName = "SummonerHeal";
                break;
            case 11:
                spellName = "SummonerSmite";
                break;
            case 12:
                spellName = "SummonerTeleport";
                break;
            case 14:
                spellName = "SummonerDot";
                break;
            case 21:
                spellName = "SummonerBarrier";
                break;
            default:
                spellName = "Unknown"; // 정의되지 않은 값일 경우 기본값으로 "Unknown" 반환
                break;
        }
        return spellName;
    }

    private void refactorForResponse(UserAverageDto userAverage, SummonerDto summonerDto) {
        for (RecentCountDto recentCountDto : userAverage.getPlayChampionList().values()) {
            recentCountDto.averKda();
        }
        if (userAverage.getUserAverDeath() == 0) {
            userAverage.updateIsPerpect();
        }
        for (PositionCountDto positionCountDto : userAverage.getPositionList().values()) {
            positionCountDto.updatePositionOdds(count);
        }
        summonerDto.updateForResponse();
        userAverage.updateChampListDESC();
    }

}
