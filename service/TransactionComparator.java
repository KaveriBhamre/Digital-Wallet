package com.service;

import com.model.Transaction;
import java.util.Comparator;

public class TransactionComparator implements Comparator<Transaction> {

    @Override
    public int compare(Transaction t1, Transaction t2) {
        return Double.compare(t2.getAmount(), t1.getAmount()); // descending order
    }
}