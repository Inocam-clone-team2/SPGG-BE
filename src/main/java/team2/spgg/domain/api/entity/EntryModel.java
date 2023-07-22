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
@Table(name = "entrymodel")
public class EntryModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
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
	private int wins;

	@Column
	private int losses;

	@Column
	private double winRate;
}
