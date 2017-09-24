package com.hulkdx.moneymanagerv2;

import com.hulkdx.moneymanagerv2.common.TestDataFactory;
import com.hulkdx.moneymanagerv2.data.DataManager;
import com.hulkdx.moneymanagerv2.data.local.PreferencesHelper;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import com.hulkdx.moneymanagerv2.ui.main.MainMvpView;
import com.hulkdx.moneymanagerv2.ui.main.MainPresenter;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import io.reactivex.Flowable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 20/09/2017.
 * MainPresenter Tests
 */

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock MainMvpView mMockMainMvpView;
    @Mock DataManager mMockDataManager;
    @Mock PreferencesHelper mMockPreferencesHelper;
    private MainPresenter mMainPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mMainPresenter = new MainPresenter(mMockDataManager);
        mMainPresenter.attachView(mMockMainMvpView);

        when(mMockDataManager.getPreferencesHelper())
                .thenReturn(mMockPreferencesHelper);
    }

    @After
    public void tearDown() {
        mMainPresenter.detachView();
    }

    @Test
    public void loadTransactionReturnsTransaction() {
        List<Transaction> transactions = TestDataFactory.makeListTransactions(10);

        when(mMockDataManager.getTransactions())
                .thenReturn(Flowable.just(transactions));

        mMainPresenter.loadTransactions();
        verify(mMockMainMvpView).showTransactions(transactions);
    }

    @Test
    public void loadTransactionReturnsEmptyList() {
        when(mMockDataManager.getTransactions())
                .thenReturn(Flowable.just(Collections.emptyList()));

        mMainPresenter.loadTransactions();
        verify(mMockMainMvpView).showEmptyTransactions(new ArrayList<>());
        verify(mMockMainMvpView, never()).showTransactions(ArgumentMatchers.anyList());
        verify(mMockMainMvpView, never()).showError(
                ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    public void loadTransactionFails() {
        RuntimeException throwable = new RuntimeException();
        when(mMockDataManager.getTransactions())
                .thenReturn(Flowable.error(throwable));

        mMainPresenter.loadTransactions();
        verify(mMockMainMvpView).showError("loadTransactions", throwable);
        verify(mMockMainMvpView, never()).showEmptyTransactions(new ArrayList<>());
        verify(mMockMainMvpView, never()).showTransactions(ArgumentMatchers.anyList());
    }

}
