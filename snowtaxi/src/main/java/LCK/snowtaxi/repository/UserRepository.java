package LCK.snowtaxi.repository;

import LCK.snowtaxi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
