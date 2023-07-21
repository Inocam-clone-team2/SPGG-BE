package com.example.spgg.domain.api.controller;

import com.example.spgg.domain.api.dto.InfoDto;
import com.example.spgg.domain.api.dto.RespDto;
import com.example.spgg.domain.api.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class ApiController {

	private final ApiService apiService;

	// rankingDto 가져오기 아이디 검색
	@GetMapping("ranking/name/{name}")
	public RespDto<?> getRankByName(@PathVariable String name) {

		return apiService.getRank(name);
	}

	// rankingDto 가져오기
	@GetMapping("ranking/page/{page}")
	public RespDto<?> getRankByPage(@PathVariable long page) {

		return apiService.getRank(page);
	}

	// detailDto 가져오기
	@GetMapping("detail/gameid/{gameId}")
	public RespDto<?> getDetailByGameId(@PathVariable long gameId) {

		return apiService.getDetail(gameId);
	}

	// infoDto 가져오기
	@GetMapping("info/name/{name}")
	public RespDto<?> getInfoByName(@PathVariable String name) {
		System.out.println("날아옴```````````````````````````````````````````````````````````");
		System.out.println(name);
		RespDto resp = apiService.getInfo(name);
		System.out.println("resp : " + resp);
		return resp;
	}

	// 전적갱신하기
	@PostMapping("info/update/name/{name}")
	public RespDto<?> updateInfoByName(@PathVariable String name) {

		System.out.println(name);

		boolean isGetData = apiService.getApiData(name);

		if (isGetData) {
			return apiService.getInfo(name);
		} else {
			return new RespDto<List<InfoDto>>(HttpStatus.BAD_REQUEST.value(), "전적갱신에 실패하였습니다.", null);
		}

	}
}
