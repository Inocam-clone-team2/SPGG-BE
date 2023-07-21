package com.example.spgg.domain.api.apimodel;

import com.example.spgg.domain.api.apimodel.attr.matchentry.Match;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// https://kr.api.riotgames.com/lol/match/v4/matchlists/by-account/KeU7EWsN4M_49nHyece2ErlHksjMr4mjjP1jD_A8czKL?api_key=RGAPI-8f2ab161-b201-4d25-a846-17abf656e8e7

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiMatchEntry {

    private List<Match> matches;
    private long startIndex;
    private long endIndex;
    private long totalGames;

}