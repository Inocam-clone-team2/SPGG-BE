package team2.spgg.domain.api.dto;

import team2.spgg.domain.api.entity.EntryModel;
import team2.spgg.domain.api.entity.MatchSummonerModel;
import team2.spgg.domain.api.entity.SummonerModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfoDto {

    private int type;
    
    private long radder;

    private SummonerModel summonerModel;

    private List<EntryModel> entryModels;

    private MatchSummonerModel matchSummonerModel;
    
}