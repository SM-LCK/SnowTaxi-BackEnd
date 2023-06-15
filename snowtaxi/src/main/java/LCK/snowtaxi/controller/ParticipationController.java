package LCK.snowtaxi.controller;

import LCK.snowtaxi.Dto.MyPotDto;
import LCK.snowtaxi.domain.Participation;
import LCK.snowtaxi.repository.ParticipationRepository;
import LCK.snowtaxi.service.ParticipationService;
import LCK.snowtaxi.token.JwtService;
import LCK.snowtaxi.token.TokenInfoVo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/participation")
@RestController
public class ParticipationController {
    @Autowired
    JwtService jwtService;
    @Autowired
    ParticipationService participationService;

    @GetMapping("/mypot") // null 이면 현재 팟 참여하지 않음
    public MyPotDto viewMyPot(HttpServletRequest request) {
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);

        long userId = tokenInfoVo.getUserId();
        return participationService.viewMyPot(userId);
    }
    @DeleteMapping("/delete")
    public void potOut(HttpServletRequest request) {
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);

        long userId = tokenInfoVo.getUserId();
        participationService.del(userId);
        return;
    }

//    @PatchMapping("/{userId}")
//    public void dutchpay() {
//
//    }


}
