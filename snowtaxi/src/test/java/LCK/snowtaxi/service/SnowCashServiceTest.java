package LCK.snowtaxi.service;

import LCK.snowtaxi.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class SnowCashServiceTest {
    @Autowired
    SnowCashService snowCashService;

    @Test
    public void testSend() {
        User sender = User.builder()
                .userId(10)
                .kakaoId("123")
                .phone("123345677")
                .walletAddress("0x97236c32f67d5389eb16afe2ba7e0fdb8bfe082c")
                .build();

        User receiver = User.builder()
                .userId(10)
                .kakaoId("123")
                .phone("123345677")
                .walletAddress("0x690f56b25c1c7f1e41e3dee2da26ebaa1759b85f")
                .build();

        System.out.println("b sender" + snowCashService.getBalance(sender));
        System.out.println("b receiver" + snowCashService.getBalance(receiver));

        snowCashService.sendCash(sender, receiver, 1000);

        System.out.println("a sender" + snowCashService.getBalance(sender));
        System.out.println("a receiver" + snowCashService.getBalance(receiver));
    }

}