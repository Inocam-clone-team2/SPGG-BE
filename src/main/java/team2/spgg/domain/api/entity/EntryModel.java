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
public class EntryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "leagueId")
	private String leagueId;

	@Column(name = "queueType")
	private String queueType;

	@Column(name = "tier")
	private String tier;

	@Column(name = "rankName") // 변경된 필드명: rank -> rankName
	private String rankName;

	@Column(name = "summonerId")
	private String summonerId;

	@Column(name = "summonerName")
	private String summonerName;

	@Column(name = "leaguePoints")
	private long leaguePoints;

	@Column(name = "wins")
	private long wins;

	@Column(name = "losses")
	private long losses;

	@Column(name = "tierRankId")
	private String tierRankId;
}
