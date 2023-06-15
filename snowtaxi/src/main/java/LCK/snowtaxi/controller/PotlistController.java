package LCK.snowtaxi.controller;

import LCK.snowtaxi.domain.Potlist;
import LCK.snowtaxi.service.ParticipationService;
import LCK.snowtaxi.service.PotlistService;
import LCK.snowtaxi.token.JwtService;
import LCK.snowtaxi.token.TokenInfoVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequestMapping(path = "/potlist")
@RestController
public class PotlistController {

    @Autowired
    PotlistService potlistService;
    @Autowired
    ParticipationService participationService;
    @Autowired
    JwtService jwtService;

    @GetMapping("/{departure}")
    public List<Potlist> getPotlist(@PathVariable String departure) {
        return potlistService.getTodayPotlist(departure, LocalDate.now());
    }

    @PostMapping("/{departure}/create")
    public String makePot(@PathVariable String departure, @RequestParam String ridingTime, HttpServletRequest request) {
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);

        long userId = tokenInfoVo.getUserId();
        long potlistId = potlistService.makePot(departure, ridingTime, userId);
        if (potlistId != 0) {
            participationService.makeParticipation(userId, potlistId);
            return "success";
        } else {
            return "fail";
        }
    }

    @PostMapping("/{departure}/join")
    public String joinPot(@PathVariable String departure, @RequestParam long potlistId, HttpServletRequest request){
        String access_token = jwtService.extractAccessToken(request).orElseGet(() -> "");
        TokenInfoVo tokenInfoVo = jwtService.getTokenInfo(access_token);

        long userId = tokenInfoVo.getUserId();

        if (participationService.makeParticipation(userId, potlistId)) {
            potlistService.increaseHeadCount(potlistId);
            return "success";
        }
        return "fail";
    }





}
