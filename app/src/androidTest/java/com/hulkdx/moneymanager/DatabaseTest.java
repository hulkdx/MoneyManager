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
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

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
}
