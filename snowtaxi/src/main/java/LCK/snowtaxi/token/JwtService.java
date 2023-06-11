package LCK.snowtaxi.token;

import LCK.snowtaxi.repository.UserRepository;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String USERID_CLAIM = "userId";
    private static final String KAKAOID_CLAIM = "kakaoId";
    private static final String NICKNAME_CLAIM = "nickname";
    private static final String KAKAO_TOKEN_CLAIM = "kakaoAccessToken";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;

    public String createAccessToken(String kakaoId, long userId, String nickname, String kakao_token) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(KAKAOID_CLAIM, kakaoId)
                .withClaim(USERID_CLAIM, userId)
                .withClaim(NICKNAME_CLAIM, nickname)
                .withClaim(KAKAO_TOKEN_CLAIM, kakao_token)
                .sign(Algorithm.HMAC512(secretKey)); // HMAC512 알고리즘 사용, application-jwt.yml에서 지정한 secret 키로 암호화
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        Optional<String> access_token =
                Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
        System.out.println(access_token);
        return access_token;
    }


    public TokenInfoVo getTokenInfo(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(token);

        Long userId = decodedJWT.getClaim(USERID_CLAIM).asLong();
        String kakaoId = decodedJWT.getClaim(KAKAOID_CLAIM).asString();
        String nickname = decodedJWT.getClaim(NICKNAME_CLAIM).asString();
        String kakao_token = decodedJWT.getClaim(KAKAO_TOKEN_CLAIM).asString();

        return new TokenInfoVo(userId, kakaoId, nickname, kakao_token);
    }

    public Optional<String> extractInfo(String accessToken) {
        try {
            log.info("액세스 토큰이 유효해서 정보를 가져옵니다.");
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken) // accessToken을 검증하고 유효하지 않다면 예외 발생
                    .getClaim(KAKAOID_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    public void updateRefreshToken(long userId, String refreshToken) {
        userRepository.findByUserId(userId)
                .ifPresentOrElse(
                        user -> {
                            user.setRefreshToken(refreshToken);
                            userRepository.saveAndFlush(user);
                        },
                        () -> new Exception("일치하는 회원이 없습니다.")
                );

    }
    public boolean isTokenValid(String token) {
        try {
            // 토큰 유효성 검사하는 데에 사용할 알고리즘이 있는 JWT verifier 빌드하고 verify(token)
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            log.info("유효한 토큰입니다");
            return true;
        } catch (Exception e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }

}
