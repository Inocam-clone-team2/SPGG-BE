package team2.spgg.domain.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team2.spgg.domain.api.dto.RankingEntry;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RiotApiService {

    private final RestTemplate restTemplate;
    private List<RankingEntry> rankingDataList;
    private final ObjectMapper objectMapper;

    @Value("${riot.api.key}")
    public String apiKey;

    public RiotApiService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
        rankingDataList = new ArrayList<>();
    }

    // 1. 전체 유저 랭킹 조회기능
    public List<RankingEntry> getAllRankingData() {
        if (rankingDataList.isEmpty()) {
            // 데이터가 없는 경우 API로부터 데이터를 가져와서 저장
            log.info("API로부터 모든 랭킹 데이터를 가져와 저장합니다.");
            rankingDataList = fetchAndSaveAllRankingData();
            log.info("로컬 캐시에서 모든 랭킹 데이터를 조회합니다.");
        }
        return new ArrayList<>(rankingDataList); // 새로운 ArrayList를 생성하여 반환
    }

    // 2. 티어별 특정 랭킹 조회기능
    public List<RankingEntry> getRankingDataDetail(String tier, String rank) {
        String apiUrl = buildApiUrl(tier, rank);
        List<RankingEntry> rankingDataList = getRankingData(apiUrl);
        log.info("티어: {} / 랭크: {} 에 해당하는 랭킹 데이터를 조회합니다.", tier, rank);
        return rankingDataList;
    }

    // 3. 티어별 상위 N명씩 조회 (마스터 ~ 아이언)
    public List<RankingEntry> getTopPlayersForEachTier(int n) {
        String[] tiers = {"DIAMOND", "PLATINUM", "GOLD", "SILVER", "BRONZE", "IRON"};
        String[] ranks = {"I", "II", "III", "IV"};

        List<RankingEntry> topPlayersForEachTier = new ArrayList<>();

        // 마스터 랭킹 상위 N명 추가
        List<RankingEntry> masterRankingData = getMasterRankingData();
        log.info("마스터 랭킹 상위 {}명을 조회합니다.", n);
        topPlayersForEachTier.addAll(getTopPlayers(masterRankingData, n));
        // 다이아 이하 랭킹 N명씩 추가
        log.info("다이아 이하 랭킹 상위 {}명씩 조회합니다.", n);
        topPlayersForEachTier.addAll(getTopPlayersForEachTierByRank(tiers, ranks, n));

        return topPlayersForEachTier;
    }

    // 4. 마스터 랭킹 조회
    public List<RankingEntry> getMasterRankingData() {
        String masterApiUrl = buildMasterApiUrl();
        log.info("마스터 랭킹 데이터를 조회합니다.");
        return getRankingDataForMaster(masterApiUrl);
    }

    // 하위 부속 메소드

    /**
     * 마스터 랭킹을 API로부터 조회하여 가져오는 메소드입니다.
     *
     * @param apiUrl API 호출에 사용할 URL
     * @return 마스터 랭킹 데이터를 담고 있는 리스트
     */
    private List<RankingEntry> getRankingDataForMaster(String apiUrl) {
        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Map<String, Object>>() {}
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseMap = responseEntity.getBody();
                if (responseMap != null) {
                    List<Map<String, Object>> entries = (List<Map<String, Object>>) responseMap.get("entries");
                    if (entries != null) {
                        List<RankingEntry> masterRankingData = entries.stream()
                                .map(rankingData -> {
                                    RankingEntry rankingEntry = new RankingEntry();
                                    rankingEntry.setSummonerName((String) rankingData.get("summonerName"));
                                    rankingEntry.setTier("MASTER"); // Set the tier to "MASTER" for all entries in Master tier
                                    rankingEntry.setLeaguePoints((int) rankingData.get("leaguePoints"));
                                    rankingEntry.setWins((int) rankingData.get("wins"));
                                    rankingEntry.setLosses((int) rankingData.get("losses"));

                                    // Calculate and set the winRate
                                    int totalGames = rankingEntry.getWins() + rankingEntry.getLosses();
                                    double winRate = Math.round((double) rankingEntry.getWins() / totalGames * 100);
                                    rankingEntry.setWinRate(winRate);

                                    return rankingEntry;
                                })
                                .collect(Collectors.toList());

                        return masterRankingData;
                    }
                }
            }
            return Collections.emptyList();
        } catch (Exception e) {
            // 로깅 추가: 요청과 응답 내용을 출력
            log.error("API 요청 URL: " + apiUrl, e);
            throw e;
        }
    }

    // 하위 부속 메소드: 모든 랭킹 데이터를 API로부터 가져와 저장하는 메소드
    private List<RankingEntry> fetchAndSaveAllRankingData() {
        List<RankingEntry> allRankingData = new ArrayList<>();

        // 다른 티어와 랭크의 랭킹 데이터를 가져오기 위한 티어와 랭크의 배열을 정의
        String[] tiers = {"DIAMOND", "PLATINUM", "GOLD", "SILVER", "BRONZE", "IRON"};
        String[] ranks = {"I", "II", "III", "IV"};

        for (String tier : tiers) {
            for (String rank : ranks) {
                List<RankingEntry> response = getRankingDataDetail(tier, rank);
                allRankingData.addAll(response);
            }
        }

        // 랭킹 데이터 저장
        saveRankingData(allRankingData);

        return allRankingData;
    }

    // 하위 부속 메소드: RankingData를 RankingEntry로 변환하는 메서드
    private Function<Map<String, Object>, RankingEntry> convertToRankingEntry() {
        return rankingData -> {
            RankingEntry.RankingEntryBuilder builder = RankingEntry.builder()
                    .summonerName((String) rankingData.get("summonerName"))
                    .tier((String) rankingData.get("tier"))
                    .rank((String) rankingData.get("rank"))
                    .leaguePoints((int) rankingData.get("leaguePoints"))
                    .wins((int) rankingData.get("wins"))
                    .losses((int) rankingData.get("losses"));

            // Calculate and set the winRate
            int totalGames = (int) rankingData.get("wins") + (int) rankingData.get("losses");
            double winRate = Math.round((float) (int) rankingData.get("wins") / totalGames * 100);
            builder.winRate(winRate);

            return builder.build();
        };
    }

    // 하위 부속 메소드: API 호출하여 RankingData 가져오는 메서드
    private List<RankingEntry> getRankingData(String apiUrl) {
        try {
            ResponseEntity<List<Map<String, Object>>> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {}
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                List<Map<String, Object>> responseList = responseEntity.getBody();
                if (responseList != null) {
                    return responseList.stream()
                            .map(convertToRankingEntry())
                            .collect(Collectors.toList());
                }
            }
            return Collections.emptyList();
        } catch (Exception e) {
            // 로깅 추가: 요청과 응답 내용을 출력
            log.error("API 요청 URL: " + apiUrl, e);
            throw e;
        }
    }

    public void saveRankingData(List<RankingEntry> rankingEntries) {
        rankingDataList.addAll(rankingEntries);
        rankingDataList.sort(Comparator.comparingInt(RankingEntry::getLeaguePoints).reversed());
        if (rankingDataList.size() > 60) { // 상위 10명씩 6개 랭크에 대한 데이터 저장 (6 * 10 = 60)
            rankingDataList = rankingDataList.subList(0, 60);
        }
    }

    /**
     *
     * 하위 부속 메소드 : 특정 티어와 랭크에서 상위 n명만 반환하는 메소드
     *
     * @param tiers 티어 배열
     * @param ranks 랭크 배열
     * @param n     조회할 상위 N명의 수
     * @return 특정 티어와 랭크에서 상위 N명의 랭킹 데이터를 담고 있는 리스트
     */
    private List<RankingEntry> getTopPlayersForEachTierByRank(String[] tiers, String[] ranks, int n) {
        List<RankingEntry> topPlayersForEachTierByRank = new ArrayList<>();

        for (String tier : tiers) {
            for (String rank : ranks) {
                List<RankingEntry> response = getRankingDataDetail(tier, rank);
                topPlayersForEachTierByRank.addAll(getTopPlayers(response, n));
            }
        }

        return topPlayersForEachTierByRank;
    }

    /**
     * 하위 부속 메소드: 주어진 랭킹 데이터에서 상위 n명만 반환하는 메소드
     *
     * @param rankingData 랭킹 데이터 리스트
     * @param n           조회할 상위 N명의 수
     * @return 상위 N명의 랭킹 데이터를 담고 있는 리스트
     */
    private List<RankingEntry> getTopPlayers(List<RankingEntry> rankingData, int n) {
        return rankingData.stream()
                .sorted(Comparator.comparingInt(RankingEntry::getLeaguePoints).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    // API URL을 생성하는 보조 메서드
    private String buildApiUrl(String tier, String rank) {
        return "https://kr.api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/" + tier + "/" + rank
                + "?api_key=" + apiKey;
    }

    // 마스터 랭킹을 조회하는 API URL을 생성하는 메서드
    private String buildMasterApiUrl() {
        return "https://kr.api.riotgames.com/lol/league/v4/masterleagues/by-queue/RANKED_SOLO_5x5?api_key=" + apiKey;
    }
}
