package com.hulkdx.moneymanager.common;

import com.hulkdx.moneymanager.data.model.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 19/09/2017.
 */

public class TestDataFactory {

    public static List<Transaction> makeListTransactions(int number) {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            transactions.add(makeTransactionWithoutCategory(i));
        }
        return transactions;
    }

    public static Transaction makeTransactionWithoutCategory(int i) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Date todayDate = new Date();
        Date plusDate = new Date(todayDate.getTime() + TimeUnit.DAYS.toMillis( i ));
        return new Transaction(dateFormat.format(plusDate), i+1, "");
    }
}
