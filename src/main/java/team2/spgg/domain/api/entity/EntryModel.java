package team2.spgg.domain.api.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntryModel {


	private int id;

	private String summonerName;

	private String tier;

	private String rank;

	private long leaguePoints;

	private int wins;

	private int losses;

	private double winRate;
}
