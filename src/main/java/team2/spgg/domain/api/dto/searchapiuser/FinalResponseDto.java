package team2.spgg.domain.api.dto.searchapiuser;

import lombok.*;
import team2.spgg.domain.user.entity.User;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FinalResponseDto {
    private SummonerDto summoner;
    private List<MatchDto> matchInfo;
    private UserAverageDto userAverageDto;

    public void updateInfoForResponse(List<MatchDto> infos){
        if(infos!=null){
            matchInfo.clear();
            matchInfo.addAll(infos);
        } else{
            matchInfo = null;
        }
    }
}
