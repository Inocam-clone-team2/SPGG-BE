package com.example.spgg.domain.api.repository;

import com.example.spgg.domain.api.entity.MatchTeamModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchTeamRepository extends JpaRepository<MatchTeamModel, Integer>{
	
	List<MatchTeamModel> findAllByGameId(long gameId);
	
	void deleteAllByGameId(long gameId);
}
