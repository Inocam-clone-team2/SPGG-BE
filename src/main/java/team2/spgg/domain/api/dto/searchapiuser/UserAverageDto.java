package team2.spgg.domain.api.dto.searchapiuser;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAverageDto {

    private Integer userTotalGame=0;
    private Integer userTotalWin=0;
    private Integer userTotalLose=0;
    private Float userTotalOdds=0F;
    private Integer userTotalKill=0;
    private Integer userTotalDeath=0;
    private Integer userTotalAssist=0;
    private Float userAverKill=0F;
    private Float userAverDeath=0F;
    private Float userAverAssist=0F;
    private Float userAverKDA=0F;
    private Boolean isPerpect= false;

    private Map<String, RecentCountDto> playChampionList = new HashMap<>();

    private Map<String, PositionCountDto> positionList= new HashMap<>();
    public void addUserTotalKda(Boolean win, Integer userKill, Integer userDeath, Integer userAssist) {
        if(win) {
            this.userTotalWin++;
        } else {
            this.userTotalLose++;
        }
        this.userTotalKill += userKill;
        this.userTotalDeath += userDeath;
        this.userTotalAssist += userAssist;
    }
    public void updateAverKda(UserAverageDto userAverageDto, Integer gameCount){
        this.userAverKill= (float)userAverageDto.getUserTotalKill()/gameCount;
        this.userAverDeath = (float)userAverageDto.getUserTotalDeath()/gameCount;
        this.userAverAssist = (float)userAverageDto.getUserTotalAssist()/gameCount;
        this.userAverKDA = (this.userAverKill+this.userAverAssist)/this.userAverDeath;
        this.userTotalKill=null;
        this.userTotalDeath=null;
        this.userTotalAssist=null;
        this.userTotalGame = this.userTotalWin + this.userTotalLose;
        this.userTotalOdds = ((float)this.userTotalWin/(this.userTotalWin+this.userTotalLose))*100;
    }

    public void updateIsPerpect() {
        this.isPerpect=true;
    }
}