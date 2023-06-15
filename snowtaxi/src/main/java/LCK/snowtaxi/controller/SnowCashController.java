package LCK.snowtaxi.controller;

import LCK.snowtaxi.domain.User;
import LCK.snowtaxi.service.SnowCashService;
import LCK.snowtaxi.token.JwtService;
import LCK.snowtaxi.token.TokenInfoVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(path = "/cash")
@Controller
public class SnowCashController {
    @Autowired
    SnowCashService snowCashService;
    @Autowired
    JwtService jwtService;

    @GetMapping("/charge")
    public void charge(HttpServletRequest request, @RequestParam("amount") int amount){
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);
        long userId = tokenInfoVo.getUserId();

        snowCashService.chargeCash(userId, amount);
    }

    @GetMapping("/pay")
    public void pay(HttpServletRequest request, @RequestParam("potId") int potId){
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);

        long userId = tokenInfoVo.getUserId();

        snowCashService.sendCash(userId, potId, 1200);
    }

}
