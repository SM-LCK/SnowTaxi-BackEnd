package LCK.snowtaxi.service;

import LCK.snowtaxi.domain.User;
import LCK.snowtaxi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean isUser(String kakaoId) {
        return userRepository.existsByKakaoId(kakaoId);
    }

    public void createUser(String kakaoId, String nickname, String phone, String walletAddress) {
        User user = User.builder()
                .kakaoId(kakaoId)
                .nickname(nickname)
                .phone(phone)
                .walletAddress(walletAddress)
                .build();

        userRepository.save(user);
    }

    public void deleteUser(long userId) {
        userRepository.deleteById(userId);
    }

    public long getUserId(String kakaoId) {
        User user = userRepository.findByKakaoId(kakaoId).orElseThrow(IllegalArgumentException::new);
        long userId = user.getUserId();

        return userId;
    }

}
