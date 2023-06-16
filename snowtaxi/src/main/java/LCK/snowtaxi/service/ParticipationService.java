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
    public boolean makeParticipation(long userId, long potlistId) {
        User user = userRepository.findById(userId).get();

        if (user.getParticipatingPotId() == 0) {
            Participation participation = Participation.builder()
                    .userId(userId)
                    .potlistId(potlistId)
                    .isPaid(false)
                    .build();
            user.setParticipatingPotId(potlistId);
            participationRepository.save(participation);
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }

    public MyPotDto viewMyPot(long userId) {
        User user = userRepository.findById(userId).get();
        long potId = user.getParticipatingPotId();

        if(potId == 0)
            return null; // 현재 참여중인 팟 없음

        Potlist mypot = potlistRepository.findByPotlistId(potId).orElse(null);
        int n = mypot.getHeadCount();

        List<Participation> participations = participationRepository.findByPotlistId(potId);

        MyPotDto myPotDto = new MyPotDto();
        myPotDto.setPotlist(mypot);
        List<MyPotMemberDto> myPotMemberDtos = new ArrayList<MyPotMemberDto>();
        myPotDto.setIsHost(false);

        for(int i = 0; i < n; i++) {
            long memberId = participations.get(i).getUserId();
            User member = userRepository.findById(memberId).orElse(null);
            MyPotMemberDto myPotMemberDto = new MyPotMemberDto(member.getNickname(), member.getPhone(), participations.get(i).isPaid());
            if (memberId == mypot.getHostUserId()) {
                myPotDto.setHost(myPotMemberDto);
                if (memberId == userId)
                    myPotDto.setIsHost(true);
            } else {
                if (memberId != userId) {
                    myPotMemberDtos.add(myPotMemberDto);
                }
            }
            if (memberId == userId)
                myPotDto.setMe(myPotMemberDto);
        }
        myPotDto.setMembers(myPotMemberDtos);

        return myPotDto;
    }

    public void del(long userId){
        User user = userRepository.findById(userId).get();
        long potId = user.getParticipatingPotId();

        user.setParticipatingPotId(0);
        userRepository.save(user);

        if(potId == 0)
            return; // 현재 참여중인 팟 없음

        long participationId = participationRepository.findByUserIdAndPotlistId(userId, potId).get().getParticipationId();
        Potlist mypot = potlistRepository.findById(potId).get();

        if(mypot.getHostUserId() == userId) { // 본인이 방장
            if(mypot.getHeadCount() == 1) { // 본인밖에 없으면 방폭
                participationRepository.deleteById(participationId);
                potlistRepository.deleteById(potId);
                return;
            } else { // 다음 사람으로 방장 위임
                participationRepository.deleteById(participationId);

                List<Participation> participations = participationRepository.findByPotlistId(mypot.getPotlistId());
                mypot.setHostUserId(participations.get(0).getUserId());

            }
        } else {
            participationRepository.deleteById(participationId);
        }
        mypot.setHeadCount(mypot.getHeadCount()-1);
        potlistRepository.saveAndFlush(mypot);

        user.setParticipatingPotId(0);
        userRepository.saveAndFlush(user);
    }
}
