package LCK.snowtaxi.repository;

import LCK.snowtaxi.domain.Participation;
import LCK.snowtaxi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findByUserIdAndPotlistId(long userId, long potlistId);
}
