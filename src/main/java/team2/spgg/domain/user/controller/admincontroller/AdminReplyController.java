package team2.spgg.domain.user.controller.admincontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import team2.spgg.domain.comment.entity.Comment;
import team2.spgg.domain.comment.repository.CommentRepository;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/reply")
public class AdminReplyController {

	@Autowired
	private CommentRepository commentRepository;
	
//	//댓글 검색기능
//	@PostMapping("/search")
//	public @ResponseBody List<Comment> search(@RequestBody Map<String, String> data) {
//
//		return commentRepository.(data.get("comment"));
//	}
//
//	// 댓글 모두 가져오기
//	@GetMapping({"","/"})
//	public String reply(Model model) {
//
//		model.addAttribute("replies", commentRepository.findAllByOrderByIdDesc());
//
//		return "adminReply";
//	}
//
//	// 댓글 지우기
//	@DeleteMapping("delete/{id}")
//	public @ResponseBody String deleteReply(@PathVariable int id ) {
//
//		commentRepository.deleteById(id);
//
//		return "성공";
//	}
}
