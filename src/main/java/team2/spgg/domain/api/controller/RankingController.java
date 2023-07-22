package team2.spgg.domain.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team2.spgg.domain.api.dto.RankingData;
import team2.spgg.domain.api.dto.RankingEntry;
import team2.spgg.domain.api.service.RiotApiService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping
public class RankingController {

    private final RiotApiService riotApiService;

    @Autowired
    public RankingController(RiotApiService riotApiService) {
        this.riotApiService = riotApiService;
    }

    // 랭킹 조회를 위한 API 호출 메서드
    @GetMapping("/ranking")
    public ResponseEntity<List<RankingEntry>> getRankingDataDetail(@RequestParam("tier") String tier,
                                                                   @RequestParam("rank") String rank) {
        try {
            List<RankingEntry> rankingData = riotApiService.getRankingDataDetail(tier, rank);

            if (!rankingData.isEmpty()) {
                return ResponseEntity.ok(rankingData);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/ranking/all")
    public ResponseEntity<List<RankingEntry>> getAllRankings() {
        try {
            // 라이엇 API를 통해 모든 랭킹 정보를 조회합니다.
            List<RankingEntry> rankingEntries = riotApiService.getAllRankingData();

            // 랭킹 정보가 정상적으로 조회되면 ResponseEntity에 담아서 응답합니다.
            return ResponseEntity.ok(rankingEntries);
        } catch (Exception e) {
            // 예외가 발생한 경우에는 INTERNAL_SERVER_ERROR 상태 코드로 응답합니다.
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

