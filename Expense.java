package com.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public final class Expense {

    private final String expenseId;
    private final String groupId;
    private final String description;
    private final double totalAmount;
    private final String paidByWalletId;
    private final List<String> splitAmong;   // includes payer
    private final double splitAmount;         // per person share
    private final LocalDateTime timestamp;

    public Expense(String expenseId, String groupId, String description,
                   double totalAmount, String paidByWalletId, List<String> splitAmong) {

        if (expenseId == null || expenseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense ID cannot be null or empty");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
        if (totalAmount <= 0) {
            throw new IllegalArgumentException("Total amount must be greater than zero");
        }
        if (paidByWalletId == null || paidByWalletId.trim().isEmpty()) {
            throw new IllegalArgumentException("Payer wallet ID cannot be null");
        }
        if (splitAmong == null || splitAmong.isEmpty()) {
            throw new IllegalArgumentException("Split list cannot be empty");
        }
        if (!splitAmong.contains(paidByWalletId)) {
            throw new IllegalArgumentException("Payer must be part of the split");
        }

        this.expenseId = expenseId;
        this.groupId = groupId;
        this.description = description;
        this.totalAmount = totalAmount;
        this.paidByWalletId = paidByWalletId;
        this.splitAmong = Collections.unmodifiableList(splitAmong);
        double rawSplit = totalAmount / splitAmong.size();
        this.splitAmount = Math.round(rawSplit * 100.0) / 100.0;
        this.timestamp = LocalDateTime.now();
    }

    public String getExpenseId() { return expenseId; }
    public String getGroupId() { return groupId; }
    public String getDescription() { return description; }
    public double getTotalAmount() { return totalAmount; }
    public String getPaidByWalletId() { return paidByWalletId; }
    public List<String> getSplitAmong() { return splitAmong; }
    public double getSplitAmount() { return splitAmount; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Expense{" +
                "expenseId='" + expenseId + '\'' +
                ", description='" + description + '\'' +
                ", totalAmount=" + totalAmount +
                ", paidBy='" + paidByWalletId + '\'' +
                ", splitAmong=" + splitAmong.size() + " members" +
                ", eachOwes=" + String.format("%.2f", splitAmount) +
                ", time=" + timestamp +
                '}';
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (expenseId == null ? 0 : expenseId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Expense other = (Expense) obj;

        if (expenseId == null) {
            return other.expenseId == null;
        }
        return expenseId.equals(other.expenseId);
    }
}
