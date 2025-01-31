package dev.ritesh.Banking_v2.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.ritesh.Banking_v2.entities.TransactionInfo;
import dev.ritesh.Banking_v2.entities.UserInfo;
import dev.ritesh.Banking_v2.repositories.TransactionRepository;
import dev.ritesh.Banking_v2.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<UserInfo> userDetail = userRepository.findByUsername(username);

        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

    }

    public String addUser(UserInfo userInfo) {
        boolean userExists = userRepository.existsByUsername(userInfo.getUsername());

        if (userExists) {
            return "Another user by the same name exists";
        }

        userInfo.setCreatedAt(new Date());
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userRepository.save(userInfo);
        return "Successfully saved account";
    }

    public Optional<UserInfo> getUser(Long userId) {
        Optional<UserInfo> searchResult = userRepository.findById(userId);
        return searchResult;
    }

    public Optional<UserInfo> getUser(String username) {
        Optional<UserInfo> searchResult = userRepository.findByUsername(username);
        return searchResult;
    }

    public String deposit(String username, double amount) {
        UserInfo searchedAccount = userRepository.findByUsername(username)
            .orElse(null);

        if (searchedAccount == null) {
            return "Account not found";
        }

        double currentBalance = searchedAccount.getBalance() + amount;
        searchedAccount.setBalance(currentBalance);
        userRepository.save(searchedAccount);
        

        TransactionInfo transaction = new TransactionInfo();
        transaction.setType("deposit");
        transaction.setAmount(amount);
        transaction.setTime(new Date());
        transaction.setFromUsername(searchedAccount.getUsername());
        transaction.setToUsername(searchedAccount.getUsername());

        searchedAccount.addTransaction(transaction);

        transactionRepository.save(transaction);

        return "Successfully deposited!";
    }

    public String withdraw(String username, double amount) {
        UserInfo searchedAccount = userRepository.findByUsername(username)
            .orElse(null);

        if (searchedAccount == null) {
            return "Account not found";
        }
    
        if (searchedAccount.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        double currentBalance = searchedAccount.getBalance() - amount;
        searchedAccount.setBalance(currentBalance);
        userRepository.save(searchedAccount);

        TransactionInfo transaction = new TransactionInfo();
        transaction.setType("withdraw");
        transaction.setAmount(amount);
        transaction.setTime(new Date());
        transaction.setFromUsername(searchedAccount.getUsername());
        transaction.setToUsername(searchedAccount.getUsername());

        searchedAccount.addTransaction(transaction);

        transactionRepository.save(transaction);

        return "Successfully withdrawn!";
    }

    public String transfer(String fromUsername, String toUsername, double amount) {
        UserInfo fromSearchedAccount = userRepository.findByUsername(fromUsername)
            .orElse(null);

        if (fromSearchedAccount == null) {
            return "Sender account not found";
        }

        UserInfo toSearchedAccount = userRepository.findByUsername(toUsername)
            .orElse(null);

        if (toSearchedAccount == null) {
            return "Receiver account not found";
        }

        if (fromSearchedAccount.getBalance() < amount) {
            return("Insufficient balance");
        }

        double fromCurrentBalance = fromSearchedAccount.getBalance() - amount;
        fromSearchedAccount.setBalance(fromCurrentBalance);
        userRepository.save(fromSearchedAccount);

        double toCurrentBalance = toSearchedAccount.getBalance() + amount;
        toSearchedAccount.setBalance(toCurrentBalance);
        userRepository.save(toSearchedAccount);


        TransactionInfo senderTransaction = new TransactionInfo();
        senderTransaction.setType("transfer");
        senderTransaction.setAmount(amount);
        senderTransaction.setTime(new Date());
        senderTransaction.setFromUsername(fromSearchedAccount.getUsername());
        senderTransaction.setToUsername(toSearchedAccount.getUsername());

        fromSearchedAccount.addTransaction(senderTransaction);
        transactionRepository.save(senderTransaction);

        TransactionInfo receiverTransaction = new TransactionInfo();
        receiverTransaction.setType("transfer");
        receiverTransaction.setAmount(amount);
        receiverTransaction.setTime(new Date());
        receiverTransaction.setFromUsername(fromSearchedAccount.getUsername());
        receiverTransaction.setToUsername(toSearchedAccount.getUsername());

        
        toSearchedAccount.addTransaction(receiverTransaction);
        transactionRepository.save(receiverTransaction);

        return "Successfully transferred";
    }

    public String deleteUser(String username) {
        UserInfo searchedAccount = userRepository.findByUsername(username)
            .orElse(null);

        if (searchedAccount == null) {
            return "Account not found";
        }

        userRepository.delete(searchedAccount);

        return "Successfully deleted user";
    }
 
}
