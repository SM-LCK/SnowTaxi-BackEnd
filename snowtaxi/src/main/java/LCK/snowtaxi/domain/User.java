package LCK.snowtaxi.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;
    private String password;

    private String phone;
    private String walletAddress;
    private long balance;

    private String kakaoId;
    private String nickname; // --> 닉네임
    // private String imageUrl;

    private String refreshToken; // 리프레시 토큰

}
