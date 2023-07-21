package team2.spgg.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import team2.spgg.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    User findByEmail(String email);

    @Query(value = "select UserName from user", nativeQuery = true)
    User findUserName();

    @Query(value =" select * from user where email like %?1%", nativeQuery = true)
    List<User> mFindByEmail(String email);
}
