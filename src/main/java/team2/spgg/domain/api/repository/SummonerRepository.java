package team2.spgg.domain.api.repository;

import team2.spgg.domain.api.entity.SummonerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SummonerRepository extends JpaRepository<SummonerModel, Integer>{
	
	@Query(value = "SELECT * FROM summonermodel WHERE LOWER(REPLACE(name,' ', '')) LIKE ?1", nativeQuery = true)
	SummonerModel findByName(String name);
	
	SummonerModel findByAccountId(String accountId);
	
	SummonerModel findBySummonerId(String summonerId);
}