package LCK.snowtaxi.repository;

import LCK.snowtaxi.domain.Potlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PotlistRepository extends JpaRepository<Potlist, Long> {

    List<Potlist> findByDepartureAndCreatedAt(String departure, LocalDate createdAt);
    @Modifying
    @Query("update Potlist p set p.headCount += 1 where p.potlistId = potlistId")
    void increaseHeadCount(@Param("potlistId") long potlistId);
}
