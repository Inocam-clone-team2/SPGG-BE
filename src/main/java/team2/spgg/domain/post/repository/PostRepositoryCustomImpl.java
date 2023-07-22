package team2.spgg.domain.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import team2.spgg.domain.post.dto.PostResponseDto;
import team2.spgg.domain.post.dto.PostSearchCondition;
import team2.spgg.domain.post.dto.QPostResponseDto;
import team2.spgg.domain.post.dto.QPostResponseDto;
import team2.spgg.domain.post.entity.QPost;

import java.util.List;

import static org.springframework.util.StringUtils.hasText;
import static team2.spgg.domain.post.entity.QPost.post;


@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom{

    private final JPAQueryFactory query;

    /**
     * 조건에 맞는 게시물을 페이징 처리하여 검색하는 메소드입니다.
     *
     * @param condition 검색 조건
     * @param pageable  페이지 정보
     * @return 페이징된 게시물 목록
     */
    @Override
    public Slice<PostResponseDto> serachPostBySlice(PostSearchCondition condition, Pageable pageable) {
        List<PostResponseDto> result = query
                .select(new QPostResponseDto(
                        QPost.post.id,
                        QPost.post.title,
                        post.user.nickname,
                        QPost.post.content,
                        QPost.post.createdAt,
                        QPost.post.image,
                        QPost.post.liked
                ))
                .from(QPost.post)
                .where(
                        usernameEq(condition.getNickname()),
                        titleEq(condition.getTitle()))
                .orderBy(QPost.post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkEndPage(pageable, result);
    }

    /**
     * 사용자명에 대한 조건을 생성하는 메소드입니다.
     *
     * @param usernameCond 사용자명 조건
     * @return 사용자명 조건에 해당하는 BooleanExpression 객체
     */
    private BooleanExpression usernameEq(String usernameCond) {
        return hasText(usernameCond) ? post.user.nickname.eq(usernameCond) : null;
    }

    /**
     * 제목에 대한 조건을 생성하는 메소드입니다.
     *
     * @param titleCond 제목 조건
     * @return 제목 조건에 해당하는 BooleanExpression 객체
     */
    private BooleanExpression titleEq(String titleCond) {
        return hasText(titleCond) ? QPost.post.title.eq(titleCond) : null;
    }

    /**
     * 페이징된 게시물 목록을 반환하고, 다음 페이지 여부를 확인하는 메소드입니다.
     *
     * @param pageable 페이징 정보
     * @param content  조회된 게시물 목록
     * @return 페이징된 게시물 목록
     */
    private static SliceImpl<PostResponseDto> checkEndPage(Pageable pageable, List<PostResponseDto> content) {
        boolean hasNext = false;
        if (content.size() > pageable.getPageSize()) {
            hasNext = true;
            content.remove(pageable.getPageSize());
        }
        return new SliceImpl<>(content, pageable, hasNext);
    }
}
