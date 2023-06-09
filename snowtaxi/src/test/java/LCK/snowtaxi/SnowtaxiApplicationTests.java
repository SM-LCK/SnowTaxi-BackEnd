package LCK.snowtaxi;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.http.HttpService;

import java.io.File;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;

@SpringBootTest
class SnowtaxiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mysqlTest() {
		// mysql connection example
		String URL = "jdbc:mysql://localhost:3306/snowtaxi?characterEncoding=UTF-8&serverTimezone=UTC";
		String USER = "root";
		String PW = "password";
		try(Connection conn = DriverManager.getConnection(URL, USER, PW)){
			System.out.println(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getEthClientVersionTest() throws Exception
	{
		Web3j web3j = Web3j.build(new HttpService());
		Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
		System.out.println(web3ClientVersion.getWeb3ClientVersion());
	}

	@Test
	public void getEthBalanceTest() throws Exception
	{
		Web3j web3j = Web3j.build(new HttpService());
		EthGetBalance result = new EthGetBalance();
		// example address
		String address = "0x435cbbc7d7243acaaeeba647882263a3a64f76bf";
		result = web3j.ethGetBalance(address,
						DefaultBlockParameter.valueOf("latest"))
				.sendAsync()
				.get();
		BigInteger balance = result.getBalance();
		System.out.println(balance);
	}

	@Test
	public void getEthCreateAccount() throws Exception {
		try {
			// Ethereum 네트워크에 연결
			Web3j web3j = Web3j.build(new HttpService("http://127.0.0.1:8545"));

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
