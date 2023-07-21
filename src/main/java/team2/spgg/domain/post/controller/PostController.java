package team2.spgg.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team2.spgg.domain.post.dto.PostRequestDto;
import team2.spgg.domain.post.dto.PostSearchCondition;
import team2.spgg.domain.post.service.PostService;
import team2.spgg.global.responseDto.ApiResponse;
import team2.spgg.global.security.UserDetailsImpl;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    /**
     * 게시물을 검색합니다.
     *
     * @param condition 검색 조건
     * @param pageable  페이지 정보
     * @return ApiResponse 객체 (게시물 검색 결과 포함)
     */
    @GetMapping
    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return postService.searchPost(condition, pageable);
    }

    /**
     * 특정 게시물을 조회합니다.
     *
     * @param postId 게시물 ID
     * @return ApiResponse 객체 (특정 게시물 조회 결과 포함)
     */
    @GetMapping("/{postId}")
    public ApiResponse<?> readOnePost(@PathVariable Long postId) {
        return postService.getSinglePost(postId);
    }

    /**
     * 게시물을 생성합니다.
     *
     * @param postRequestDto   게시물 생성 요청 DTO
     * @param image            첨부 이미지 파일
     * @param userDetailsImpl 인증된 사용자 정보
     * @return ApiResponse 객체 (게시물 생성 성공 메시지 포함)
     */
    @PostMapping("/newpost")
    public ApiResponse<?> createPost(@RequestPart(value = "data") PostRequestDto postRequestDto,
                                     @RequestPart(value = "file", required = false) MultipartFile image,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.createPost(postRequestDto, image, userDetailsImpl.getUser());
    }

    /**
     * 게시물을 수정합니다.
     *
     * @param postId           게시물 ID
     * @param postRequestDto   게시물 수정 요청 DTO
     * @param image            첨부 이미지 파일
     * @param userDetailsImpl 인증된 사용자 정보
     * @return ApiResponse 객체 (게시물 수정 성공 메시지 포함)
     */
    @PutMapping("/{postId}")
    public ApiResponse<?> modifyPost(@PathVariable Long postId,
                                     @RequestPart(value = "data") PostRequestDto postRequestDto,
                                     @RequestPart(value = "file", required = false) MultipartFile image,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.updatePost(postId, postRequestDto, image, userDetailsImpl.getUser());
    }

    /**
     * 게시물을 삭제합니다.
     *
     * @param postId           게시물 ID
     * @param userDetailsImpl 인증된 사용자 정보
     * @return ApiResponse 객체 (게시물 삭제 성공 메시지 포함)
     */
    @DeleteMapping ("/{postId}")
    public ApiResponse<?> removePost(@PathVariable Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.deletePost(postId, userDetailsImpl.getUser());
    }
}