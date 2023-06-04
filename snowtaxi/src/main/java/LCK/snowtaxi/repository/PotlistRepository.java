package LCK.snowtaxi.repository;

import LCK.snowtaxi.domain.Potlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface PotlistRepository extends JpaRepository<Potlist, Long> {

}
