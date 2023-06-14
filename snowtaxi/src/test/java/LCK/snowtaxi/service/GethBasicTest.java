package LCK.snowtaxi.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;


@SpringBootTest
@Transactional
public class GethBasicTest {
    static Admin web3j = Admin.build(new HttpService());

    @Test
    public void checkClientVersion() throws Exception {
        try {
            Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
            System.out.println(web3ClientVersion.getWeb3ClientVersion());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getAccounts() throws Exception {
        try {
            EthAccounts accounts = web3j.ethAccounts().send();
            System.out.println("accounts = " + accounts.getAccounts());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
