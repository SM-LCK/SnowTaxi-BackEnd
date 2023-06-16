package LCK.snowtaxi.controller;

import LCK.snowtaxi.domain.User;
import LCK.snowtaxi.repository.UserRepository;
import LCK.snowtaxi.service.SnowCashService;
import LCK.snowtaxi.service.UserService;
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
    UserRepository userRepository;
    @Autowired
    JwtService jwtService;

    @PostMapping("/charge")
    public void charge(HttpServletRequest request, @RequestParam("amount") int amount){
        System.out.println("in");
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);
        long userId = tokenInfoVo.getUserId();

        snowCashService.chargeCash(userId, amount);
    }

    @PostMapping("/pay")
    public void pay(HttpServletRequest request) {
        System.out.println("whyy");
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);

        long userId = tokenInfoVo.getUserId();
        User user = userRepository.findById(userId).get();

        snowCashService.sendCash(userId, user.getParticipatingPotId(), 1200);
    }

}
