package LCK.snowtaxi.service;

import LCK.snowtaxi.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
//@Transactional
class SnowCashServiceTest {
    @Autowired
    SnowCashService snowCashService;
    @Autowired
    UserService userService;

    @Test
    public void testSend() {
        long senderId = 2;
        long receiverId = 3;
        System.out.println("b sender" + snowCashService.getBalance(senderId));
        System.out.println("b receiver" + snowCashService.getBalance(receiverId));

        snowCashService.sendCash(senderId, receiverId, 1000);

        System.out.println("a sender" + snowCashService.getBalance(senderId));
        System.out.println("a receiver" + snowCashService.getBalance(receiverId));
    }

    @Test
    public void charge() {
        long chargerId = 2;
        System.out.println("balance = " + snowCashService.getBalance(chargerId));
    }
}