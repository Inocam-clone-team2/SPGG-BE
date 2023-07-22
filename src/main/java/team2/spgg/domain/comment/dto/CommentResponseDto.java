package team2.spgg.domain.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import team2.spgg.domain.comment.entity.Comment;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CommentResponseDto {

    private Long id;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;

    /**
     * 생성자를 통해 Comment 엔티티로부터 CommentResponseDto를 생성합니다.
     *
     * @param comment Comment 엔티티
     */
    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.nickname = comment.getUser().getNickname();
        this.createdAt = comment.getCreatedAt();
    }
}
