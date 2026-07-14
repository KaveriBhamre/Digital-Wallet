package com.service;

import com.model.*;
import java.util.*;
import java.time.LocalDateTime;

public class WalletService implements IWalletService {
	
	private Map<String, Wallet> walletMap = new HashMap<>();
    private Map<String, List<Transaction>> transactionMap = new HashMap<>();
    private Set<User> users = new HashSet<>();
    private Map<String, Group> groupMap = new HashMap<>();
    // expenseId → list of split records for that expense
    private Map<String, List<SplitRecord>> splitRecordMap = new HashMap<>();
    
 // ─── WALLET ────────────────────────────────────────────────
    
    public void createWallet(User user, String walletId) {
    	if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
    	if (walletId == null || walletId.trim().isEmpty()) {
    	    throw new IllegalArgumentException("Wallet ID cannot be null or empty");
    	}
    	if (walletMap.containsKey(walletId)) {
            throw new IllegalArgumentException("Wallet ID already exists");
        }

    	if (users.contains(user)) {

    	    // Check if same userId but different details
    	    for (User u : users) {
    	        if (u.equals(user)) {  // same userId

    	            if (!u.getName().equals(user.getName()) ||
    	                !u.getPhoneNumber().equals(user.getPhoneNumber())) {

    	                throw new IllegalArgumentException(
    	                    "User ID already exists with different details"
    	                );
    	            }
    	        }
    	    }

    	    System.out.println("User already exists, creating another wallet...");

    	} else {
    	    users.add(user);
    	}
        
        Wallet wallet = new Wallet(walletId, user);
        walletMap.put(walletId, wallet);
        transactionMap.put(walletId, new ArrayList<>());

        System.out.println("Wallet created successfully!");
    }
    
    public void addMoney(String walletId, double amount) {
    	
    	if (amount <= 0) {
    	    throw new IllegalArgumentException("Invalid amount");
    	}
    	
    	Wallet wallet = walletMap.get(walletId);

        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found");
        }

        wallet.addMoney(amount);
        
        Transaction t = new Transaction(
                generateTransactionId(),
                null,
                walletId,
                amount,
                TransactionType.CREDIT
        );
        
        transactionMap.computeIfAbsent(walletId, k -> new ArrayList<>()).add(t);

        System.out.println("Money added successfully!");
    }
    
    public void sendMoney(String fromId, String toId, double amount) {
    	
    	if (amount <= 0) {
    	    throw new IllegalArgumentException("Invalid amount");
    	}
    	
    	if (fromId == null || toId == null) {
    	    throw new IllegalArgumentException("Wallet IDs cannot be null");
    	}

        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Cannot transfer to same wallet");
        }

        Wallet sender = walletMap.get(fromId);
        Wallet receiver = walletMap.get(toId);

        if (sender == null || receiver == null) {
            throw new IllegalArgumentException("Invalid wallet ID");
        }

        if (!sender.hasSufficientBalance(amount)) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        sender.deductMoney(amount);
        receiver.addMoney(amount);

        String txId = generateTransactionId();

        Transaction debit = new Transaction(txId, fromId, toId, amount, TransactionType.DEBIT);
        Transaction credit = new Transaction(txId, fromId, toId, amount, TransactionType.CREDIT);

        transactionMap.computeIfAbsent(fromId, k -> new ArrayList<>()).add(debit);
        transactionMap.computeIfAbsent(toId, k -> new ArrayList<>()).add(credit);

        System.out.println("Transaction successful!");
    }
    
    @Override
    public void viewBalance(String walletId) {

        if (walletId == null || walletId.trim().isEmpty()) {
            throw new IllegalArgumentException("Wallet ID cannot be null or empty");
        }

        Wallet wallet = walletMap.get(walletId);

        if (wallet == null) {
            throw new IllegalArgumentException("Wallet not found");
        }

        System.out.println("Wallet ID: " + walletId);
        System.out.println("User ID  : " + wallet.getUser().getUserId());
        System.out.println("Balance  : ₹" + String.format("%.2f", wallet.getBalance()));
    }
    
    //─── TRANSACTION ────────────────────────────────────────────────
    
    public void viewTransactions(String walletId) {

        List<Transaction> list = transactionMap.get(walletId);

        if (list == null || list.isEmpty()) {
            System.out.println("No transactions found");
            return;
        }

        list.forEach(System.out::println);
    }
    
    public void showLargeTransactions(String walletId, double limit) {

        List<Transaction> list = transactionMap.get(walletId);

        if (list == null || list.isEmpty()) {
            System.out.println("No transactions found");
            return;
        }

        List<Transaction> filtered = list.stream()
                .filter(t -> t.getAmount() > limit)
                .toList();

        if (filtered.isEmpty()) {
            System.out.println("No transactions above given limit");
        } else {
            filtered.forEach(System.out::println);
        }
    }
    
    public void sortTransactionsByAmount(String walletId) {

        List<Transaction> list = transactionMap.get(walletId);
        
        if (list == null || list.isEmpty()) {
            System.out.println("No transactions to sort");
            return;
        }

        list.stream()
        	.sorted(new TransactionComparator())
        	.forEach(System.out::println);


        
    }
    
    public void searchByType(String walletId, TransactionType type) {

        if (walletId == null || walletId.trim().isEmpty()) {
            throw new IllegalArgumentException("Wallet ID cannot be null or empty");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        if (!walletMap.containsKey(walletId)) {
            throw new IllegalArgumentException("Wallet not found");
        }

        List<Transaction> list = transactionMap.get(walletId);

        if (list == null || list.isEmpty()) {
            System.out.println("No transactions found");
            return;
        }

        boolean found = false;
        for (Transaction t : list) {
            if (t.getType() == type) {
                System.out.println(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No " + type + " transactions found for wallet: " + walletId);
        }
    }

    public void searchByDateRange(String walletId, LocalDateTime from, LocalDateTime to) {

        if (walletId == null || walletId.trim().isEmpty()) {
            throw new IllegalArgumentException("Wallet ID cannot be null or empty");
        }
        if (from == null || to == null) {
            throw new IllegalArgumentException("Date range cannot be null");
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' date must not be after 'to' date");
        }
        if (!walletMap.containsKey(walletId)) {
            throw new IllegalArgumentException("Wallet not found");
        }

        List<Transaction> list = transactionMap.get(walletId);

        if (list == null || list.isEmpty()) {
            System.out.println("No transactions found");
            return;
        }

        boolean found = false;
        for (Transaction t : list) {
            LocalDateTime ts = t.getTimestamp();
            if (!ts.isBefore(from) && !ts.isAfter(to)) {
                System.out.println(t);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No transactions found between " + from + " and " + to);
        }
    }

    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }
    
    // ─── GROUP MANAGEMENT ───────────────────────────────────────────────

    public void createGroup(String groupId, String groupName, String creatorWalletId) {

        if (groupId == null || groupId.trim().isEmpty()) {
            throw new IllegalArgumentException("Group ID cannot be null or empty");
        }
        if (groupMap.containsKey(groupId)) {
            throw new IllegalArgumentException("Group ID already exists");
        }
        if (!walletMap.containsKey(creatorWalletId)) {
            throw new IllegalArgumentException("Creator wallet not found");
        }

        Group group = new Group(groupId, groupName, creatorWalletId);
        groupMap.put(groupId, group);

        System.out.println("Group '" + groupName + "' created successfully!");
    }

    public void addMemberToGroup(String groupId, String memberWalletId) {

        Group group = groupMap.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }
        if (!walletMap.containsKey(memberWalletId)) {
            throw new IllegalArgumentException("Wallet not found: " + memberWalletId);
        }

        group.addMember(memberWalletId);
        System.out.println("Member " + memberWalletId + " added to group '" + group.getGroupName() + "'");
    }
    
    @Override
    public void viewUserGroups(String walletId) {

        if (walletId == null || walletId.trim().isEmpty()) {
            throw new IllegalArgumentException("Wallet ID cannot be null or empty");
        }

        if (!walletMap.containsKey(walletId)) {
            throw new IllegalArgumentException("Wallet not found");
        }

        boolean found = false;

        for (Group group : groupMap.values()) {
            if (group.hasMember(walletId)) {
                System.out.println(group);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No groups found for wallet: " + walletId);
        }
    }

    // ─── EXPENSE & SPLIT ────────────────────────────────────────────────

    public void addGroupExpense(String groupId, String paidByWalletId,
                                double totalAmount, String description) {

        if (totalAmount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Group group = groupMap.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }
        if (!walletMap.containsKey(paidByWalletId)) {
            throw new IllegalArgumentException("Payer wallet not found");
        }
        if (!group.hasMember(paidByWalletId)) {
            throw new IllegalArgumentException("Payer is not a member of this group");
        }

        Set<String> members = group.getMemberWalletIds();
        String expenseId = "EXP" + System.currentTimeMillis();

        Expense expense = new Expense(expenseId, groupId, description,
                                    totalAmount, paidByWalletId, new ArrayList<>(members));
        group.addExpense(expense);

        // Create a SplitRecord for every member except the payer
        List<SplitRecord> records = new ArrayList<>();

        for (String memberWalletId : members) {
            if (!memberWalletId.equals(paidByWalletId)) {
                SplitRecord record = new SplitRecord(
                        expenseId, groupId,
                        memberWalletId,      // debtor
                        paidByWalletId,      // creditor
                        expense.getSplitAmount()
                );
                records.add(record);
            }
        }

        splitRecordMap.put(expenseId, records);
        
        System.out.println("Expense added with ID: " + expenseId);

        System.out.println("Expense : '" + description + "' | Total: " + totalAmount);
        
        System.out.printf("Each member owes: %.2f%n", expense.getSplitAmount());
    }

    public void settleExpense(String expenseId, String debtorWalletId) {

        List<SplitRecord> records = splitRecordMap.get(expenseId);
        if (records == null || records.isEmpty()) {
            throw new IllegalArgumentException("No split records found for expense: " + expenseId);
        }

        // Find the specific record for this debtor
        SplitRecord targetRecord = null;
        for (SplitRecord record : records) {
            if (record.getDebtorWalletId().equals(debtorWalletId)) {
                targetRecord = record;
                break;
            }
        }

        if (targetRecord == null) {
            throw new IllegalArgumentException("No pending settlement found for this wallet");
        }
        if (targetRecord.isSettled()) {
            System.out.println("Already settled!");
            return;
        }

        Wallet debtor = walletMap.get(debtorWalletId);
        Wallet creditor = walletMap.get(targetRecord.getCreditorWalletId());

        if (debtor == null || creditor == null) {
            throw new IllegalArgumentException("Wallet not found");
        }
        if (!debtor.hasSufficientBalance(targetRecord.getAmountOwed())) {
            throw new IllegalArgumentException("Insufficient balance to settle");
        }

        // Perform the transfer
        debtor.deductMoney(targetRecord.getAmountOwed());
        creditor.addMoney(targetRecord.getAmountOwed());

        // Record transactions
        String txId = "TXN" + System.currentTimeMillis();
        Transaction debit = new Transaction(txId, debtorWalletId,
                targetRecord.getCreditorWalletId(),
                targetRecord.getAmountOwed(), TransactionType.DEBIT);
        Transaction credit = new Transaction(txId, debtorWalletId,
                targetRecord.getCreditorWalletId(),
                targetRecord.getAmountOwed(), TransactionType.CREDIT);

        transactionMap.computeIfAbsent(debtorWalletId, k -> new ArrayList<>()).add(debit);
        transactionMap.computeIfAbsent(targetRecord.getCreditorWalletId(),
                k -> new ArrayList<>()).add(credit);

        targetRecord.markSettled();

        System.out.printf("Settlement successful! %.2f paid to %s%n",
                targetRecord.getAmountOwed(), targetRecord.getCreditorWalletId());
    }

    // ─── VIEW METHODS ────────────────────────────────────────────────────

    public void viewPendingSettlements(String walletId) {

        if (!walletMap.containsKey(walletId)) {
            throw new IllegalArgumentException("Wallet not found");
        }

        boolean found = splitRecordMap.values().stream()
                .flatMap(List::stream)
                .filter(r -> r.getDebtorWalletId().equals(walletId) && !r.isSettled())
                .peek(System.out::println)
                .findAny()
                .isPresent();

        if (!found) {
            System.out.println("No pending settlements for wallet: " + walletId);
        }
    }

    public void viewGroupSummary(String groupId) {

        Group group = groupMap.get(groupId);
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }

        System.out.println("\n===== GROUP SUMMARY =====");
        System.out.println("Group  : " + group.getGroupName());
        System.out.println("Members: " + group.getMemberWalletIds());
        System.out.println("-------------------------");

        List<Expense> expenses = group.getExpenses();
        if (expenses.isEmpty()) {
            System.out.println("No expenses yet.");
            return;
        }

        for (Expense expense : expenses) {
            System.out.println("\n" + expense);

            List<SplitRecord> records = splitRecordMap.get(expense.getExpenseId());
            if (records != null) {
                for (SplitRecord record : records) {
                    String status = record.isSettled() ? "✔ SETTLED" : "✘ PENDING";
                    System.out.printf("  %s owes %.2f → %s  [%s]%n",
                            record.getDebtorWalletId(),
                            record.getAmountOwed(),
                            record.getCreditorWalletId(),
                            status);
                }
            }
        }
        System.out.println("=========================");
    }
    

}