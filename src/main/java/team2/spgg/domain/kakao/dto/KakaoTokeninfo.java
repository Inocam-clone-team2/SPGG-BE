package team2.spgg.domain.kakao.dto;

import lombok.Data;

@Data
public class KakaoTokeninfo {
    Long id;
    Integer expires_in;
    Integer app_id;
}
