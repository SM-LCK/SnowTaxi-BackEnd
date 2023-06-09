package LCK.snowtaxi.service;

import LCK.snowtaxi.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.io.File;

@Service
public class BlockChainService {
    // Ethereum 네트워크에 연결
    static Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));

    public void getEthCreateAccount(User user) throws Exception {
        try {
            // 새 계정 생성
            String password = "snowtaxi";
            String walletFileName = WalletUtils.generateNewWalletFile(password, new File("/Users/kelly/Blockchain/data/keystore"));

            // 생성된 계정 정보 출력
            String walletFilePath = "/Users/kelly/Blockchain/data/keystore" + File.separator + walletFileName;
            Credentials credentials = WalletUtils.loadCredentials(password, walletFilePath);
            String address = credentials.getAddress();
            System.out.println("New account address: " + address);
            System.out.println("Wallet file path: " + walletFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
