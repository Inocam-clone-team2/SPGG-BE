package team2.spgg.domain.api.dto.searchapiuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class UserAverageDto {
    private Map<String, RecentCountDto> resentChampionList = new HashMap<>();


}
