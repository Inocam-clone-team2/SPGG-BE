package team2.spgg.domain.preference.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.spgg.domain.post.dto.PostResponseDto;
import team2.spgg.domain.post.entity.Post;
import team2.spgg.domain.post.repository.PostRepository;
import team2.spgg.domain.preference.entity.Like;
import team2.spgg.domain.preference.repository.LikeRepository;
import team2.spgg.domain.user.entity.User;
import team2.spgg.global.exception.InvalidConditionException;

import static team2.spgg.global.stringCode.ErrorCodeEnum.POST_NOT_EXIST;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public PostResponseDto updateLike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() ->
                new InvalidConditionException(POST_NOT_EXIST));

        String nickname = user.getNickname();
        String postTitle = post.getTitle();   // 게시물 제목 가져오기

        if (!isLikedPost(post, user)) {
            createLike(post, user);
            post.increaseLike();
            log.info("'{}'님이 '{}'에 좋아요를 추가했습니다.", nickname, postTitle);
        } else {
            removeLike(post, user);
            post.decreaseLike();
            log.info("'{}'님이 '{}'의 좋아요를 취소했습니다.", nickname, postTitle);
        }

        return new PostResponseDto(post);
    }



    private boolean isLikedPost(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user).isPresent();
    }

    private void createLike(Post post, User user) {
        Like like = new Like(post, user);
        likeRepository.save(like);
    }

    private void removeLike(Post post, User user) {
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow();
        likeRepository.delete(like);
    }


}
