package LCK.snowtaxi.service;

import LCK.snowtaxi.domain.User;
import LCK.snowtaxi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean isUser(String kakaoId) {
        return userRepository.existsByKakaoId(kakaoId);
    }

    public void createUser(String kakaoId, String phone) {
        User user = User.builder()
                .kakaoId(kakaoId)
                .phone(phone)
                .build();

        userRepository.save(user);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public long getUserId(String kakaoId) {
        User user = userRepository.findBKakaoId(kakaoId).orElseThrow(IllegalArgumentException::new);
        long userId = user.getUserId();

        return userId;
    }

}
