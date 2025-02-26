package dev.ritesh.Banking_v2.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="UserInfo")
public class UserInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private double balance;
    private Date createdAt;

    @OneToMany()
    private List<TransactionInfo> transactions = new ArrayList<TransactionInfo>();

    public void addTransaction(TransactionInfo transaction) {
        this.transactions.add(transaction);
    }

    public List<TransactionInfo> getUserTransactions() {
        return transactions;
    }

}
