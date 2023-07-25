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
public class PositionCountDto {
    private Integer count;
    private Float Odds=0F;
    public void addPositionCount(){
        if(count!=null) {
            this.count = this.count+1;
        };
    }
    public void updatePositionOdds(Integer count){
        this.Odds= ((float)this.count/count)*100;
        this.count=null;
    }
}