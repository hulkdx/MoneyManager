package com.hulkdx.moneymanagerv2;

import androidx.test.InstrumentationRegistry;
import androidx.test.annotation.UiThreadTest;
import androidx.test.rule.UiThreadTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.hulkdx.moneymanagerv2.data.local.DatabaseHelper;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.List;

import javax.inject.Provider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

import static com.hulkdx.moneymanagerv2.common.TestDataFactory.makeListTransactions;
import static org.mockito.Mockito.*;
/**
 * Created by Mohammad Jafarzadeh Rezvan on 19/09/2017.
 * Test Realm and DatabaseHelper functions here.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseTest extends TestCase {

    private Realm mRealm;
    private DatabaseHelper mDatabaseHelper;

    @Rule
    public final UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();


    @Before
    public void setUp() throws Exception {
        Realm.init(InstrumentationRegistry.getTargetContext());
        RealmConfiguration testConfig =
                new RealmConfiguration.Builder().
                        inMemory().
                        name("test-realm").build();

        mRealm = Realm.getInstance(testConfig);
        Provider realmProvider = Mockito.mock(Provider.class);
        mDatabaseHelper = new DatabaseHelper(realmProvider);
        when(realmProvider.get()).thenReturn(mRealm);
    }


    @Test
    @UiThreadTest
    public void testAddTransaction() {

        List<Transaction> transactions = makeListTransactions(10);

        mDatabaseHelper
                .addTransactions(transactions)
                .test()
                .assertSubscribed()
                .assertNoErrors()
                .assertValue(transactions);
    }

    @Test
    @UiThreadTest
    public void testGetTransactionsSubscription() throws Exception {


        List<Transaction> transactions = makeListTransactions(100);
        mRealm.beginTransaction();
        mRealm.copyToRealm(transactions);
        mRealm.commitTransaction();

        mDatabaseHelper
                // Add some first to the memory transactions
                .getTransactions()
                .doOnError(Throwable::printStackTrace)
                .test()
                .assertNoErrors()
                .assertSubscribed()
                .assertValue( (list) -> {
                    boolean result = true;
                    for (int i = 0; i < list.size(); i++) {
                        Transaction expected = list.get(i);
                        Transaction actual   = transactions.get(i);

                        if (expected.getAmount()     != actual.getAmount()     &&
                            !expected.getAttachment().equals(actual.getAttachment()) &&
                            expected.getCategory()   != actual.getCategory()   &&
                            !expected.getDate().equals(actual.getDate()) &&
                            expected.getId()         != actual.getId())
                        {
                            result = false;
                            break;
                        }
                    }
                    return result;
                });
    }


}
