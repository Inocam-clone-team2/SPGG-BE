package com.example.spgg.domain.api.repository;

import com.example.spgg.domain.api.entity.MatchSummonerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MatchSummonerRepository extends JpaRepository<MatchSummonerModel, Integer>{
	
	List<MatchSummonerModel> findAllByGameIdOrderByParticipantIdAsc(long gameId);
	
	List<MatchSummonerModel> findAllByGameIdOrderByParticipantIdDesc(long gameId);
	
	List<MatchSummonerModel> findAllByAccountIdOrderByGameCreationDesc(String accountId);
	
	@Query(value = "SELECT id, gameId, queueId, gameCreation, gameDuration, summonerName, accountId, participantId, teamId, championId, spell1Id, spell2Id, win, item0, item1, item2, item3, item4, item5, item6, kills, deaths, assists, totalDamageDealtToChampions, goldEarned, totalMinionsKilled, champLevel, sightWardsBoughtInGame, wardsPlaced, wardsKilled, perkPrimaryStyle, perkSubStyle FROM matchsummonermodel WHERE LOWER(replace(summonerName,' ', '')) LIKE ?1 ORDER BY gameCreation Desc", nativeQuery = true)
	List<MatchSummonerModel> findAllBySummonerNameOrderByGameCreationDesc(String summonerName);
	
	List<MatchSummonerModel> findAllByGameId(long gameId);
	
	void deleteAllByGameId(long gameId);
	
	
}
