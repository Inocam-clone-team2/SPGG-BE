package team2.spgg.domain.user.controller.admincontroller;

import team2.spgg.domain.post.entity.Post;
import team2.spgg.domain.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/post")
public class AdminPostController {

//	@Autowired
//	private PostRepository postRepository;
//
//	//유저 검색기능
//	@PostMapping("/search")
//	public @ResponseBody List<Post> search(@RequestBody Map<String, String> data) {
//
//		return postRepository.findByContent(data.get("title"));
//
//	}
//
//    //게시글 메인
//	@GetMapping({"","/"})
//	public String post(Model model) {
//
//		model.addAttribute("posts", postRepository.findAllByOrderByIdDesc());
//
//		return "adminPost";
//	}
	
	//게시글 삭제
//	@DeleteMapping("/delete/{id}")
//	public @ResponseBody String deletePost(@PathVariable int id ) {
//
//		postRepository.deleteById(id);
//
//		return "성공";
//	}

}
