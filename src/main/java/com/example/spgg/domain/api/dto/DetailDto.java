package com.example.spgg.domain.api.dto;


import com.example.spgg.domain.api.entity.MatchCommonModel;
import com.example.spgg.domain.api.entity.MatchSummonerModel;
import com.example.spgg.domain.api.entity.MatchTeamModel;
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