package LCK.snowtaxi.controller;

import LCK.snowtaxi.service.SnowCashService;
import LCK.snowtaxi.token.JwtService;
import LCK.snowtaxi.token.TokenInfoVo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/cash")
@RestController
public class SnowCashController {
    @Autowired
    SnowCashService snowCashService;
    @Autowired
    JwtService jwtService;

    @PostMapping("/charge")
    public void charge(HttpServletRequest request, @RequestParam("amount") int amount){
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);
        long userId = tokenInfoVo.getUserId();

        snowCashService.chargeCash(userId, amount);
    }

    @PostMapping("/pay")
    public void pay(HttpServletRequest request, @RequestParam("potId") int potId){
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);

        long userId = tokenInfoVo.getUserId();

        snowCashService.sendCash(userId, potId, 1200);
    }

}
