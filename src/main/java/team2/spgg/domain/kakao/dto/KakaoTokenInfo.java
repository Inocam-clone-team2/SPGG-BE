package team2.spgg.domain.kakao.dto;

import lombok.Data;

@Data
public class KakaoTokenInfo {
    Long id;
    Integer expires_in;
    Integer app_id;
}
