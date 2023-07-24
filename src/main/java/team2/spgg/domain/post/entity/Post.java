package team2.spgg.domain.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.OnDelete;
import team2.spgg.domain.comment.entity.Comment;
import team2.spgg.domain.post.dto.PostRequestDto;
import team2.spgg.domain.user.entity.User;
import team2.spgg.global.utils.Timestamped;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;
import static jakarta.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.annotations.FetchMode.*;
import static org.hibernate.annotations.OnDeleteAction.*;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private String title;

    @Column(name = "nickname", nullable = false)
    private String nickname;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Fetch(SUBSELECT)
    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    private long liked;

    private String image;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = CASCADE)
    private User user;

    public Post(PostRequestDto postRequestDto, String image, User user) {
        this.category = postRequestDto.getCategory();
        this.title = postRequestDto.getTitle();
        this.nickname = user.getNickname();
        this.content = postRequestDto.getContent();
        this.image = image;
        this.liked = 0;
        this.user = user;
    }

    public void update(PostRequestDto postRequestDto) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
    }

    public void updateAll(PostRequestDto postRequestDto, String image) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.image = image;
    }

    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.initPost(this);
    }

    public void increaseLike() {
        this.liked += 1;
    }

    public void decreaseLike() {
        if (this.liked > 0) {
            this.liked -= 1;
        }
    }

}
