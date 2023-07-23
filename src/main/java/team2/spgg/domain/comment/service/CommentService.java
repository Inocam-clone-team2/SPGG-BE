package team2.spgg.domain.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.spgg.domain.comment.dto.CommentRequestDto;
import team2.spgg.domain.comment.entity.Comment;
import team2.spgg.domain.comment.repository.CommentRepository;
import team2.spgg.domain.post.entity.Post;
import team2.spgg.domain.post.repository.PostRepository;
import team2.spgg.domain.user.entity.User;
import team2.spgg.global.exception.InvalidConditionException;
import team2.spgg.global.responseDto.ApiResponse;
import team2.spgg.global.stringCode.ErrorCodeEnum;
import team2.spgg.global.stringCode.SuccessCodeEnum;
import team2.spgg.global.utils.ResponseUtils;
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public ApiResponse<?> createComment(Long postId, CommentRequestDto commentRequestDto, User user) {
        log.info("'{}' 사용자가 '{}' 게시물에 댓글을 작성하였습니다.", user.getNickname(), postId);
        Comment comment = new Comment(commentRequestDto, user);
        Post post = findPost(postId);
        post.addComment(comment);
        commentRepository.save(comment);
        return ResponseUtils.okWithMessage(SuccessCodeEnum.COMMENT_CREATE_SUCCESS);
    }

    public ApiResponse<?> updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(commentId);
        checkUsername(comment, user);
        comment.update(commentRequestDto);
        log.info("'{}' 사용자가 '{}' 댓글을 수정하였습니다.", user.getNickname(), commentId);
        return ResponseUtils.okWithMessage(SuccessCodeEnum.COMMENT_UPDATE_SUCCESS);
    }

    public ApiResponse<?> deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        checkUsername(comment, user);
        Post post = comment.getPost();
        post.getCommentList().remove(comment);
        commentRepository.delete(comment);
        log.info("'{}' 사용자가 '{}' 댓글을 삭제하였습니다.", user.getNickname(), commentId);
        return ResponseUtils.okWithMessage(SuccessCodeEnum.COMMENT_DELETE_SUCCESS);
    }

    @Transactional(readOnly = true)
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new InvalidConditionException(ErrorCodeEnum.COMMENT_NOT_EXIST));
    }

    @Transactional(readOnly = true)
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new InvalidConditionException(ErrorCodeEnum.POST_NOT_EXIST));
    }

    private void checkUsername(Comment comment, User user) {
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new InvalidConditionException(ErrorCodeEnum.USER_NOT_MATCH);
        }
    }
}
