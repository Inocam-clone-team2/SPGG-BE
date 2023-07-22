package team2.spgg.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import team2.spgg.domain.post.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    /**
     * 해당 ID의 게시물을 가져올 때, 해당 게시물의 댓글까지 상세히 가져옵니다.
     *
     * @param postId 가져올 게시물의 ID.
     * @return 해당 ID의 게시물과 관련된 댓글들을 포함한 Optional. 만약 해당 ID의 게시물이 존재하지 않을 경우 빈 Optional을 반환합니다.
     */
    @Query("select p from Post p left join fetch p.commentList cl where p.id = :postId")
    Optional<Post> findDetailPost(@Param("postId") Long postId);

    /**
     * 해당 ID의 게시물을 가져올 때, 해당 게시물의 좋아요 정보까지 상세히 가져옵니다.
     *
     * @param id 가져올 게시물의 ID.
     * @return 해당 ID의 게시물과 관련된 좋아요 정보를 포함한 Post 객체.
     */
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.liked WHERE p.id = :id")
    Post findByIdWithLikes(@Param("id") Long id);

    @Query(value = "SELECT * FROM post WHERE title LIKE %?1% OR content LIKE %?1% ORDER BY createDate DESC" , nativeQuery = true)
    List<Post> findByContent(String content);

    List<Post> findAllByOrderByIdDesc();

}

