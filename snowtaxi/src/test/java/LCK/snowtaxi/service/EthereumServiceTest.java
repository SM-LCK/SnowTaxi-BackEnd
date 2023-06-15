package LCK.snowtaxi.service;

import LCK.snowtaxi.blockchain.EthereumService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

import java.util.Arrays;
import java.util.Collections;

@SpringBootTest
class EthereumServiceTest {

    @Autowired
    EthereumService ethereumService;
    String reciever = "0x97236c32f67d5389eb16afe2ba7e0fdb8bfe082c";

    @Test
    public void createAccount() {
        try {
            System.out.println(ethereumService.createAccount("pwd"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ethCallTest() {
        Function balances = new Function("balances",
                Arrays.asList(new Address(reciever)),
                Arrays.asList(new TypeReference<Uint256>() {}));

        try {
            Object balance = ethereumService.ethCall(balances);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ethSendTransactionTest() {
        Function mint = new Function("mint",
                Arrays.asList(new Address(reciever), new Uint256(100)),
                Collections.emptyList());

        try {
            Object balance = ethereumService.ethSendTransaction(mint);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}