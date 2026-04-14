package com.service;

import com.model.User;

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
