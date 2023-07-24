package team2.spgg.domain.api.dto.searchapiuser;

import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class StylesDto {
    private List<SelectionsDto> selections;
    private Integer style;
}
