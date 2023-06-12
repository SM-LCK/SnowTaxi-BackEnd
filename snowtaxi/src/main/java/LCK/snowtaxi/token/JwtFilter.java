package LCK.snowtaxi.token;

import LCK.snowtaxi.domain.User;
import LCK.snowtaxi.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter  extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("filter");

        // refresh-token 있다면 access-token 만료된 상태, 아니라면 null
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // refresh-token 으로 새로운 access-token 발급
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        // access-token 인증하고 처리
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
            return;
        }
    }

    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        log.info("checkRefreshTokenAndReIssueAccessToken() 호출");
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(user);
                    jwtService.sendAccessAndRefreshToken(
                            response,
                            jwtService.createAccessToken(
                                    user.getKakaoId(),
                                    user.getUserId(),
                                    user.getNickname(),
                                    user.getKakaoId()),
                            reIssuedRefreshToken);
                });
    }

    private String reIssueRefreshToken(User user) {
        log.info("reIssueRefreshToken() 호출");
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.setRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractInfo(accessToken)
                        .ifPresent(kakaoId -> userRepository.findByKakaoId(kakaoId)
                                .ifPresent(this::saveAuthentication)));

        filterChain.doFilter(request, response);
    }

    public void saveAuthentication(User user) {
        log.info("saveAuthentication() 호출");
            String password = user.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = user.getKakaoId();
            user.setPassword(password);
            userRepository.saveAndFlush(user);
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getKakaoId())
                .password(password)
                .authorities("USER")
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, password,
                        userDetailsUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("SecurityContextHolder에 저장합니다");
    }

}
