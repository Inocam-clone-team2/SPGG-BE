package team2.spgg.domain.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostSearchCondition {

    private String username;
    private String title;
}
