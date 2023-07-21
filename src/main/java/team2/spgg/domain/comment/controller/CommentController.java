package team2.spgg.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team2.spgg.domain.comment.dto.CommentRequestDto;
import team2.spgg.domain.comment.service.CommentService;
import team2.spgg.global.responseDto.ApiResponse;
import team2.spgg.global.security.UserDetailsImpl;

@RestController
@RequestMapping("/post/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 게시글에 댓글을 작성합니다.
     *
     * @param postId              게시글 ID
     * @param commentRequestDto   댓글 요청 DTO
     * @param userDetailsImpl     인증된 사용자의 UserDetailsImpl 객체
     * @return                    ApiResponse 객체
     */
    @PostMapping
    public ApiResponse<?> createComment(@PathVariable Long postId,
                                        @RequestBody CommentRequestDto commentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return commentService.createComment(postId, commentRequestDto, userDetailsImpl.getUser());
    }

    /**
     * 댓글을 수정합니다.
     *
     * @param commentId           댓글 ID
     * @param commentRequestDto   댓글 요청 DTO
     * @param userDetailsImpl     인증된 사용자의 UserDetailsImpl 객체
     * @return                    ApiResponse 객체
     */
    @PutMapping("/{commentId}")
    public ApiResponse<?> updateComment(@PathVariable Long commentId,
                                        @RequestBody CommentRequestDto commentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return commentService.updateComment(commentId, commentRequestDto, userDetailsImpl.getUser());
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param commentId           댓글 ID
     * @param userDetailsImpl     인증된 사용자의 UserDetailsImpl 객체
     * @return                    ApiResponse 객체
     */
    @DeleteMapping("/{commentId}")
    public ApiResponse<?> removeComment(@PathVariable Long commentId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return commentService.deleteComment(commentId, userDetailsImpl.getUser());
    }
}