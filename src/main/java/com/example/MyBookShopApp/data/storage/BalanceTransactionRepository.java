package com.example.MyBookShopApp.data.storage;

import com.example.MyBookShopApp.struct.payments.BalanceTransactionEntity;
import liquibase.pro.packaged.I;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BalanceTransactionRepository extends JpaRepository<BalanceTransactionEntity, Integer> {

    BalanceTransactionEntity getByHash(String hash);

    List<BalanceTransactionEntity> findAllByUserId(int userId);
}
