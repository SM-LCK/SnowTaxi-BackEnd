package LCK.snowtaxi.service;

import LCK.snowtaxi.domain.Potlist;
import LCK.snowtaxi.repository.PotlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PotlistService {
    @Autowired
    private PotlistRepository potlistRepository;

    public List<Potlist> getTodayPotlist(String departure, LocalDate createdAt) {
        return potlistRepository.findByDepartureAndCreatedAt(departure, createdAt);
    }

    public void makePot(String departure, String ridingTime, long hostUserId) {
        Potlist potlist = Potlist.builder()
                .departure(departure)
                .ridingTime(ridingTime)
                .headCount(1)
                .hostUserId(hostUserId)
                .createdAt(LocalDate.now())
                .isPaidRequest(false)
                .build();

        potlistRepository.save(potlist);
    }

    public void increaseHeadCount(long potlistId) {
        potlistRepository.increaseHeadCount(potlistId);
    }

}
