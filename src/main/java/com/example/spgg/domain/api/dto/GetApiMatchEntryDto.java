package com.example.spgg.domain.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetApiMatchEntryDto {
	private String summonerName;
	private String accountId;
	private String puuid;
}
