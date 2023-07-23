package team2.spgg.domain.preference.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import team2.spgg.domain.preference.service.LikeService;
import team2.spgg.global.security.UserDetailsImpl;

@Controller
@RequestMapping("/api/post/{postId}/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public void updateLike(@PathVariable Long postId,
                           @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        likeService.updateLike(postId, userDetailsImpl.getUser());
    }
}
