package team2.spgg.domain.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team2.spgg.domain.api.dto.searchapiuser.SummonerDto;
import team2.spgg.domain.api.dto.searchapiuser.FinalResponseDto;
import team2.spgg.domain.api.dto.searchapiuser.MatchDto;
import team2.spgg.domain.api.dto.searchapiuser.ParticipantDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchApiUserService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Integer count = 5;
    @Value("${riot.api.key}") // application.properties에 정의된 riot.api.key 값을 읽어옵니다.
    public String apiKey;

    public ResponseEntity<FinalResponseDto> getSummoner(String summonerName) throws IOException {
        try {
            String apiUrl = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/" + summonerName + "?api_key=" + apiKey;
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<SummonerDto> responseEntity = restTemplate.getForEntity(apiUrl, SummonerDto.class);
            SummonerDto summonerDto = responseEntity.getBody();
            assert summonerDto != null;
            List<MatchDto> dataList = getMatch(summonerDto.getPuuid());
            summonerDto.updateForResponse();
            FinalResponseDto finalResponseDto = FinalResponseDto.builder().summoner(summonerDto).matchInfo(new ArrayList<>()).build();
            finalResponseDto.updateInfoForResponse(dataList);
            return ResponseEntity.ok(finalResponseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<MatchDto> getMatch(String puuid) {
        try {
            String apiUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/by-puuid/" + puuid + "/ids"
                    + "?type=ranked&start=0&count=" + count + "&api_key=" + apiKey;

            System.out.println("apiUrl = " + apiUrl);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String[]> MatchId = restTemplate.getForEntity(apiUrl, String[].class);
            String[] dataArray = MatchId.getBody();
            return findMatchById(dataArray, puuid);
        } catch (Exception e) {
            return null;
        }
    }

    public List<MatchDto> findMatchById(String[] matchIds, String puuid) {
        try {
            List<MatchDto> matchDtos = new ArrayList<>();
            for (String matchId : matchIds) {
                String apiUrl = "https://asia.api.riotgames.com/lol/match/v5/matches/" + matchId + "?api_key=" + apiKey;
                System.out.println("apiUrl = " + apiUrl);
                RestTemplate restTemplate = new RestTemplate();
                MatchDto matchDto= restTemplate.getForObject(apiUrl, MatchDto.class);
                assert matchDto != null;
                List<ParticipantDto> participantDtoList = findParticipantByPuuid(matchDto.getInfo().getParticipants(), puuid);
                matchDto.getInfo().updateParticipantsList(participantDtoList);

                System.out.println("update완료");
                matchDtos.add(matchDto);
            }

            return matchDtos;
        } catch (Exception e) {
            System.out.println("익셉션");
            return null;
        }
    }

    private Integer findSubRune(ParticipantDto participantDto) {
        return participantDto.getPerks().getStyles().get(1).getStyle();
    }

    private Integer findMainRune(ParticipantDto participantDto) {
        return participantDto.getPerks().getStyles().get(0).getSelections().get(0).getPerk();
    }

    public List<ParticipantDto> findParticipantByPuuid(List<ParticipantDto> participants, String puuid) {
        List<ParticipantDto> participantDtoList = (participants.stream()
                .map(participant -> {
                    if (participant.getPuuid().equals(puuid)) {
                        participant.updateRune(findMainRune(participant), findSubRune(participant));
                        participant.updateSpell(
                                placeSpellNameBySpellId(participant.getSummoner1Id()), placeSpellNameBySpellId(participant.getSummoner2Id()));
                        return participant; // filter에 걸리면 수정 없이 그대로 반환
                    } else {
                        // filter에 걸리지 않으면 새로운 ParticipantDto 객체를 생성하여 반환
                        return new ParticipantDto(participant.getSummonerName(), participant.getChampionName(), participant.getTeamId());
                    }
                })
                .toList());
        return participantDtoList;
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
}