package team2.spgg.domain.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import team2.spgg.domain.api.dto.RankingData;
import team2.spgg.domain.api.dto.RankingEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RiotApiService {

    private final RestTemplate restTemplate;

    @Value("${riot.api.key}")
    public String apiKey;

    public RiotApiService() {

        this.restTemplate = new RestTemplate();
    }

    public List<RankingEntry> getRankingDataDetail(String tier, String rank) {
        try {
            String apiUrl = "https://kr.api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/" + tier + "/" + rank
                    + "?api_key=" + apiKey;

            ResponseEntity<RankingData[]> responseEntity = restTemplate.getForEntity(apiUrl, RankingData[].class);
            RankingData[] rankingDataArray = responseEntity.getBody();

            if (rankingDataArray != null) {
                // RankingData[]를 RankingEntry 리스트로 변환하여 반환
                return Arrays.stream(rankingDataArray)
                        .map(this::convertToRankingEntry)
                        .collect(Collectors.toList());
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<RankingEntry> getAllRankingData() {
        List<RankingEntry> allRankingData = new ArrayList<>();

        // 다른 티어와 랭크의 랭킹 데이터를 가져오기 위한 티어와 랭크의 배열을 정의
        String[] tiers = {"DIAMOND", "PLATINUM", "GOLD", "SILVER", "BRONZE", "IRON"};
        String[] ranks = {"I", "II", "III", "IV"};

        // 다른 티어와 랭크에 대해 순차적으로 API 호출하여 결과를 합칩니다.
        for (String tier : tiers) {
            for (String rank : ranks) {
                List<RankingEntry> rankingData = getRankingDataDetail(tier, rank);
                allRankingData.addAll(rankingData);
            }
        }

        return allRankingData;
    }

    // RankingData를 RankingEntry로 변환하는 메서드
    private RankingEntry convertToRankingEntry(RankingData rankingData) {
        return RankingEntry.builder()
                .summonerName(rankingData.getSummonerName())
                .tier(rankingData.getTier())
                .leaguePoints(rankingData.getLeaguePoints())
                .rank(rankingData.getRank())
                .wins(rankingData.getWins())
                .losses(rankingData.getLosses())
                .winRate(rankingData.getWinRate())
                .build();
    }
}