package team2.spgg.domain.preference.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.spgg.domain.post.entity.Post;
import team2.spgg.domain.post.repository.PostRepository;
import team2.spgg.domain.preference.entity.Like;
import team2.spgg.domain.preference.repository.LikeRepository;
import team2.spgg.domain.user.entity.User;
import team2.spgg.global.exception.InvalidConditionException;
import team2.spgg.global.responseDto.ApiResponse;

import static team2.spgg.global.stringCode.ErrorCodeEnum.POST_NOT_EXIST;
import static team2.spgg.global.stringCode.SuccessCodeEnum.*;
import static team2.spgg.global.utils.ResponseUtils.*;


@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public ApiResponse<?> updateLike(Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(()->
                new InvalidConditionException(POST_NOT_EXIST));;

        if (!isLikedPost(post, user)) {
            createLike(post, user);
            post.increaseLike();
            return okWithMessage(LIKE_SUCCESS);
        }

        removeLike(post, user);
        post.decreaseLike();
        return okWithMessage(LIKE_CANCEL_SUCCESS);
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
