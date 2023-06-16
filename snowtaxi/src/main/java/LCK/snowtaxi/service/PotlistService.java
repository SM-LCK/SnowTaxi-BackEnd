package LCK.snowtaxi.service;

import LCK.snowtaxi.Dto.GetPotListDto;
import LCK.snowtaxi.Dto.PotListDto;
import LCK.snowtaxi.domain.Participation;
import LCK.snowtaxi.domain.Potlist;
import LCK.snowtaxi.domain.User;
import LCK.snowtaxi.repository.PotlistRepository;
import LCK.snowtaxi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PotlistService {
    @Autowired
    private PotlistRepository potlistRepository;
    @Autowired
    UserRepository userRepository;

    public GetPotListDto getTodayPotlist(String departure, LocalDate createdAt, long userId) {
        List<Potlist> pots = potlistRepository.findByDepartureAndCreatedAt(departure, createdAt);
        long userParticipatingPotId = userRepository.findById(userId).orElse(null).getParticipatingPotId();
        User user = userRepository.findById(userId).get();
        boolean isParticipating = false;
        if (user.getParticipatingPotId() != 0)
            isParticipating = true;

        List<PotListDto> potListDtos = new ArrayList<PotListDto>();

        for (int i = 0; i< pots.size(); i++) {
            long potId = pots.get(i).getPotlistId();
            Potlist potlist = potlistRepository.findById(potId).get();
            boolean isMyPot = false;

            if (potId == userParticipatingPotId){
                isMyPot = true;
            }

            PotListDto potListDto = new PotListDto(potlist, isMyPot);
            potListDtos.add(potListDto);
        }
        return new GetPotListDto(potListDtos, isParticipating);
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
