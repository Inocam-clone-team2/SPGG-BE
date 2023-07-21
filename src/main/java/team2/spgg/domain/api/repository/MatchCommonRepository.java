package team2.spgg.domain.api.repository;

import team2.spgg.domain.api.entity.MatchCommonModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchCommonRepository extends JpaRepository<MatchCommonModel, Integer> {
    MatchCommonModel findByGameId(long gameId);

}
