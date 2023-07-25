package team2.spgg.domain.api.dto.searchapiuser;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecentCountDto {
    private Float champOdds =0F;
    private Integer winCount;
    private Integer loseCount;
    private Integer killCount;
    private Integer deathCount;
    private Integer assistCount;
    private Float averKda = 0F;
    private Boolean isPerpect=false;

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

    public void averKda(){
        int gameCount = this.winCount+this.loseCount;
        if(deathCount!=0) {
            float kda = (float) (killCount + assistCount) / deathCount;
            this.averKda = kda/gameCount;
            this.killCount=null;
            this.deathCount=null;
            this.assistCount=null;
        } else {
            this.isPerpect=true;
            this.killCount=null;
            this.deathCount=null;
            this.assistCount=null;
        }
        if(this.winCount !=0 ) {
            this.champOdds = (this.winCount / (float)(this.winCount + this.loseCount)) * 100;
        } else {
            this.champOdds = 0F;
        }
    }
}