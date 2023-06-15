package LCK.snowtaxi.service;

import LCK.snowtaxi.domain.Participation;
import LCK.snowtaxi.domain.Potlist;
import LCK.snowtaxi.domain.User;
import LCK.snowtaxi.repository.PotlistRepository;
import LCK.snowtaxi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PotlistService {
    @Autowired
    private PotlistRepository potlistRepository;
    @Autowired
    UserRepository userRepository;

    public List<Potlist> getTodayPotlist(String departure, LocalDate createdAt) {
        return potlistRepository.findByDepartureAndCreatedAt(departure, createdAt);
    }

    public long makePot(String departure, String ridingTime, long hostUserId) {
        User host = userRepository.findById(hostUserId).get();

        System.out.println("sdfsdfsfs" + host.getParticipatingPotId());

        if (host.getParticipatingPotId() == 0) {
            Potlist potlist = Potlist.builder()
                    .departure(departure)
                    .ridingTime(ridingTime)
                    .headCount(1)
                    .hostUserId(hostUserId)
                    .createdAt(LocalDate.now())
                    .isPaidRequest(false)
                    .build();

            potlistRepository.save(potlist);
            return potlist.getPotlistId();
        } else {
            return 0;
        }
    }

    public void increaseHeadCount(long potlistId) {
        Potlist potlist = potlistRepository.findById(potlistId).orElse(null);
        potlist.setHeadCount(potlist.getHeadCount()+1);
       potlistRepository .saveAndFlush(potlist);
    }

}
