package team2.spgg.domain.api.dto;


import team2.spgg.domain.api.entity.MatchCommonModel;
import team2.spgg.domain.api.entity.MatchSummonerModel;
import team2.spgg.domain.api.entity.MatchTeamModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailDto {
	
    private int type;

    private MatchCommonModel matchCommonModel;

    private MatchTeamModel winTeam;
    private MatchTeamModel loseTeam;

    private List<MatchSummonerModel> winSummonerModels;
    private List<MatchSummonerModel> loseSummonerModels;

}