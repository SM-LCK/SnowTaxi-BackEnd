package LCK.snowtaxi.service;

import LCK.snowtaxi.domain.Participation;
import LCK.snowtaxi.repository.ParticipationRepository;

public class ParticipationService {
    private ParticipationRepository participationRepository;

    public void makeParticipation(long userId, long potlistId) {
        Participation participation = Participation.builder()
                .userId(userId)
                .potlistId(potlistId)
                .isPaid(false)
                .build();

        participationRepository.save(participation);
    }
}
