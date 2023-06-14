package LCK.snowtaxi.service;

import LCK.snowtaxi.domain.User;
import LCK.snowtaxi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.http.HttpService;

import javax.imageio.IIOException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@Service
public class SnowCashService {

    @Value("${ethereum.admin}")
    private String admin;
    @Value("${ethereum.contract}")
    private String contract;
    @Value("${ethereum.walletPath}")
    private String walletPath;
    static Web3j web3j = Web3j.build(new HttpService());

    @Autowired
    final EthereumService ethereumService;

    @Autowired
    UserRepository userRepository;
    public SnowCashService(EthereumService ethereumService) {
        this.ethereumService = ethereumService;
    }

    public Long getBalance(User user) {
        Long balance = null;
        Function getBalances = new Function(
                "balances",
                Arrays.asList(new Address(user.getWalletAddress())),
                Arrays.asList(new TypeReference<Uint256>() {})
        );

        try {
            balance = Long.valueOf(String.valueOf(ethereumService.ethCall(getBalances)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return balance;
    }

    public void chargeCash(User user, int amount) {
        Function charge = new Function("charge",
                Arrays.asList(new Address(user.getWalletAddress()), new Uint256(amount)),
                Collections.emptyList()
        );

        try {
            String transactionHash = ethereumService.ethSendTransaction(charge);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCash(User sender, User receiver, int amount) {
        Function send = new Function("send",
                Arrays.asList(new Address(sender.getWalletAddress()), new Address(receiver.getWalletAddress()), new Uint256(amount)),
                Collections.emptyList()
        );

        try {
            String transactionHash = ethereumService.ethSendTransaction(send);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
