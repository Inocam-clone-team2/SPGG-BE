package team2.spgg.domain.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import team2.spgg.domain.comment.entity.Comment;
import team2.spgg.domain.post.dto.PostRequestDto;
import team2.spgg.domain.user.entity.User;
import team2.spgg.global.utils.Timestamped;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.OnDeleteAction.CASCADE;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> commentList = new ArrayList<>();

    private long liked;

    private long disliked;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = CASCADE)
    private User user;

    /**
     * 이미지 URL을 설정합니다.
     *
     * @param image 이미지 URL
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * PostRequestDto와 User를 기반으로 Post를 생성하는 생성자입니다.
     *
     * @param postRequestDto 게시물 요청 DTO
     * @param image          이미지 URL
     * @param user           작성자 User
     */
    public Post(PostRequestDto postRequestDto, String image, User user) {
        this.title = postRequestDto.getTitle();
        this.username = user.getUsername();
        this.content = postRequestDto.getContent();
        this.image = image;
        this.liked = 0;
        this.disliked = 0;
        this.user = user;
    }

    /**
     * PostRequestDto를 기반으로 Post를 업데이트합니다.
     *
     * @param postRequestDto 업데이트할 게시물 요청 DTO
     */
    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    /**
     * Comment를 추가하고 연관 관계를 설정합니다.
     *
     * @param comment 추가할 Comment
     */
    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.setPost(this);
    }

    /**
     * 좋아요 수를 증가시킵니다.
     */
    public void increaseLike() {
        this.liked += 1;
    }

    /**
     * 좋아요 수를 감소시킵니다.
     */
    public void decreaseLike() {
        this.liked -= 1;
    }

    /**
     * 싫어요 수를 증가시킵니다.
     */
    public void increaseDislike() {
        this.disliked += 1;
    }

    /**
     * 싫어요 수를 감소시킵니다.
     */
    public void decreaseDislike() {
        this.disliked -= 1;
    }
}
