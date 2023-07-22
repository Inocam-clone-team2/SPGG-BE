package team2.spgg.domain.api.controller;

import team2.spgg.domain.api.dto.InfoDto;
import team2.spgg.domain.api.dto.RespDto;
import team2.spgg.domain.api.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class ApiController {

	private final ApiService apiService;

	// rankingDto 가져오기 (이름으로 검색)
	@GetMapping("ranking/name/{name}")
	public ResponseEntity<RespDto<?>> getRankByName(@PathVariable String name) {
		RespDto<?> resp = apiService.getRank(name);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	// rankingDto 가져오기 (페이지별)
	@GetMapping("ranking/page/{page}")
	public ResponseEntity<RespDto<?>> getRankByPage(@PathVariable long page) {
		RespDto<?> resp = apiService.getRank(page);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	// detailDto 가져오기 (게임 ID로 검색)
	@GetMapping("detail/gameid/{gameId}")
	public ResponseEntity<RespDto<?>> getDetailByGameId(@PathVariable long gameId) {
		RespDto<?> resp = apiService.getDetail(gameId);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	// infoDto 가져오기 (이름으로 검색)
	@GetMapping("info/name/{name}")
	public ResponseEntity<RespDto<?>> getInfoByName(@PathVariable String name) {
		RespDto<?> resp = apiService.getInfo(name);
		return new ResponseEntity<>(resp, HttpStatus.OK);
	}

	// 전적 갱신하기 (이름으로 검색)
	@PostMapping("info/update/name/{name}")
	public ResponseEntity<RespDto<?>> updateInfoByName(@PathVariable String name) {
		boolean isGetData = apiService.getApiData(name);
		if (isGetData) {
			RespDto<?> resp = apiService.getInfo(name);
			return new ResponseEntity<>(resp, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new RespDto<List<InfoDto>>(HttpStatus.BAD_REQUEST.value(), "전적갱신에 실패하였습니다.", null), HttpStatus.BAD_REQUEST);
		}
	}
}
