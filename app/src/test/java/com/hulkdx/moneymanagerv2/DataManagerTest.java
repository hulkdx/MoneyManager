package com.hulkdx.moneymanagerv2;

import com.hulkdx.moneymanagerv2.common.TestDataFactory;
import com.hulkdx.moneymanagerv2.data.DataManager;
import com.hulkdx.moneymanagerv2.data.local.DatabaseHelper;
import com.hulkdx.moneymanagerv2.data.local.PreferencesHelper;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import com.hulkdx.moneymanagerv2.data.model.TransactionResponse;
import com.hulkdx.moneymanagerv2.data.remote.HulkService;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Arrays;
import java.util.List;
import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 20/09/2017.
 * DataManager Tests
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock DatabaseHelper mMockDatabaseHelper;
    @Mock PreferencesHelper mMockPreferencesHelper;
    @Mock HulkService mMockService;
    private DataManager mDataManager;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();


    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockPreferencesHelper, mMockDatabaseHelper,
                mMockService);
    }

    @Test
    public void syncTransactionEmits() {
        List<Transaction> transactions = Arrays.asList(
                TestDataFactory.makeTransactionWithoutCategory(1),
                TestDataFactory.makeTransactionWithoutCategory(1));

        TransactionResponse response = new TransactionResponse();
        response.setAmountCount(0);
        response.setTransactions(transactions);

        beforeCalls(response);

        TestSubscriber<List<Transaction>> result = new TestSubscriber<>();

        mDataManager
                .syncTransactions("")
                .subscribe(result);
        result.assertNoErrors();
    }

    @Test
    public void syncTransactionCallsApiAndDatabase() {
        List<Transaction> transactions = Arrays.asList(
                TestDataFactory.makeTransactionWithoutCategory(1),
                TestDataFactory.makeTransactionWithoutCategory(1));

        TransactionResponse response = new TransactionResponse();
        response.setAmountCount(0);
        response.setTransactions(transactions);

        beforeCalls(response);

        mDataManager.syncTransactions("").subscribe();

        verify(mMockService).getTransactions("JWT ");
        verify(mMockDatabaseHelper).addTransactions(response.getTransactions());
    }

    @Test
    public void syncTransactionDoesNotCallDatabaseWhenApiFails() {
        when(mMockService.getTransactions("JWT "))
                .thenReturn(Flowable.error(new RuntimeException()));

        mDataManager.syncTransactions("").subscribe(new TestSubscriber<>());
        // Verify right calls to helper methods
        verify(mMockService).getTransactions("JWT ");
        verify(mMockDatabaseHelper, never()).addTransactions(ArgumentMatchers.any());
    }

    private void beforeCalls(TransactionResponse response) {

        // Stub calls to the ribot service and database helper.
        when(mMockService.getTransactions("JWT "))
                .thenReturn(Flowable.just(response));


        when(mMockDatabaseHelper.addTransactions(response.getTransactions()))
                .thenReturn(Flowable.just(response.getTransactions()));
    }

}
