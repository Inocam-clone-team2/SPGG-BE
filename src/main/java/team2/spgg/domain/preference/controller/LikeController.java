package team2.spgg.domain.preference.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team2.spgg.domain.post.dto.PostResponseDto;
import team2.spgg.domain.preference.service.LikeService;
import team2.spgg.domain.user.entity.User;
import team2.spgg.global.responseDto.ApiResponse;
import team2.spgg.global.security.UserDetailsImpl;

@Controller
@RequestMapping("/api/post/{postId}/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<?> updateLike(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        User user = userDetailsImpl.getUser();
        return ResponseEntity.ok(likeService.updateLike(postId, user));
    }
}
