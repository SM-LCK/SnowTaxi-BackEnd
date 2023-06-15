package LCK.snowtaxi.service;

import LCK.snowtaxi.Dto.MyPotDto;
import LCK.snowtaxi.Dto.MyPotMemberDto;
import LCK.snowtaxi.domain.Participation;
import LCK.snowtaxi.domain.Potlist;
import LCK.snowtaxi.domain.User;
import LCK.snowtaxi.repository.ParticipationRepository;
import LCK.snowtaxi.repository.PotlistRepository;
import LCK.snowtaxi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ParticipationService {
    @Autowired
    private ParticipationRepository participationRepository;
    @Autowired
    private PotlistRepository potlistRepository;
    @Autowired
    private UserRepository userRepository;

    public void makeParticipation(long userId, long potlistId) {
        Participation participation = Participation.builder()
                .userId(userId)
                .potlistId(potlistId)
                .isPaid(false)
                .build();

        participationRepository.save(participation);
    }

    public MyPotDto viewMyPot(long userId) {
        List<Long> potId = participationRepository.findPotId(userId);
        if(potId.get(0) ==null) {
            return null; // 현재 참여중인 팟 없음
        }

        Potlist mypot = potlistRepository.findByPotlistId(potId.get(0)).orElse(null);
        int n = mypot.getHeadCount();

        List<Participation> participations = participationRepository.findByPotlistId(mypot.getPotlistId());

        MyPotDto myPotDto = new MyPotDto();
        myPotDto.setPotlist(mypot);
        List<MyPotMemberDto> myPotMemberDtos = new ArrayList<MyPotMemberDto>();
        System.out.println(participations.size());
        for(int i = 0; i < n; i++) {
            long memberId = participations.get(i).getUserId();
            User member = userRepository.findById(memberId).orElse(null);
            MyPotMemberDto myPotMemberDto = new MyPotMemberDto(member.getNickname(), member.getPhone(), participations.get(i).isPaid());
            myPotMemberDtos.add(myPotMemberDto);
        }
        myPotDto.setMembers(myPotMemberDtos);

        return myPotDto;
    }

    public void del(long userId){
        List<Long> potId = participationRepository.findPotId(userId);
        if(potId == Collections.<Long>emptyList()) {return;} // 현재 참여중인 팟 없음
        List<Long> participationId = participationRepository.findByParticipationId(potId.get(0));
        Potlist mypot = potlistRepository.findByPotlistId(potId.get(0)).orElse(null);

        // 본인이 방장
        if(mypot.getHostUserId() == userId) {
            // 본인밖에 없으면 방폭
            if(mypot.getHeadCount() == 1) {
                participationRepository.deleteById(participationId.get(0));
                potlistRepository.deleteById(potId.get(0));
                return;
            }
            participationRepository.deleteById(participationId.get(0));

            List<Participation> participations = participationRepository.findByPotlistId(mypot.getPotlistId());
            mypot.setHostUserId(participations.get(0).getUserId());

            mypot.setHeadCount(mypot.getHeadCount()-1);
            potlistRepository.saveAndFlush(mypot);
            return;
        }
        participationRepository.deleteById(participationId.get(0));
        return;
    }
}
