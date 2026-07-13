package com.service;

import com.model.User;
import com.model.TransactionType;
import java.time.LocalDateTime;

public interface IWalletService {

    // ─── WALLET MANAGEMENT ───────────────────────────────────────────
    void createWallet(User user, String walletId);

    void addMoney(String walletId, double amount);

    void sendMoney(String fromWalletId, String toWalletId, double amount);
    
    void viewBalance(String walletId);

    // ─── TRANSACTION OPERATIONS ──────────────────────────────────────
    void viewTransactions(String walletId);

    void showLargeTransactions(String walletId, double limit);

    void sortTransactionsByAmount(String walletId);
    
    void searchByType(String walletId, TransactionType type);

    void searchByDateRange(String walletId, LocalDateTime from, LocalDateTime to);


    // ─── GROUP MANAGEMENT ────────────────────────────────────────────
    void createGroup(String groupId, String groupName, String creatorWalletId);

    void addMemberToGroup(String groupId, String memberWalletId);
    
    void viewUserGroups(String walletId);

    // ─── EXPENSE & SPLIT ─────────────────────────────────────────────
    void addGroupExpense(String groupId, String paidByWalletId,
                         double totalAmount, String description);

    void settleExpense(String expenseId, String debtorWalletId);

    // ─── VIEW / REPORTING ────────────────────────────────────────────
    void viewPendingSettlements(String walletId);

    void viewGroupSummary(String groupId);
}
