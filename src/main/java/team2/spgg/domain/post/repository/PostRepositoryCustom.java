package team2.spgg.domain.post.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team2.spgg.domain.post.dto.PostResponseDto;
import team2.spgg.domain.post.dto.PostSearchCondition;
import team2.spgg.domain.post.entity.Post;

public interface PostRepositoryCustom {

    Page<PostResponseDto> serachPostByPage(PostSearchCondition condition, Pageable pageable);

    Page<PostResponseDto> searchPostByPageByPopularity(PostSearchCondition condition, Pageable pageable);

    Page<PostResponseDto> searchPostByPageByMostView(PostSearchCondition condition, Pageable pageable);

}
