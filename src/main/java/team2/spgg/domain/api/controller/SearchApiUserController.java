package team2.spgg.domain.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import team2.spgg.domain.api.service.SearchApiUserService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SearchApiUserController {
    private final SearchApiUserService searchApiUserService;

    @GetMapping("/api/search/test")
    public ResponseEntity<?> getSummoner(@RequestParam String summonerName) throws IOException {
        System.out.println("summonerName = " + summonerName);
        return searchApiUserService.getSummoner(summonerName);
    }
}