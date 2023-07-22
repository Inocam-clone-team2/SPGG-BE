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
    @Column
	private int id;
    @Column
    private String summonerName;
    @Column
    private String tier;
    @Column(name = "rankName")
    private String rank;
    @Column
    private long leaguePoints;
    @Column
    private int win;
    @Column
    private int lose;

}