package dev.ritesh.Banking_v2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.ritesh.Banking_v2.entities.TransactionInfo;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionInfo, Long>{
    
}
