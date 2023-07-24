package team2.spgg.domain.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import team2.spgg.domain.comment.dto.CommentRequestDto;
import team2.spgg.domain.comment.service.CommentService;
import team2.spgg.global.responseDto.ApiResponse;
import team2.spgg.global.security.UserDetailsImpl;

@RestController
@RequestMapping("/api/post/{postId}/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ApiResponse<?> createComment(@PathVariable Long postId,
                                        @RequestBody CommentRequestDto commentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return commentService.createComment(postId, commentRequestDto, userDetailsImpl.getUser());
    }

    @PutMapping("/{commentId}")
    public ApiResponse<?> updateComment(@PathVariable Long commentId,
                                        @RequestBody CommentRequestDto commentRequestDto,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return commentService.updateComment(commentId, commentRequestDto, userDetailsImpl.getUser());
    }
    @DeleteMapping("/{commentId}")
    public ApiResponse<?> removeComment(@PathVariable Long commentId,
                                        @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return commentService.deleteComment(commentId, userDetailsImpl.getUser());
    }
}