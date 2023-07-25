package team2.spgg.domain.api.dto.searchapiuser;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipantDto {

    private String summonerName;
    private String puuid;
    private Integer champLevel;
    private String championName;

    //KDA
    private Integer kills;
    private Integer deaths;
    private Integer assists;
    private Boolean isPerpect=false;
    //cs관련. 두개 더하면 총 cs
    private Integer totalMinionsKilled;
    private Integer neutralMinionsKilled;
    //각 멀티킬 횟수. 0개면 pass, 1개이상이면 가장 높은 거 출력.
    private Integer doubleKills;
    private Integer tripleKills;
    private Integer quadraKills;
    private Integer pentaKills;
    //핑와 갯수
    private Integer detectorWardsPlaced;
    //수행 포지션. TOP,JUNGLE etc..
    private String teamPosition;

    private Integer mainRune;
    private Integer subRune;
    //spell.
    private Integer summoner1Id;
    private String summonerSpell1;
    private Integer summoner2Id;
    private String summonerSpell2;
    private PerkDto perks;
    //item
    private Integer item0;
    private Integer item1;
    private Integer item2;
    private Integer item3;
    private Integer item4;
    private Integer item5;
    private Integer item6;
    //레드팀 or 블루팀. 100 or 200으로 표현
    private Integer teamId;
    private String team = "";
    //이겼는지 여부. true or false
    private Boolean win;


    public ParticipantDto(String summonerName, String championName, Integer teamId) {
        this.summonerName= summonerName;
        this.championName = championName;
        this.teamId=teamId;
        this.isPerpect=null;
    }
    public void updateRune(Integer mainRune, Integer subRune){
        this.mainRune= mainRune;
        this.subRune = subRune;
        this.perks = null;
    }

    public void updateSpell(String spellName1, String spellName2) {
        this.summonerSpell1 = spellName1;
        this.summonerSpell2 = spellName2;
        this.summoner1Id = null;
        this.summoner2Id = null;

    }
    public void updateTeamToString(){
        if(this.teamId==100){
            this.team = "Red";
        }else{
            this.team = "Blue";
        }
        this.teamId=null;
    }
    public void updateIsPerpect() {
        this.isPerpect=true;
    }
}
