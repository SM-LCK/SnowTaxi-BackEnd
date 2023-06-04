package LCK.snowtaxi.controller;

import LCK.snowtaxi.service.PotlistService;
import jakarta.servlet.http.HttpSession;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/potlist")
@RestController
public class PotlistController {

    @Autowired
    PotlistService potlistService;

    @PostMapping("/{departure}/")
    public String makePot(@PathVariable String departure, @RequestParam String ridingTime, @NotNull HttpSession session) {
        long userId = (long)session.getAttribute("userId");
        potlistService.makePot(departure, ridingTime, userId);
        return "make";
    }





}
