package com.model;
import java.time.LocalDateTime;

public final class Transaction {
	private final String transactionId;
    private final String senderWalletId;
    private final String receiverWalletId;
    private final double amount;
    private final TransactionType type; // CREDIT or DEBIT
    private final LocalDateTime timestamp;
    
    public Transaction(String transactionId, String senderWalletId,
            		   String receiverWalletId, double amount,TransactionType type) {
    	if (transactionId == null || transactionId.trim().isEmpty()) {
            throw new IllegalArgumentException("Transaction ID cannot be null or empty");
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (type == null) {
            throw new IllegalArgumentException("Transaction type cannot be null");
        }
        
        this.transactionId = transactionId;
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }
    
 // Getters ONLY (No setters → Immutable)
    public String getTransactionId() {
        return transactionId;
    }

    public String getSenderWalletId() {
        return senderWalletId;
    }

    public String getReceiverWalletId() {
        return receiverWalletId;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", sender='" + senderWalletId + '\'' +
                ", receiver='" + receiverWalletId + '\'' +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", time=" + timestamp +
                '}';
    }
    
    
}