package team2.spgg.domain.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team2.spgg.domain.comment.dto.CommentRequestDto;
import team2.spgg.domain.post.entity.Post;
import team2.spgg.domain.user.entity.User;
import team2.spgg.global.utils.Timestamped;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;


@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;


    public Comment(CommentRequestDto commentRequestDto, User user) {
        this.content = commentRequestDto.getContent();
        this.nickname = user.getNickname();
        this.user = user;
    }

    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

    public void initPost(Post post) {
        this.post = post;
    }
}