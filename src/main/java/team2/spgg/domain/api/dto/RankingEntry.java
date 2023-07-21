package team2.spgg.domain.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingEntry {
    private String summonerName;
    private String tier;
    private String rank;
    private int leaguePoints;
}
