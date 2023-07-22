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


    /**
     * Comment 엔티티의 생성자입니다.
     *
     * @param commentRequestDto Comment 생성에 필요한 데이터가 담긴 DTO
     * @param user              Comment 작성자(User 엔티티)
     */
    public Comment(CommentRequestDto commentRequestDto, User user) {
        this.content = commentRequestDto.getContent();
        this.nickname = user.getNickname();
        this.user = user;
    }

    /**
     * Comment 엔티티의 내용을 업데이트합니다.
     *
     * @param commentRequestDto 업데이트할 Comment 정보가 담긴 DTO
     */
    public void update(CommentRequestDto commentRequestDto) {
        this.content = commentRequestDto.getContent();
    }

    /**
     * Comment 엔티티와 연결된 Post 엔티티를 설정합니다.
     *
     * @param post Comment와 연결할 Post 엔티티
     */
    public void setPost(Post post) {
        this.post = post;
    }
}