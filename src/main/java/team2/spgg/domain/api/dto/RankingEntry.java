package team2.spgg.domain.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RankingEntry {
    private String summonerName;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int summonerLevel;
    private int wins;
    private int losses;
}
