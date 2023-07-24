package team2.spgg.domain.api.dto.searchapiuser;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class MatchDto {

    private MetadataDto metadata;
    private InfoDto info;
}