package LCK.snowtaxi.repository;

import LCK.snowtaxi.domain.Potlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PotlistRepository extends JpaRepository<Potlist, Long> {

    Optional<Potlist> findByPotlistId(long potlistId);
    List<Potlist> findByDepartureAndCreatedAt(String departure, LocalDate createdAt);
}
