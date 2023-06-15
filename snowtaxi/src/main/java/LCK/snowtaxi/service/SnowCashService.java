package LCK.snowtaxi.service;

import LCK.snowtaxi.blockchain.CoinContractService;
import LCK.snowtaxi.domain.Participation;
import LCK.snowtaxi.domain.Potlist;
import LCK.snowtaxi.domain.User;
import LCK.snowtaxi.repository.ParticipationRepository;
import LCK.snowtaxi.repository.PotlistRepository;
import LCK.snowtaxi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

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
    CoinContractService coinContractService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PotlistRepository potlistRepository;
    @Autowired
    ParticipationRepository participationRepository;


    public Long getBalance(long userId) {
        String address = userRepository.findByUserId(userId).get().getWalletAddress();

        return coinContractService.balances(address);
    }

    public void chargeCash(long userId, int amount) {
        User user = userRepository.findByUserId(userId).get();
        String address = user.getWalletAddress();

        long beforeBalance = user.getBalance();
        user.setBalance(beforeBalance + amount);

        coinContractService.charge(address, amount);
        userRepository.save(user);
    }

    public boolean sendCash(long senderId, long potId, int amount) {
        User sender = userRepository.findByUserId(senderId).get();
        long senderBalance = sender.getBalance();

        if (senderBalance >= amount) {
            String senderAddress = sender.getWalletAddress();


            Potlist potlist = potlistRepository.findById(potId).get();
            Long receiverId = potlist.getHostUserId();

            User receiver = userRepository.findByUserId(receiverId).get();
            String receiverAddress = receiver.getWalletAddress();
            long receiverBalance = receiver.getBalance();

            Participation participation = participationRepository.findByUserIdAndPotlistId(senderId, potId).get();

            coinContractService.send(senderAddress, receiverAddress, amount);

            participation.setPaid(true);
            participationRepository.save(participation);

            sender.setBalance(senderBalance - amount);
            receiver.setBalance(receiverBalance + amount);
            userRepository.save(sender);
            userRepository.save(receiver);

            return true;
        } else {
            return false;
        }
    }
}
