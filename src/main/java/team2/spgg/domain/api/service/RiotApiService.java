package team2.spgg.domain.api.service;

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

@Service
public class RiotApiService {

    @Value("${riot.api.key}") // application.properties에 정의된 riot.api.key 값을 읽어옵니다.
    public String apiKey;

    public List<RankingData> getRankingData(String tier, String rank) {
        try {
            String apiUrl = "https://kr.api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/" + tier + "/" + rank
                    + "?api_key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<RankingData[]> responseEntity = restTemplate.getForEntity(apiUrl, RankingData[].class);
            RankingData[] rankingDataArray = responseEntity.getBody();

            if (rankingDataArray != null) {
                return Arrays.asList(rankingDataArray);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<RankingEntry> getAllRankingData() {
        try {
            String apiUrl = "https://kr.api.riotgames.com/lol/league/v4/masterleagues/by-queue/RANKED_SOLO_5x5?api_key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<RankingEntry[]> responseEntity = restTemplate.getForEntity(apiUrl, RankingEntry[].class);
            RankingEntry[] rankingEntries = responseEntity.getBody();

            if (rankingEntries != null) {
                return Arrays.asList(rankingEntries);
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}

