package LCK.snowtaxi.controller;

import LCK.snowtaxi.Dto.MyInfoDto;
import LCK.snowtaxi.blockchain.EthereumService;
import LCK.snowtaxi.domain.Participation;
import LCK.snowtaxi.repository.ParticipationRepository;
import LCK.snowtaxi.service.KakaoService;
import LCK.snowtaxi.service.ParticipationService;
import LCK.snowtaxi.service.PotlistService;
import LCK.snowtaxi.service.UserService;
import LCK.snowtaxi.token.JwtService;
import LCK.snowtaxi.token.TokenInfoVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    @Autowired
    EthereumService ethereumService;
    @Autowired
    PotlistService potlistService;
    @Autowired
    ParticipationService participationService;
    @Autowired
    ParticipationRepository participationRepository;

    @GetMapping("/login")
    public String login()
    {
        String url = "https://kauth.kakao.com/oauth/authorize?client_id=60968cb46ee97afe3cb98d0b6cac5aa5&redirect_uri=http://localhost:9090/user/kakao&response_type=code";
        return url;
    }

    @GetMapping("/kakao")
    public String getCI(@RequestParam String code) throws IOException {
        System.out.println("code = " + code);

        return "";
    }
    @GetMapping("/mkdum")
    public String makeDummy(HttpServletResponse response){
        String kakao_token =  "noToken1";
        String kakaoId = "a";
        String nickname = "이송이";
        String phone = "010-1234-1234";
        String walletAddress ="0x7d49f06791462903bd07cb80fb8b67dc13fc75a8";

        userService.createUser(kakaoId, nickname, phone, walletAddress);
        long userId = userService.getUserId(kakaoId);

        long potlistId = potlistService.makePot("숙대입구", "오후 1:00", userId);
        if (potlistId != 0)
            participationService.makeParticipation(userId, potlistId);

        kakao_token =  "noToken2";
        kakaoId = "b";
        nickname = "눈송송";
        phone = "010-1234-1234";
        walletAddress ="";

        userService.createUser(kakaoId, nickname, phone, walletAddress);
        userId = userService.getUserId(kakaoId);
        participationService.makeParticipation(userId, potlistId);
        potlistService.increaseHeadCount(potlistId);

        kakao_token =  "noToken3";
        kakaoId = "c";
        nickname = "박두두";
        phone = "010-1234-1234";
        walletAddress ="0x97236c32f67d5389eb16afe2ba7e0fdb8bfe082c";

        userService.createUser(kakaoId, nickname, phone, walletAddress);
        userId = userService.getUserId(kakaoId);

        potlistId = potlistService.makePot("효창공원앞", "오후 2:30", userId);
        if (potlistId != 0)
            participationService.makeParticipation(userId, potlistId);

        kakao_token =  "noToken4";
        kakaoId = "d";
        nickname = "박미미";
        phone = "010-1234-1234";
        walletAddress ="";

        userService.createUser(kakaoId, nickname, phone, walletAddress);
        userId = userService.getUserId(kakaoId);
        participationService.makeParticipation(userId, potlistId);
        potlistService.increaseHeadCount(potlistId);

        kakao_token =  "noToken5";
        kakaoId = "e";
        nickname = "박하하";
        phone = "010-1234-1234";
        walletAddress ="";

        userService.createUser(kakaoId, nickname, phone, walletAddress);
        userId = userService.getUserId(kakaoId);
        participationService.makeParticipation(userId, potlistId);
        potlistService.increaseHeadCount(potlistId);
        Participation participation = participationRepository.findByUserIdAndPotlistId(userId, potlistId).get();
        participation.setPaid(true);
        participationRepository.save(participation);

//        String access_token = jwtService.createAccessToken(kakaoId,userId,nickname,kakao_token);
//        String refresh_token = jwtService.createRefreshToken();
//
//        jwtService.updateRefreshToken(userId, refresh_token);
//
//        jwtService.sendAccessAndRefreshToken(response, access_token, refresh_token);
        return "makeDummy";
    }

    @PostMapping("/isUser")
    public String validation (@RequestBody AuthRequest authRequest) throws IOException {
        String kakao_token = authRequest.getKakao_token();
        HashMap<String, Object> userInfo = ks.getUserInfo(kakao_token);

        String kakaoId = (String) userInfo.get("id");

        if (!userService.isUser(kakaoId)) {
            return "SignUp";
        } else {
            return "Main";
        }
    }

    @PostMapping("/signUp")
    public void signUp(@RequestBody SignUpRequest signUpRequest) throws Exception {
        String kakao_token = signUpRequest.getKakao_token();
        String phone = signUpRequest.getPhone();
        HashMap<String, Object> userInfo = ks.getUserInfo(kakao_token);

        String kakaoId = (String) userInfo.get("id");
        String nickname = (String) userInfo.get("nickname");
        String walletAddress = ethereumService.createAccount(kakaoId);

        userService.createUser(kakaoId, nickname, phone, walletAddress);
    }

    @PostMapping("/auth")
    public void validation (@RequestBody AuthRequest authRequest, HttpServletResponse response) throws IOException {
        String kakao_token = authRequest.getKakao_token();
        HashMap<String, Object> userInfo = ks.getUserInfo(kakao_token);

        String kakaoId = (String) userInfo.get("id");
        String nickname = (String) userInfo.get("nickname");

        if (!userService.isUser(kakaoId)) {
            userService.createUser(kakaoId, nickname, "err", "err");
        }

        long userId = userService.getUserId(kakaoId);
        String access_token = jwtService.createAccessToken(kakaoId,userId,nickname,kakao_token);
        String refresh_token = jwtService.createRefreshToken();

        jwtService.updateRefreshToken(userId, refresh_token);

        jwtService.sendAccessAndRefreshToken(response, access_token, refresh_token);
        System.out.println("토큰을 헤더로 전송");

    }


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

    @GetMapping("/me")
    public MyInfoDto getMe(HttpServletRequest request) {
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);

        long userId = tokenInfoVo.getUserId();

        return userService.getUser(userId);
    }


    // 인증요청 객체
    @AllArgsConstructor
    @Data
    static class AuthRequest{
        private String kakao_token;
        public AuthRequest(){}
    }

    @AllArgsConstructor
    @Data
    static class SignUpRequest{
        private String kakao_token;
        private String phone;
        public SignUpRequest(){

        }
    }
}