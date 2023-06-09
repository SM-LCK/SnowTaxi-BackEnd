package LCK.snowtaxi.controller;

import LCK.snowtaxi.domain.Potlist;
import LCK.snowtaxi.service.ParticipationService;
import LCK.snowtaxi.service.PotlistService;
import jakarta.servlet.http.HttpSession;
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

    @GetMapping("/{departure}")
    public List<Potlist> getPotlist(@PathVariable String departure) {
        return potlistService.getTodayPotlist(departure, LocalDate.now());
    }

    @PostMapping("/{departure}/")
    public String makePot(@PathVariable String departure, @RequestParam String ridingTime, @NotNull HttpSession session) {
        long userId = (long)session.getAttribute("userId");
        potlistService.makePot(departure, ridingTime, userId);
        return "makePot";
    }

    @PostMapping("/{departure}/2")
    public void joinPot(@PathVariable String departure, @RequestParam long potlistId, @NotNull HttpSession session){
        long userId = (long)session.getAttribute("userId");

        potlistService.increaseHeadCount(potlistId);
        participationService.makeParticipation(userId, potlistId);
        return;
    }




}
