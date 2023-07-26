package team2.spgg.domain.api.dto.searchapiuser;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

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
    private String gameEndTime;
    private Long gameTimeAgo;
    private ParticipantDto searchUserInfo;
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC")); // 시간대를 UTC로 설정
        this.gameEndTime = sdf.format(gameEndTimestamp);
        this.gameEndTimestamp=null;
        this.gameStartTimestamp=null;

    }
    public void updateSearchUserInfo(ParticipantDto participantDto){
        this.searchUserInfo = participantDto;
    }
}
