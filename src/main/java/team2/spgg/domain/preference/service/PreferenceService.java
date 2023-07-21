package team2.spgg.domain.preference.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team2.spgg.domain.post.entity.Post;
import team2.spgg.domain.post.repository.PostRepository;
import team2.spgg.domain.preference.entity.Dislike;
import team2.spgg.domain.preference.entity.Like;
import team2.spgg.domain.preference.repository.DislikeRepository;
import team2.spgg.domain.preference.repository.LikeRepository;
import team2.spgg.domain.user.entity.User;
import team2.spgg.global.responseDto.ApiResponse;

import static team2.spgg.global.stringCode.SuccessCodeEnum.DISLIKE_CANCEL_SUCCESS;
import static team2.spgg.global.stringCode.SuccessCodeEnum.DISLIKE_SUCCESS;
import static team2.spgg.global.stringCode.SuccessCodeEnum.LIKE_CANCEL_SUCCESS;
import static team2.spgg.global.stringCode.SuccessCodeEnum.LIKE_SUCCESS;
import static team2.spgg.global.utils.ResponseUtils.okWithMessage;


@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceService {

    private final LikeRepository likeRepository;
    private final DislikeRepository dislikeRepository;
    private final PostRepository postRepository;

    /**
     * 게시물에 대한 좋아요를 추가 또는 제거하는 메소드입니다.
     *
     * @param postId    게시물 ID
     * @param user      사용자 정보
     * @return 성공 또는 실패 여부를 반환하는 ApiResponse
     */
    public ApiResponse<?> updateLike(Long postId, User user) {
        Post post = postRepository.findByIdWithLikes(postId);

        if (!isLikedPost(post, user)) {
            createLike(post, user);
            post.increaseLike();
            postRepository.save(post);
            return okWithMessage(LIKE_SUCCESS);
        }
        removeLike(post, user);
        post.decreaseLike();
        postRepository.save(post);
        return okWithMessage(LIKE_CANCEL_SUCCESS);
    }

    /**
     * 게시물에 대한 싫어요를 추가 또는 제거하는 메소드입니다.
     *
     * @param postId    게시물 ID
     * @param user      사용자 정보
     * @return 성공 또는 실패 여부를 반환하는 ApiResponse
     */
    public ApiResponse<?> updateDislike(Long postId, User user) {
        Post post = postRepository.findByIdWithDislikes(postId);

        if (!isDislikedPost(post, user)) {
            createDislike(post, user);
            post.increaseDislike();
            postRepository.save(post);
            return okWithMessage(DISLIKE_SUCCESS);
        }
        removeDislike(post, user);
        post.decreaseDislike();
        postRepository.save(post);
        return okWithMessage(DISLIKE_CANCEL_SUCCESS);
    }

    /**
     * 해당 게시물에 사용자가 좋아요를 눌렀는지 확인합니다.
     *
     * @param post      게시물
     * @param user      사용자 정보
     * @return 사용자가 좋아요를 눌렀는지 여부
     */
    private boolean isLikedPost(Post post, User user) {
        return likeRepository.findByPostAndUser(post, user).isPresent();
    }

    /**
     * 해당 게시물에 사용자가 싫어요를 눌렀는지 확인합니다.
     *
     * @param post      게시물
     * @param user      사용자 정보
     * @return 사용자가 싫어요를 눌렀는지 여부
     */
    private boolean isDislikedPost(Post post, User user) {
        return dislikeRepository.findByPostAndUser(post, user).isPresent();
    }

    /**
     * 게시물에 좋아요를 생성합니다.
     *
     * @param post      게시물
     * @param user      사용자 정보
     */
    private void createLike(Post post, User user) {
        Like like = new Like(post, user);
        likeRepository.save(like);
    }

    /**
     * 게시물에 싫어요를 생성합니다.
     *
     * @param post      게시물
     * @param user      사용자 정보
     */
    private void createDislike(Post post, User user) {
        Dislike dislike = new Dislike(post, user);
        dislikeRepository.save(dislike);
    }

    /**
     * 게시물의 좋아요를 제거합니다.
     *
     * @param post      게시물
     * @param user      사용자 정보
     */
    private void removeLike(Post post, User user) {
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow();
        likeRepository.delete(like);
    }

    /**
     * 게시물의 싫어요를 제거합니다.
     *
     * @param post      게시물
     * @param user      사용자 정보
     */
    private void removeDislike(Post post, User user) {
        Dislike dislike = dislikeRepository.findByPostAndUser(post, user).orElseThrow();
        dislikeRepository.delete(dislike);
    }
}
