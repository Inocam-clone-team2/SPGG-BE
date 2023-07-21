package team2.spgg.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team2.spgg.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
