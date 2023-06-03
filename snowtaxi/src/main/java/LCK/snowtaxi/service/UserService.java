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

    public boolean isUser(String userId) {
        return userRepository.existsById(userId);
    }

    public void createUser(String userId, String phone) {
        User user = User.builder()
                .kakaoId(userId)
                .phone(phone)
                .build();

        userRepository.save(user);
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }



}
