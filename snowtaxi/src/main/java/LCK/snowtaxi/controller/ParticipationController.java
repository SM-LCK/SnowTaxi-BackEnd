package LCK.snowtaxi.controller;

import LCK.snowtaxi.domain.Participation;
import LCK.snowtaxi.repository.ParticipationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/participation")
@RestController
public class ParticipationController {
    @Autowired
    private ParticipationRepository participationRepository;
    

}
