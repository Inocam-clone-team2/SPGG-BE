package team2.spgg.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import team2.spgg.domain.comment.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Comment findById(int id);

    List<Comment> findAllByOrderByIdDesc();

    void deleteByUserId(int id);

    //게시글 검색기능
    @Query(value =" select * from reply where reply like %?1%", nativeQuery = true)
    List<Comment> findByReply(String reply);
}
