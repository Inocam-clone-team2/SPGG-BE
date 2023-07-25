package team2.spgg.domain.api.dto.searchapiuser;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoDto {
    private Long gameId;
    private Long gameDuration;
    private Long minute;
    private Long second;
    private Long gameStartTimestamp;
    private Long gameEndTimestamp;
    private Long gameTimeAgo;
    private List<ParticipantDto> participants;
    private String platformId;

    public void updateParticipantsList(List<ParticipantDto> updateList){
        if (updateList != null) {
            participants.clear();
            participants.addAll(updateList);
        } else {
            // 덮어쓸 리스트가 null일 경우, participants 리스트를 비우는 대신 null로 설정해도 됩니다.
            participants = null;
        }
    }
    public void updateGameTime(long currentTime){

        this.minute = ((this.gameEndTimestamp-this.gameStartTimestamp)/1000)/60;
        this.second = ((this.gameEndTimestamp-this.gameStartTimestamp)/1000)%60;
        this.gameDuration=null;
        this.gameTimeAgo = (currentTime-gameEndTimestamp)/60000;
        this.gameStartTimestamp=null;
        this.gameEndTimestamp=null;


    }
}
