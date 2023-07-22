package team2.spgg.domain.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingData {
    private String summonerName;
    private String tier;
    private String rank;
    private int summonerLevel;
    private double winRate;
    private int leaguePoints;

    private List<RankingEntry> entries;
}
