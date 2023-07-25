package team2.spgg.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team2.spgg.domain.post.entity.Category;

@Getter
@NoArgsConstructor
public class PostRequestDto {
    private Category category;
    private String title;
    private String content;
}
