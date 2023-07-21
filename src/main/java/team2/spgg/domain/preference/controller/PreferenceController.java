package team2.spgg.domain.preference.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import team2.spgg.domain.preference.service.PreferenceService;
import team2.spgg.global.security.UserDetailsImpl;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PreferenceController {

    private final PreferenceService preferenceService;

    /**
     * 게시물에 좋아요를 추가 또는 제거하는 메소드입니다.
     *
     * @param postId             게시물 ID
     * @param userDetailsImpl    인증된 사용자 정보
     * @param redirectAttributes 리다이렉트 시 데이터를 전달하기 위한 RedirectAttributes
     * @return 업데이트된 게시물 페이지로 리다이렉트하는 RedirectView
     */
    @PostMapping("/{postId}/like")
    public RedirectView updateLike(@PathVariable Long postId,
                                   @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                   RedirectAttributes redirectAttributes) {
        preferenceService.updateLike(postId, userDetailsImpl.getUser());
        redirectAttributes.addFlashAttribute("successMessage", "좋아요 처리가 완료되었습니다.");
        return new RedirectView("/post/" + postId);
    }

    /**
     * 게시물에 싫어요를 추가 또는 제거하는 메소드입니다.
     *
     * @param postId             게시물 ID
     * @param userDetailsImpl    인증된 사용자 정보
     * @param redirectAttributes 리다이렉트 시 데이터를 전달하기 위한 RedirectAttributes
     * @return 업데이트된 게시물 페이지로 리다이렉트하는 RedirectView
     */
    @PostMapping("/{postId}/dislike")
    public RedirectView updateDislike(@PathVariable Long postId,
                                      @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                      RedirectAttributes redirectAttributes) {
        preferenceService.updateDislike(postId, userDetailsImpl.getUser());
        redirectAttributes.addFlashAttribute("successMessage", "싫어요 처리가 완료되었습니다.");
        return new RedirectView("/post/" + postId);
    }
}
