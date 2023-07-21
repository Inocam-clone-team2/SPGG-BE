package team2.spgg.domain.user.service;

import jakarta.persistence.Id;
import team2.spgg.domain.user.entity.User;
import team2.spgg.domain.post.repository.PostRepository;
import team2.spgg.domain.comment.repository.CommentRepository;
import team2.spgg.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class AdminUserService {
//
//	@Autowired
//	private UserRepository userRepository;
//
//	@Autowired
//	private PostRepository postRepository;
//
//	@Autowired
//	private CommentRepository commentRepository;
//
//	@Transactional
//	public void updateRole(Map<String, String> roles, int id) {
//		User userEntity = userRepository.findById(id).get();
//		userEntity.setRoles(roles.get("role"));
//	}
//
//	@Transactional
//	public void deleteUser(int id) {
//
//		commentRepository.deleteByUserId(id);
//		postRepository.deleteByUserId(id);
//		userRepository.deleteById(id);
//
//	}

}
