package team2.spgg.domain.preference.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import team2.spgg.domain.preference.service.LikeService;
import team2.spgg.global.responseDto.ApiResponse;
import team2.spgg.global.security.UserDetailsImpl;

@Controller
@RequestMapping("/api/post/{postId}/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<?> updateLike(@PathVariable Long postId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                        RedirectAttributes redirectAttributes) {
        likeService.updateLike(postId, userDetailsImpl.getUser());

        // 게시물의 상세 페이지로 리다이렉트
        redirectAttributes.addFlashAttribute("message", "좋아요를 업데이트하였습니다.");
        return ResponseEntity.status(302).header("Location", "/api/post/" + postId).build();
    }
}