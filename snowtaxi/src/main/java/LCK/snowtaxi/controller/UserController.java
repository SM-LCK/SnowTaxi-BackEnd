package LCK.snowtaxi.controller;

import LCK.snowtaxi.service.KakaoService;
import LCK.snowtaxi.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/login")
    public String login()
    {
        String url = "https://kauth.kakao.com/oauth/authorize?client_id=60968cb46ee97afe3cb98d0b6cac5aa5&redirect_uri=http://localhost:9090/user/kakao&response_type=code";
        return url;
    }

    @GetMapping("/kakao")
    public String getCI(@RequestParam String code, HttpSession session) throws IOException {
        System.out.println("code = " + code);

        String access_token = ks.getAccessToken(code);
        HashMap<String, Object> userInfo = ks.getUserInfo(access_token);


        if (userInfo.get("id") != null) {
            session.setAttribute("access_token", access_token);
            session.setAttribute("kakaoId", userInfo.get("id"));
            session.setAttribute("nickname", userInfo.get("nickname"));
        }

        return "home";
    }

    @GetMapping("/validation")
    public String a (HttpSession session) {
        String kakaoId = (String)session.getAttribute("kakaoId");
        if (!userService.isUser(kakaoId)){
            return "/signUp";
        }
        else {
            long userId = userService.getUserId(kakaoId);
            session.setAttribute("userId", userId);
        }
        // signup 안하고 나가면 unlink 추가하세요
        return "home";
    }


    @ResponseBody
    @PostMapping("/signUp")
    public String signUp(@RequestParam String phone,@NotNull HttpSession session) {
        String kakaoId = (String)session.getAttribute("kakaoId");
        userService.createUser(kakaoId, phone);
        long userId = userService.getUserId(kakaoId);
        session.setAttribute("userId", userId);

        return "home";
    }
    
    @GetMapping("/logout")
    public String logout(@NotNull HttpSession session) {
        String access_Token = (String)session.getAttribute("access_token");

        if(access_Token != null && !"".equals(access_Token)){
            ks.kakaoLogout(access_Token);
            session.removeAttribute("access_Token");
            session.removeAttribute("kakaoId");
            session.removeAttribute("nickname");
            session.removeAttribute("userId");
            System.out.println("bye");
        }else{
            System.out.println("access_Token is null");
        }
        return "/login";

    }

    @DeleteMapping("/unlink")
    public String unlink(@NotNull HttpSession session) {
        String access_Token = (String)session.getAttribute("access_token");
        long userId = (long)session.getAttribute("userId");

        ks.unlink(access_Token);
        userService.deleteUser(userId);
        session.invalidate();

        return "/login";
    }

}
