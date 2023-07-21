package team2.spgg.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import team2.spgg.domain.post.dto.PostRequestDto;
import team2.spgg.domain.post.dto.PostResponseDto;
import team2.spgg.domain.post.dto.PostSearchCondition;
import team2.spgg.domain.post.entity.Post;
import team2.spgg.domain.post.repository.PostRepository;
import team2.spgg.domain.user.entity.User;
import team2.spgg.global.exception.InvalidConditionException;
import team2.spgg.global.responseDto.ApiResponse;
import team2.spgg.global.stringCode.ErrorCodeEnum;
import team2.spgg.global.stringCode.SuccessCodeEnum;
import team2.spgg.global.utils.ResponseUtils;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;

    /**
     * 게시물을 검색하는 메소드입니다.
     *
     * @param condition 검색 조건
     * @param pageable  페이지 정보
     * @return 검색된 게시물 목록과 페이징 정보를 포함한 ApiResponse
     */
    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return ResponseUtils.ok(postRepository.serachPostBySlice(condition, pageable));
    }

    /**
     * 게시물을 생성하는 메소드입니다.
     *
     * @param postRequestDto 게시물 요청 DTO
     * @param image          업로드할 이미지 파일
     * @param user           작성자 정보
     * @return ApiResponse with success message
     */
    @Transactional
    public ApiResponse<?> createPost(PostRequestDto postRequestDto, MultipartFile image, User user) {
        String imageUrl = s3Service.upload(image);
        postRepository.save(new Post(postRequestDto, imageUrl, user));
        return ResponseUtils.okWithMessage(SuccessCodeEnum.POST_CREATE_SUCCESS);
    }

    /**
     * 특정 게시물을 조회하는 메소드입니다.
     *
     * @param postId 게시물 ID
     * @return 조회된 게시물 정보를 포함한 ApiResponse
     */
    public ApiResponse<?> getSinglePost(Long postId) {
        Post post = postRepository.findDetailPost(postId).orElseThrow(() ->
                new InvalidConditionException(ErrorCodeEnum.POST_NOT_EXIST));
        return ResponseUtils.ok(new PostResponseDto(post));
    }

    /**
     * 게시물을 수정하는 메소드입니다.
     *
     * @param postId         게시물 ID
     * @param postRequestDto 수정할 게시물 요청 DTO
     * @param image          업로드할 이미지 파일
     * @param user           작성자 정보
     * @return ApiResponse with success message
     */
    @Transactional
    public ApiResponse<?> updatePost(Long postId, PostRequestDto postRequestDto, MultipartFile image, User user) {
        Post post = confirmPost(postId, user);
        post.update(postRequestDto);
        updatePostImage(image, post);
        return ResponseUtils.okWithMessage(SuccessCodeEnum.POST_UPDATE_SUCCESS);
    }

    /**
     * 게시물의 이미지를 업데이트하는 메소드입니다.
     *
     * @param image 업로드할 이미지 파일
     * @param post  게시물 정보
     */
    private void updatePostImage(MultipartFile image, Post post) {
        // 이미지 업로드
        if (image != null && !image.isEmpty()) {
            String existingImageUrl = post.getImage();
            String imageUrl = s3Service.upload(image);
            post.setImage(imageUrl);

            // 새로운 이미지 업로드 후에 기존 이미지 삭제
            if (StringUtils.hasText(existingImageUrl)) {
                s3Service.delete(existingImageUrl);
            }
        }
    }

    /**
     * 게시물을 삭제하는 메소드입니다.
     *
     * @param postId 게시물 ID
     * @param user   작성자 정보
     * @return ApiResponse with success message
     */
    @Transactional
    public ApiResponse<?> deletePost(Long postId, User user) {
        Post post = confirmPost(postId, user);
        deleteImage(post);
        postRepository.delete(post);
        return ResponseUtils.okWithMessage(SuccessCodeEnum.POST_DELETE_SUCCESS);
    }

    /**
     * 게시물의 이미지를 삭제하는 메소드입니다.
     *
     * @param post 게시물 정보
     */
    private void deleteImage(Post post) {
        String imageUrl = post.getImage();
        if (StringUtils.hasText(imageUrl)) {
            s3Service.delete(imageUrl);
        }
    }

    /**
     * 특정 게시물을 조회하는 메소드입니다.
     *
     * @param postId 게시물 ID
     * @return 조회된 게시물 정보
     * @throws InvalidConditionException 게시물이 존재하지 않을 경우 예외 발생
     */
    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new InvalidConditionException(ErrorCodeEnum.POST_NOT_EXIST));
    }

    /**
     * 게시물을 확인하고 작성자인지 확인하는 메소드입니다.
     *
     * @param postId 게시물 ID
     * @param user   사용자 정보
     * @return 확인된 게시물 정보
     * @throws InvalidConditionException 작성자가 아닐 경우 예외 발생
     */
    private Post confirmPost(Long postId, User user) {
        Post post = findPost(postId);
        if (!user.getId().equals(post.getUser().getId())) {
            throw new InvalidConditionException(ErrorCodeEnum.USER_NOT_MATCH);
        }
        return post;
    }
}

