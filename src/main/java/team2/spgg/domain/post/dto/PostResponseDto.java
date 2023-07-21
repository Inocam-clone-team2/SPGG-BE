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
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private List<CommentResponseDto> commentList;
    private String image;
    private long liked;
    private long disliked;

    @QueryProjection
    public PostResponseDto(Long id, String title, String username, String content, LocalDateTime createdAt, String image, long liked, long disliked) {
        this.id = id;
        this.title = title;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
        this.image = image;
        this.liked = liked;
        this.disliked = disliked;
    }

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.username = post.getUsername();
        this.content = post.getContent();
        this.commentList = post.getCommentList().stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        this.createdAt = post.getCreatedAt();
        this.image = post.getImage();
        this.liked = post.getLiked();
        this.disliked = post.getDisliked();
    }
}