package team2.spgg.domain.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team2.spgg.domain.comment.dto.CommentResponseDto;
import team2.spgg.domain.post.entity.Post;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
public class PostResponseDto {

    private Long id;
    private String title;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> commentList;
    private String image;
    private long liked;

    @QueryProjection
    public PostResponseDto(Long id, String title, String nickname, String content, LocalDateTime createdAt, String image, long liked) {
        this.id = id;
        this.title = title;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.image = image;
        this.liked = liked;
    }

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.nickname = post.getUser().getNickname();
        this.content = post.getContent();
        this.commentList = post.getCommentList().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        this.createdAt = post.getCreatedAt();
        this.image = post.getImage();
        this.liked = post.getLiked();
    }
}