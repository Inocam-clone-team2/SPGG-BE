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
public class SummonerDto {

    private String id;
    private String accountId;
    private String puuid;
    private String name;
    private long profileIconId;
    private long summonerLevel;

    public void updateForResponse(){
        this.puuid=null;
        this.accountId=null;
        this.id=null;
    }
}
