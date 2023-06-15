package LCK.snowtaxi.token;

import lombok.Getter;

@Getter
public class TokenInfoVo {
    private Long userId;
    private String kakaoId;
    private String nickname;
    private String kakao_token;

    TokenInfoVo(Long userId, String kakaoId, String nickname, String kakao_token){
        this.userId = userId;
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.kakao_token = kakao_token;
    }
}
