package LCK.snowtaxi.blockchain;

import LCK.snowtaxi.blockchain.EthereumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Service
public class CoinContractService {

    @Autowired
    EthereumService ethereumService;

    public Long balances(String userAddress) {
        Long balance = null;

        Function getBalances = new Function(
                "balances",
                Arrays.asList(new Address(userAddress)),
                Arrays.asList(new TypeReference<Uint256>() {})
        );

        try {
            balance = Long.valueOf(String.valueOf(ethereumService.ethCall(getBalances)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return balance;
    }

    public void charge(String userAddress, int amount) {
        Function charge = new Function("charge",
                Arrays.asList(new Address(userAddress), new Uint256(amount)),
                Collections.emptyList()
        );

        try {
            ethereumService.ethSendTransaction(charge);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(String senderAddress, String receiverAddress, int amount) {
        Function send = new Function("send",
                Arrays.asList(new Address(senderAddress), new Address(receiverAddress), new Uint256(amount)),
                Collections.emptyList()
        );

        try {
            ethereumService.ethSendTransaction(send);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
