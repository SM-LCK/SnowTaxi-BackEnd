package LCK.snowtaxi.controller;

import LCK.snowtaxi.service.KakaoService;
import LCK.snowtaxi.service.UserService;
import LCK.snowtaxi.token.JwtService;
import LCK.snowtaxi.token.TokenInfoVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

@RequestMapping(path = "/user")
@RestController
public class UserController {
    @Autowired
    KakaoService ks;
    @Autowired
    UserService userService;
    @Autowired
    JwtService jwtService;

    @GetMapping("/login")
    public String login()
    {
        String url = "https://kauth.kakao.com/oauth/authorize?client_id=60968cb46ee97afe3cb98d0b6cac5aa5&redirect_uri=http://localhost:9090/user/kakao&response_type=code";
        return url;
    }

    @GetMapping("/kakao")
    public String getCI(@RequestParam String code) throws IOException {
        System.out.println("code = " + code);
        String access_token = ks.getAccessToken(code);

        return access_token;
    }

    @PostMapping("/auth")
    public void validation (@RequestBody AuthRequest authRequest, HttpServletResponse response) throws IOException {
        String kakao_token = authRequest.getKakao_token();
        HashMap<String, Object> userInfo = ks.getUserInfo(kakao_token);

        String kakaoId = (String) userInfo.get("id");
        String nickname = (String) userInfo.get("nickname");

        if (!userService.isUser(kakaoId)) {
            userService.createUser(kakaoId, nickname, null);
            System.out.println("전화번호를 받으세요");
        }

        long userId = userService.getUserId(kakaoId);
        String access_token = jwtService.createAccessToken(kakaoId,userId,nickname,kakao_token);
        String refresh_token = jwtService.createRefreshToken();

        jwtService.updateRefreshToken(userId, refresh_token);

        jwtService.sendAccessAndRefreshToken(response, access_token, refresh_token);
        System.out.println("토큰을 헤더로 전송");
        return;
    }


//    @ResponseBody
//    @PostMapping("/signUp")
//    public String signUp(@RequestParam String phone) {
//
//        return "home";
//    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);

        String kakao_token = tokenInfoVo.getKakao_token();
        ks.kakaoLogout(kakao_token);
        System.out.println("bye");

        SecurityContextHolder.getContext().setAuthentication(null);
        return "/login";
    }

    @GetMapping("/unlink")
    public String unlink(HttpServletRequest request) {
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);

        String kakao_token = tokenInfoVo.getKakao_token();
        long userId = tokenInfoVo.getUserId();

        ks.unlink(kakao_token);
        userService.deleteUser(userId);

        SecurityContextHolder.getContext().setAuthentication(null);

        return "/login";
    }


    // 인증요청 객체
    @AllArgsConstructor
    @Data
    static class AuthRequest{
        private String kakao_token;
        public AuthRequest(){

        }
    }
}