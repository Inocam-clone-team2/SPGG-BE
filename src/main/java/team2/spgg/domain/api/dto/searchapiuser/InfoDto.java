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
public class InfoDto {
    private Long gameId;
    private Long gameCreation;
    private Long gameDuration;
    private Long gameStartTimestamp;
    private Long gameEndTimestamp;
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
}
