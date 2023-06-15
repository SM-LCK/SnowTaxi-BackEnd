package LCK.snowtaxi.service;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

import LCK.snowtaxi.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;

@Service
public class EthereumService {

    @Value("${ethereum.admin}")
    private String admin;
    @Value("${ethereum.contract}")
    private String contract;
    @Value("${ethereum.walletPath}")
    private String walletPath;

    private static final Web3j web3j = Web3j.build(new HttpService());

    public String createAccount(String pwd) throws Exception {
        // 새 계정 생성
        String walletFileName = WalletUtils.generateNewWalletFile(pwd, new File(walletPath));

        // 생성된 계정 정보 출력
        String walletFilePath = walletPath + File.separator + walletFileName;
        Credentials credentials = WalletUtils.loadCredentials(pwd, walletFilePath);
        String address = credentials.getAddress();

        System.out.println("New account address: " + address);
        System.out.println("Wallet file path: " + walletFilePath);

        return address;
    }

    public String getAccount(int idx) throws Exception {
        EthAccounts accounts = web3j.ethAccounts().send();
        return accounts.getAccounts().get(idx);
    }

    public Object ethCall(Function function) throws IOException {
        //1. transaction 제작
        Transaction transaction = Transaction.createEthCallTransaction(
                admin,
                contract,
                FunctionEncoder.encode(function)
        );
        //2. ethereum 호출후 결과 가져오기
        EthCall ethCall = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();

        //3. 결과값 decode
        List<Type> decode = FunctionReturnDecoder.decode(ethCall.getResult(), function.getOutputParameters());

        System.out.println("ethCall.getResult() = " + ethCall.getResult());
        System.out.println("getValue = " + decode.get(0).getValue());
        System.out.println("getType = " + decode.get(0).getTypeAsString());

        return decode.get(0).getValue();
    }

    public String ethSendTransaction(Function function) throws IOException, InterruptedException, ExecutionException {
        //1. account에 대한 nonce값 가져오기.
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(admin, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        //2. Transaction값 제작
        Transaction transaction = Transaction.createFunctionCallTransaction(
                admin,
                nonce,
                Transaction.DEFAULT_GAS,
                null,
                contract,
                FunctionEncoder.encode(function)
        );

        //3. ethereum Call
        EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();

        // transaction에 대한 transaction Hash값 얻기.
        String transactionHash = ethSendTransaction.getTransactionHash();

        // ledger에 쓰여지기 까지 기다리기.
        Thread.sleep(6000);

        return transactionHash;
    }

//    public TransactionReceipt getReceipt(String transactionHash) throws IOException {
//
//        EthGetTransactionReceipt transactionReceipt = web3j.ethGetTransactionReceipt(transactionHash).send();
//
//        if(transactionReceipt.getTransactionReceipt().isPresent())
//        {
//            System.out.println("transactionReceipt.getResult().getContractAddress() = " +
//                    transactionReceipt.getResult());
//        }
//        else
//        {
//            System.out.println("transaction complete not yet");
//        }
//
//        return transactionReceipt.getResult();
//    }
//
//    private class PersonalLockException extends RuntimeException
//    {
//        public PersonalLockException(String msg)
//        {
//            super(msg);
//        }
//    }

}