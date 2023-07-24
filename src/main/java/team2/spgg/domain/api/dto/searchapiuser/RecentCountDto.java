package team2.spgg.domain.api.dto.searchapiuser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class RecentCountDto {
    private Integer winCount;
    private Integer loseCount;
    private Integer killCount;
    private Integer deathCount;
    private Integer assistCount;

    public void addCount(ParticipantDto participantDto){
        if(participantDto.getWin()){
            this.winCount++;
        } else{
            this.loseCount++;
        }
        this.killCount+= participantDto.getKills();
        this.deathCount+=participantDto.getDeaths();
        this.assistCount+=participantDto.getAssists();
    }
}
