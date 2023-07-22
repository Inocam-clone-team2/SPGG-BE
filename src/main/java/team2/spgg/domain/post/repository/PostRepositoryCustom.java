package team2.spgg.domain.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import team2.spgg.domain.post.dto.PostResponseDto;
import team2.spgg.domain.post.dto.PostSearchCondition;

public interface PostRepositoryCustom {

    Slice<PostResponseDto> serachPostBySlice(PostSearchCondition condition, Pageable pageable);
}
