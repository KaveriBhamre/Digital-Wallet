package com.model;

public class SplitRecord {

    private final String expenseId;
    private final String groupId;
    private final String debtorWalletId;    // who owes
    private final String creditorWalletId;  // who paid (gets money back)
    private final double amountOwed;
    private boolean settled;

    public SplitRecord(String expenseId, String groupId,
                       String debtorWalletId, String creditorWalletId,
                       double amountOwed) {

        if (expenseId == null || expenseId.trim().isEmpty()) {
            throw new IllegalArgumentException("Expense ID cannot be null");
        }
        if (debtorWalletId == null || creditorWalletId == null) {
            throw new IllegalArgumentException("Wallet IDs cannot be null");
        }
        if (debtorWalletId.equals(creditorWalletId)) {
            throw new IllegalArgumentException("Debtor and creditor cannot be the same");
        }
        if (amountOwed <= 0) {
            throw new IllegalArgumentException("Amount owed must be greater than zero");
        }

        this.expenseId = expenseId;
        this.groupId = groupId;
        this.debtorWalletId = debtorWalletId;
        this.creditorWalletId = creditorWalletId;
        this.amountOwed = amountOwed;
        this.settled = false;
    }

    public String getExpenseId() { return expenseId; }
    public String getGroupId() { return groupId; }
    public String getDebtorWalletId() { return debtorWalletId; }
    public String getCreditorWalletId() { return creditorWalletId; }
    public double getAmountOwed() { return amountOwed; }
    public boolean isSettled() { return settled; }

    public void markSettled() {
        this.settled = true;
    }

    @Override
    public String toString() {
        return "SplitRecord{" +
                "expenseId='" + expenseId + '\'' +
                ", owedBy='" + debtorWalletId + '\'' +
                ", owedTo='" + creditorWalletId + '\'' +
                ", amount=" + String.format("%.2f", amountOwed) +
                ", settled=" + settled +
                '}';
    }
}
