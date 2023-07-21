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

    public RankingData getRankingData(String tier, String rank) {
        try {
            String apiUrl = "https://kr.api.riotgames.com/lol/league/v4/entries/RANKED_SOLO_5x5/" + tier + "/" + rank
                    + "?api_key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<RankingData[]> responseEntity = restTemplate.getForEntity(apiUrl, RankingData[].class);
            RankingData[] rankingDataArray = responseEntity.getBody();

            // 라이엇 API로부터 받은 데이터를 가공하여 반환하는 로직을 구현합니다.
            // 예를 들어, RankingData[] 배열을 필요한 형태로 가공하거나 RankingData 객체 하나만 반환할 수 있습니다.
            // 여기서는 간단하게 첫 번째 요소만 반환하도록 하겠습니다.
            if (rankingDataArray != null && rankingDataArray.length > 0) {
                return rankingDataArray[0];
            } else {
                return null;
            }
        } catch (Exception e) {
            // API 호출이 실패하거나 예외가 발생한 경우에는 에러 로그를 남기고 null을 반환합니다.
            // 실제 서비스에서는 로그를 남기는 대신 사용자에게 적절한 에러 메시지를 반환하는 것이 좋습니다.
            e.printStackTrace();
            return null;
        }
    }

    public List<RankingData> getAllRankingData() {
        try {
            String apiUrl = "https://kr.api.riotgames.com/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5?api_key=" + apiKey;

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<RankingData> responseEntity = restTemplate.getForEntity(apiUrl, RankingData.class);
            RankingData rankingData = responseEntity.getBody();

            if (rankingData != null && rankingData.getEntries() != null && !rankingData.getEntries().isEmpty()) {
                // RankingEntry 객체들을 RankingData 객체들로 변환하여 리스트로 반환
                List<RankingData> rankingDataList = new ArrayList<>();
                for (RankingEntry entry : rankingData.getEntries()) {
                    RankingData data = new RankingData();
                    data.setSummonerName(entry.getSummonerName());
                    data.setTier(entry.getTier());
                    data.setRank(entry.getRank());
                    data.setLeaguePoints(entry.getLeaguePoints());
                    rankingDataList.add(data);
                }
                return rankingDataList;
            } else {
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


}

