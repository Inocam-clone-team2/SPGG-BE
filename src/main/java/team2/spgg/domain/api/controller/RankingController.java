package team2.spgg.domain.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team2.spgg.domain.api.dto.RankingEntry;
import team2.spgg.domain.api.service.RiotApiService;
import team2.spgg.global.responseDto.ApiResponse;

import java.util.List;

import static team2.spgg.global.responseDto.ApiResponse.success;

@RestController
@RequestMapping("/ranking")
public class RankingController {

    private final RiotApiService riotApiService;

    @Autowired
    public RankingController(RiotApiService riotApiService) {
        this.riotApiService = riotApiService;
    }

    @GetMapping
    public ApiResponse<?> getRankingDataDetail(@RequestParam("tier") String tier,
                                               @RequestParam("rank") String rank) {
        return success(riotApiService.getRankingDataDetail(tier, rank));
    }

    @GetMapping("/top10")
    public ApiResponse<?> getTop10RankingData() {
        List<RankingEntry> top10Data = riotApiService.getTopPlayersForEachTier(5);
        return success(top10Data);
    }
    @GetMapping("/all")
    public ApiResponse<?> getAllRankings() {
        return success(riotApiService.getAllRankingData());
    }

    @GetMapping("/master")
    public ApiResponse<?> getMasterRankings() {
        return success(riotApiService.getMasterRankingData());
    }
}
