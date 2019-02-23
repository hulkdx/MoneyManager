/**
 * Created by Mohammad Jafarzadeh Rezvan on 6/13/2017.
 */

package com.hulkdx.moneymanagerv2.ui.main;

import com.hulkdx.moneymanagerv2.data.model.Category;
import com.hulkdx.moneymanagerv2.data.model.Transaction;
import com.hulkdx.moneymanagerv2.ui.base.MvpView;

import java.util.List;

public interface MainMvpView extends MvpView {

    void showTransactions(List<Transaction> transactions);

    void setBalanceTextView(float amount);

    void showCategories(List<Category> categories);

    void addCategoryCompleted();

    void showError(String functionName, Throwable error);

    void onCompleteAddTransactions();

    void showErrorDeleteTransactions();

    void deleteTransactionsComplete(boolean isEmpty);

    void updateTransactions();
}
