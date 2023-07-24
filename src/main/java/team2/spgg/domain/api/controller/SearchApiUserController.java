package team2.spgg.domain.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import team2.spgg.domain.api.service.SearchApiUserService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class SearchApiUserController {
    private final SearchApiUserService searchApiUserService;

    @PostMapping("/api/search/test")
    public ResponseEntity<?> getSummoner(@RequestBody String username) throws IOException {

        return searchApiUserService.getSummoner(username);
    }
}