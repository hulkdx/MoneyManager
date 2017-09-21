package com.hulkdx.moneymanager;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.hulkdx.moneymanager.data.local.DatabaseHelper;
import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

import static com.hulkdx.moneymanager.common.TestDataFactory.makeListTransactions;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 19/09/2017.
 * Test Realm and DatabaseHelper functions here.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest extends TestCase {

    private Realm mRealm;

    @Before
    public void setUp() throws Exception {
        Realm.init(InstrumentationRegistry.getTargetContext());
        RealmConfiguration testConfig =
                new RealmConfiguration.Builder().
                        inMemory().
                        name("test-realm").build();

        mRealm = Realm.getInstance(testConfig);
    }


    @Test
    public void testAddTransaction(){

        Transaction newTransaction = new Transaction("1999-9-9", 0f, "test");
        int categoryId = -1;
        mRealm.executeTransactionAsync(
                bgRealm -> {
                    bgRealm.deleteAll();

                    if (newTransaction.getId() == 0) {
                        // Auto Incremental Id
                        Number currentIdNum = bgRealm.where(Transaction.class).max("id");
                        int nextId = currentIdNum == null ? 1 : currentIdNum.intValue() + 1;
                        newTransaction.setId(nextId);
                    }
                    if (categoryId != -1) {
                        Category c = bgRealm.where(Category.class).equalTo("id", categoryId).findFirst();
                        newTransaction.setCategory(c);
                    }
                    bgRealm.copyToRealm(newTransaction);

                    // Test the result.
                    Transaction result = mRealm.where(Transaction.class).findFirst();
                    assertEquals(result.getAmount(), 0f);
                    assertEquals(result.getDate(), "1999-9-9");
                    assertEquals(result.getAttachment(), "test");
                });
    }

    @Test
    public void testGetTransactions(){
        mRealm.executeTransactionAsync(
                bgRealm -> {
                    List<Transaction> transactions = makeListTransactions(10);
                    // 1. Add a list of transactions
                    for (Transaction newTransaction: transactions) {
                        // Auto Incremental Id
                        Number currentIdNum = bgRealm.where(Transaction.class).max("id");
                        int nextId = currentIdNum == null ? 1 : currentIdNum.intValue() + 1;
                        newTransaction.setId(nextId);

                        bgRealm.copyToRealm(newTransaction);
                    }


                    // Test the result.
                    RealmResults<Transaction> result = mRealm.where(Transaction.class).findAll();
                    int pos = 0;
                    for (Transaction resultTransaction: result) {
                        assertEquals(resultTransaction.getAmount(), pos);

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date todayDate = new Date();
                        Date plusDate = new Date(todayDate.getTime() + TimeUnit.DAYS.toMillis( pos ));

                        assertEquals(resultTransaction.getDate(), dateFormat.format(plusDate));
                        pos++;
                    }

                });
    }
}
