/**
 * Created by Mohammad Jafarzadeh Rezvan on 6/13/2017.
 */

package com.hulkdx.moneymanager.ui.main;

import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.ui.base.MvpView;

import java.util.List;

public interface MainMvpView extends MvpView {

    void showEmptyTransactions(List<Transaction> transactions);

    void showTransactions(List<Transaction> transactions);

    void setBalanceTextView(float amount);

    void showCategories(List<Category> categories);

    void addCategoryDataSet(Category newCategory);

    void showError(String functionName, Throwable error);

    void onCompleteAddTransations();
}
