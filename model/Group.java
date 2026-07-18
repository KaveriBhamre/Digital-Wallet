package com.model;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class Group {

    private final String groupId;
    private final String groupName;
    private final String creatorWalletId;
    private final Set<String> memberWalletIds;
    private final List<Expense> expenses;

    public Group(String groupId, String groupName, String creatorWalletId) {
        if (groupId == null || groupId.trim().isEmpty()) {
            throw new IllegalArgumentException("Group ID cannot be null or empty");
        }
        if (groupName == null || groupName.trim().isEmpty()) {
            throw new IllegalArgumentException("Group name cannot be null or empty");
        }
        if (creatorWalletId == null || creatorWalletId.trim().isEmpty()) {
            throw new IllegalArgumentException("Creator wallet ID cannot be null or empty");
        }

        this.groupId = groupId;
        this.groupName = groupName;
        this.creatorWalletId = creatorWalletId;
        this.memberWalletIds = new HashSet<>();
        this.expenses = new ArrayList<>();

        // Creator is automatically a member
        this.memberWalletIds.add(creatorWalletId);
    }

    public String getGroupId() { return groupId; }
    public String getGroupName() { return groupName; }
    public String getCreatorWalletId() { return creatorWalletId; }
    public Set<String> getMemberWalletIds() { return memberWalletIds; }
    public List<Expense> getExpenses() { return expenses; }

    public void addMember(String walletId) {
        if (walletId == null || walletId.trim().isEmpty()) {
            throw new IllegalArgumentException("Wallet ID cannot be null or empty");
        }
        if (!memberWalletIds.add(walletId)) {
            throw new IllegalArgumentException("Member already exists in group");
        }
        memberWalletIds.add(walletId);
    }

    public void addExpense(Expense expense) {
        if (expense == null) {
            throw new IllegalArgumentException("Expense cannot be null");
        }
        expenses.add(expense);
    }

    public boolean hasMember(String walletId) {
        return memberWalletIds.contains(walletId);
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupId='" + groupId + '\'' +
                ", groupName='" + groupName + '\'' +
                ", members=" + memberWalletIds.size() +
                ", expenses=" + expenses.size() +
                '}';
    }
    
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (groupId == null ? 0 : groupId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Group other = (Group) obj;

        if (groupId == null) {
            return other.groupId == null;
        }
        return groupId.equals(other.groupId);
    }
}