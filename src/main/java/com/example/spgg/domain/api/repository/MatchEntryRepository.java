package com.example.spgg.domain.api.repository;

import com.example.spgg.domain.api.entity.MatchEntryModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchEntryRepository extends JpaRepository<MatchEntryModel, Integer>{
	
	MatchEntryModel findByAccountId(String accountId);
	
	MatchEntryModel findByGameId(long gameId);

}
