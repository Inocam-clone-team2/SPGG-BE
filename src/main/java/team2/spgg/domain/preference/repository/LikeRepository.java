package team2.spgg.domain.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team2.spgg.domain.post.entity.Post;
import team2.spgg.domain.preference.entity.Like;
import team2.spgg.domain.user.entity.User;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Optional<Like> findByPostAndUser(Post post, User user);
}
