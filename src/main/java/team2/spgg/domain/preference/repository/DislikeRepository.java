package team2.spgg.domain.preference.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team2.spgg.domain.post.entity.Post;
import team2.spgg.domain.preference.entity.Dislike;
import team2.spgg.domain.user.entity.User;

import java.util.Optional;

public interface DislikeRepository extends JpaRepository<Dislike, Long> {

    Optional<Dislike> findByPostAndUser(Post post, User user);
}
