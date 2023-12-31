package team2.spgg.domain.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team2.spgg.domain.api.service.SearchApiUserService;

@RestController
@RequiredArgsConstructor
public class SearchApiUserController {
    private final SearchApiUserService searchApiUserService;

    @GetMapping("/api/search/test")
    public ResponseEntity<?> getSummoner(@RequestParam String summonerName) {
        return searchApiUserService.getSummoner(summonerName);
    }

    @GetMapping("/api/search/refresh")
    public ResponseEntity<?> getRefreshSummoner(@RequestParam String summonerName){
        return searchApiUserService.getRefreshSummoner(summonerName);
    }
}