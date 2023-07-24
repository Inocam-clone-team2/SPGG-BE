package team2.spgg.domain.api.dto.searchapiuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MatchDto {

    private MetadataDto metadata;
    private InfoDto info;
}