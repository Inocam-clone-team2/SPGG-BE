package team2.spgg.domain.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rankingmodel")
public class RankingModel {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private int id;

	private String summonerId;

    private String summonerName;

    private String tier;

    private String rank;

    private long leaguePoints;

    private int win;

    private int lose;

}