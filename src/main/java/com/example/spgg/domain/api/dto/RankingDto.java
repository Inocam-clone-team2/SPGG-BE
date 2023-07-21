package com.example.spgg.domain.api.dto;

import com.example.spgg.domain.api.entity.RankingModel;
import com.example.spgg.domain.api.entity.SummonerModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingDto {

    private int type;

    private long allUser;

    private SummonerModel summonerModel;

    private RankingModel rankingModel;

}