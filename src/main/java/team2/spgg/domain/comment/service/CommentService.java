package team2.spgg.domain.comment.service;

import team2.spgg.domain.comment.dto.CommentRequestDto;
import team2.spgg.domain.comment.entity.Comment;
import team2.spgg.domain.comment.repository.CommentRepository;
import team2.spgg.domain.post.entity.Post;
import team2.spgg.domain.post.repository.PostRepository;
import team2.spgg.domain.user.entity.User;
import team2.spgg.global.exception.InvalidConditionException;
import team2.spgg.global.responseDto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.spgg.global.stringCode.ErrorCodeEnum;
import team2.spgg.global.stringCode.SuccessCodeEnum;
import team2.spgg.global.utils.ResponseUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    /**
     * 특정 게시물에 댓글을 생성합니다.
     *
     * @param postId            게시물 ID
     * @param commentRequestDto 댓글 생성 요청 DTO
     * @param user              사용자 정보
     * @return ApiResponse 객체 (댓글 생성 성공 메시지 포함)
     */
    public ApiResponse<?> createComment(Long postId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = new Comment(commentRequestDto, user);
        findPost(postId).addComment(comment);
        commentRepository.save(comment);
        return ResponseUtils.okWithMessage(SuccessCodeEnum.COMMENT_CREATE_SUCCESS);
    }

    /**
     * 특정 댓글을 수정합니다.
     *
     * @param commentId         댓글 ID
     * @param commentRequestDto 댓글 수정 요청 DTO
     * @param user              사용자 정보
     * @return ApiResponse 객체 (댓글 수정 성공 메시지 포함)
     */
    public ApiResponse<?> updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = findComment(commentId);
        checkUsername(commentId, user);
        comment.update(commentRequestDto);
        return ResponseUtils.okWithMessage(SuccessCodeEnum.COMMENT_UPDATE_SUCCESS);
    }

    /**
     * 특정 댓글을 삭제합니다.
     *
     * @param commentId 댓글 ID
     * @param user      사용자 정보
     * @return ApiResponse 객체 (댓글 삭제 성공 메시지 포함)
     */
    public ApiResponse<?> deleteComment(Long commentId, User user) {
        Comment comment = findComment(commentId);
        checkUsername(commentId, user);
        commentRepository.delete(comment);
        return ResponseUtils.okWithMessage(SuccessCodeEnum.COMMENT_DELETE_SUCCESS);
    }

    /**
     * 주어진 댓글 ID에 해당하는 댓글을 조회합니다.
     *
     * @param commentId 댓글 ID
     * @return 조회된 Comment 객체
     * @throws InvalidConditionException 주어진 댓글 ID에 해당하는 댓글이 존재하지 않을 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public Comment findComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new InvalidConditionException(ErrorCodeEnum.COMMENT_NOT_EXIST));
    }

    /**
     * 주어진 게시물 ID에 해당하는 게시물을 조회합니다.
     *
     * @param postId 게시물 ID
     * @return 조회된 Post 객체
     * @throws InvalidConditionException 주어진 게시물 ID에 해당하는 게시물이 존재하지 않을 경우 예외 발생
     */
    @Transactional(readOnly = true)
    public Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new InvalidConditionException(ErrorCodeEnum.POST_NOT_EXIST));
    }

    /**
     * 주어진 댓글 ID에 해당하는 댓글의 사용자명과 현재 사용자의 사용자명을 비교하여 일치하는지 확인합니다.
     *
     * @param commentId 댓글 ID
     * @param user      사용자 정보
     * @throws InvalidConditionException 사용자명이 일치하지 않을 경우 예외 발생
     */
    private void checkUsername(Long commentId, User user) {
        Comment comment = findComment(commentId);
        if (!comment.getUser().getId().equals(user.getId())) {
            throw new InvalidConditionException(ErrorCodeEnum.USER_NOT_MATCH);
        }
    }
}
