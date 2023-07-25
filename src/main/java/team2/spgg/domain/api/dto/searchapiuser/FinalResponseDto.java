package team2.spgg.domain.api.dto.searchapiuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FinalResponseDto {
    private SummonerDto summoner;
    private UserAverageDto userAverage;
    private List<MatchDto> matchInfo;


    public void updateInfoForResponse(List<MatchDto> infos){
        if(infos!=null){
            matchInfo.clear();
            matchInfo.addAll(infos);
        } else{
            matchInfo = null;
        }
    }
}