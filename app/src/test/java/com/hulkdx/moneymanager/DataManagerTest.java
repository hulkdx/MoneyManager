package com.hulkdx.moneymanager;

import com.hulkdx.moneymanager.common.TestDataFactory;
import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.data.local.DatabaseHelper;
import com.hulkdx.moneymanager.data.local.PreferencesHelper;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.data.model.TransactionResponse;
import com.hulkdx.moneymanager.data.remote.HulkService;
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
        List<Transaction> transactions = Arrays.asList(TestDataFactory.makeTransactionWithoutCategory(1),
                TestDataFactory.makeTransactionWithoutCategory(1));

        TransactionResponse response = new TransactionResponse();
        response.setAmountCount(0);
        response.setResponse(transactions);

        beforeCalls(response);

        TestSubscriber<TransactionResponse> result = new TestSubscriber<>();

        mDataManager.syncTransactions("").subscribe(result);
        result.assertNoErrors();
    }

    @Test
    public void syncTransactionCallsApiAndDatabase() {
        List<Transaction> transactions = Arrays.asList(TestDataFactory.makeTransactionWithoutCategory(1),
                TestDataFactory.makeTransactionWithoutCategory(1));

        TransactionResponse response = new TransactionResponse();
        response.setAmountCount(0);
        response.setResponse(transactions);

        beforeCalls(response);

        mDataManager.syncTransactions("").subscribe();

        verify(mMockService).getTransactions("JWT ");
        verify(mMockDatabaseHelper).addTransactions(response);
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


        when(mMockDatabaseHelper.addTransactions(response))
                .thenReturn(Flowable.just(response));
    }

}
