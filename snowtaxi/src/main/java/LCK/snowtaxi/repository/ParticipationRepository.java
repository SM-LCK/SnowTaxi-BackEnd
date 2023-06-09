package LCK.snowtaxi.repository;

import LCK.snowtaxi.domain.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    Optional<Participation> findByUserIdAndPotlistId(long userId, long potlistId);

    @Query(value = "select p.potlist_id from Participation p where p.user_id = :user_id and p.is_paid = false", nativeQuery = true)
    List<Long> findPotId(@Param("user_id") long userId);

    List<Participation> findByPotlistId(long potlistId);

    @Query(value = "select participation_id from Participation p where p.potlist_id = :potlist_id", nativeQuery = true)
    List<Long> findByParticipationId(@Param("potlist_id") long potlistId);

}
