package team2.spgg.domain.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team2.spgg.domain.post.dto.PostRequestDto;
import team2.spgg.domain.post.dto.PostSearchCondition;
import team2.spgg.domain.post.entity.Category;
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
    @GetMapping("/category/{categoryId}")
    public ApiResponse<?> getPostsByCategory(@PathVariable Long categoryId, Pageable pageable) {
        return postService.getPostsByCategory(categoryId, pageable);
    }

    @GetMapping("/category/notice")
    public ApiResponse<?> getPostsByNoticeCategory(Pageable pageable) {
        return postService.getPostsByCategoryName("공지게시판", pageable);
    }

    @GetMapping("/category/free")
    public ApiResponse<?> getPostsByFreeCategory(Pageable pageable) {
        return postService.getPostsByCategoryName("자유게시판", pageable);
    }

    @GetMapping("/{postId}")
    public ApiResponse<?> readOnePost(@PathVariable Long postId) {
        return postService.getSinglePost(postId);
    }


    @PostMapping
    public ApiResponse<?> createPost(@PathVariable Long categoryId,
                                     @RequestPart(value = "data") PostRequestDto postRequestDto,
                                     @RequestPart(value = "file", required = false) MultipartFile image,
                                     @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        return postService.createPost(postRequestDto, image, userDetailsImpl.getUser(), categoryId);
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