package team2.spgg.domain.post.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.spgg.domain.post.entity.Category;
import team2.spgg.domain.post.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @Query("select p from Post p left join fetch p.commentList cl where p.id = :postId")
    Optional<Post> findDetailPost(@Param("postId") Long postId);

    @Query("SELECT p FROM Post p WHERE p.category = :category")
    Slice<Post> findByCategory(Category category, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.liked WHERE p.id = :id")
    Post findByIdWithLikes(@Param("id") Long id);

    @Query(value = "SELECT * FROM post WHERE title LIKE %?1% OR content LIKE %?1% ORDER BY createDate DESC" , nativeQuery = true)
    List<Post> findByContent(String content);

}

