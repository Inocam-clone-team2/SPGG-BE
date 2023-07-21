package team2.spgg.domain.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RankingData {
    private String summonerName;
    private String tier;
    private String rank;
    private int leaguePoints;

    private List<RankingEntry> entries;

    public List<RankingEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<RankingEntry> entries) {
        this.entries = entries;
    }
}


