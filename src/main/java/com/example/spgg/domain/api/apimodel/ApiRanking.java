package com.example.spgg.domain.api.apimodel;

import com.example.spgg.domain.api.apimodel.attr.rank.Entry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiRanking {

    public String tier;
    public String leagueId;
    public String queue;
    public String name;
    public List<Entry> entries;

}