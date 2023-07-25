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
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ApiResponse<?> searchPost(PostSearchCondition condition, Pageable pageable) {
        return postService.searchPost(condition, pageable);
    }
    @GetMapping("/popular")
    public ApiResponse<?> searchPostByPopularity(PostSearchCondition condition, Pageable pageable) {
        return postService.searchPostByPopularity(condition, pageable);
    }
    @GetMapping("/mostView")
    public ApiResponse<?> searchPostByMostView(PostSearchCondition condition, Pageable pageable) {
        return postService.searchPostByMostView(condition, pageable);
    }


    @GetMapping("/{postId}")
    public ApiResponse<?> readOnePost(@PathVariable Long postId) {
        return postService.getSinglePost(postId);
    }

    @PostMapping
    public ApiResponse<?> createPost(@RequestPart(value = "data") PostRequestDto postRequestDto,
                                     @RequestPart(value = "file", required = false) MultipartFile image,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.createPost(postRequestDto, image, userDetailsImpl.getUser());
    }

    @PutMapping("/{postId}")
    public ApiResponse<?> modifyPost(@PathVariable Long postId,
                                     @RequestPart(value = "data") PostRequestDto postRequestDto,
                                     @RequestPart(value = "file", required = false) MultipartFile image,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.updatePost(postId, postRequestDto, image, userDetailsImpl.getUser());
    }
    @DeleteMapping ("/{postId}")
    public ApiResponse<?> removePost(@PathVariable Long postId,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.deletePost(postId, userDetailsImpl.getUser());
    }


}