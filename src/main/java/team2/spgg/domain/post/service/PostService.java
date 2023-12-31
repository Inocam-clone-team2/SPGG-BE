package team2.spgg.domain.post.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import team2.spgg.domain.preference.entity.Like;
import team2.spgg.domain.preference.repository.LikeRepository;
import team2.spgg.domain.user.entity.User;
import team2.spgg.domain.user.entity.UserRoleEnum;
import team2.spgg.domain.user.repository.UserRepository;
import team2.spgg.global.exception.InvalidConditionException;
import team2.spgg.global.jwt.JwtProvider;
import team2.spgg.global.responseDto.ApiResponse;
import team2.spgg.global.utils.ResponseUtils;

import static team2.spgg.global.stringCode.ErrorCodeEnum.POST_NOT_EXIST;
import static team2.spgg.global.stringCode.ErrorCodeEnum.USER_NOT_MATCH;
import static team2.spgg.global.stringCode.SuccessCodeEnum.*;
import static team2.spgg.global.utils.ResponseUtils.ok;
import static team2.spgg.global.utils.ResponseUtils.okWithMessage;
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final S3Service s3Service;
    @Autowired
    private final JwtProvider jwtProvider;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    // 최신순 전체조회
    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return ok(postRepository.serachPostByPage(condition, pageable));
    }
    // 좋아요 (인기순) 조회
    public ApiResponse<?> searchPostByPopularity(PostSearchCondition condition, Pageable pageable) {
        return ok(postRepository.searchPostByPageByPopularity(condition, pageable));
    }
    // 조회수 조회
    public ApiResponse<?> searchPostByMostView(PostSearchCondition condition, Pageable pageable) {
        return ok(postRepository.searchPostByPageByMostView(condition, pageable));
    }

    @Transactional
    public ApiResponse<?> createPost(PostRequestDto postRequestDto, MultipartFile image, User user) {
        String imageUrl = s3Service.upload(image);
        postRepository.save(new Post(postRequestDto, imageUrl, user));
        log.info("'{}'님이 새로운 게시물을 생성했습니다.", user.getNickname());
        return ResponseUtils.okWithMessage(POST_CREATE_SUCCESS);
    }

    @Transactional
    public ApiResponse<?> getSinglePost(Long postId , HttpServletRequest req) {
        String token = jwtProvider.getTokenFromHeader(req);
        String subStringToken;
        Boolean isLike=false;
        if(token!=null) {
            subStringToken = jwtProvider.substringHeaderToken(token);
            Claims userInfo = jwtProvider.getUserInfoFromToken(subStringToken);
            Post post = postRepository.findById(postId).orElseThrow(()->new IllegalArgumentException("?"));
            User user = userRepository.findByEmail(userInfo.getSubject()).orElseThrow(()->new IllegalArgumentException("?"));

            if(likeRepository.findByPostAndUser(post, user).isPresent()) {
                isLike = true;
            }
        }
        Post post = postRepository.findDetailPost(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));
        log.info("게시물 ID '{}' 조회 성공", postId);
        post.increaseViews();
        return ok(new PostResponseDto(post, isLike));
    }

    @Transactional
    public ApiResponse<?> updatePost(Long postId, PostRequestDto postRequestDto, MultipartFile image, User user) {
        Post post = confirmPost(postId, user);
        updatePostDetail(postRequestDto, image, post);
        log.info("'{}'님이 게시물 ID '{}'의 정보를 업데이트했습니다.", user.getNickname(), postId);
        return okWithMessage(POST_UPDATE_SUCCESS);
    }

    @Transactional
    public ApiResponse<?> deletePost(Long postId, User user) {
        Post post = confirmPost(postId, user);
        deleteImage(post);
        postRepository.delete(post);
        log.info("'{}'님이 게시물 ID '{}'를 삭제했습니다.", user.getNickname(), postId);
        return okWithMessage(POST_DELETE_SUCCESS);
    }



    private void updatePostDetail(PostRequestDto postRequestDto, MultipartFile image, Post post) {
        if (image != null && !image.isEmpty()) {
            String existingImageUrl = post.getImage();
            String imageUrl = s3Service.upload(image);
            post.updateAll(postRequestDto, imageUrl);

            // 새로운 이미지 업로드 후에 기존 이미지 삭제
            if (StringUtils.hasText(existingImageUrl)) {
                s3Service.delete(existingImageUrl);
            }
        }
        post.update(postRequestDto);
    }

    private void deleteImage(Post post) {
        String imageUrl = post.getImage();
        if (StringUtils.hasText(imageUrl)) {
            s3Service.delete(imageUrl);
        }
    }


    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));
    }

    private Post confirmPost(Long postId, User user) {
        Post post = findPost(postId);
        if (!user.getId().equals(post.getUser().getId())) {
            throw new InvalidConditionException(USER_NOT_MATCH);
        }
        return post;
    }
}