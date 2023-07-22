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
    private int leaguePoints;
    private int wins;
    private int losses;
    private double winRate;

    public double getWinRate() {
        int totalGames = (int) (wins + losses);
        return (double) wins / totalGames * 100;
    }

}
