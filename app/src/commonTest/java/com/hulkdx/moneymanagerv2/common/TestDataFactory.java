package com.hulkdx.moneymanagerv2.common;

import com.hulkdx.moneymanagerv2.data.model.Transaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
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
        Transaction t = new Transaction(dateFormat.format(plusDate), i + 1, "");
        t.setId(i);
        return t;
    }

    public static Transaction makeRandomTransaction() {
        return new Transaction(makeRandomDate(), makeRandomInt(1, 5000), "");
    }

    private static int makeRandomInt(int min, int max) {
        Random random = new Random();
        return random.nextInt(max-min + 1) + min;
    }

    private static String makeRandomStr(int min, int max) {
        return String.valueOf(makeRandomInt(min, max));
    }

    private static String makeRandomDate() {
        return makeRandomStr(1950, 2100) + "-" + makeRandomStr(1, 12) + "-" + makeRandomStr(1, 30);
    }
}
