package com.example.spgg.domain.api.dto;

import com.example.spgg.domain.api.entity.EntryModel;
import com.example.spgg.domain.api.entity.MatchSummonerModel;
import com.example.spgg.domain.api.entity.SummonerModel;
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