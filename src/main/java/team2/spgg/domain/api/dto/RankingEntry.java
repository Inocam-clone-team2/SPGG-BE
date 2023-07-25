package team2.spgg.domain.api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RankingEntry {
    private String summonerName;
    private String tier;
    private String rank;
    private int leaguePoints;
    private int wins;
    private int losses;
    private double winRate;

}


