package LCK.snowtaxi.repository;

import LCK.snowtaxi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findBKakaoId(String kakaoId);
    boolean existsByKakaoId(String kakaoId);

}
