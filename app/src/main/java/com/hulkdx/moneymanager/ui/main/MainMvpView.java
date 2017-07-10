/**
 * Created by Mohammad Jafarzadeh Rezvan on 6/13/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.ui.base.MvpView;

import java.util.List;

public interface MainMvpView extends MvpView {

    void showEmptyTransactions();

    void showTransactions(List<Transaction> transactions);
}
