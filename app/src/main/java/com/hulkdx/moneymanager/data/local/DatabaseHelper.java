/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/10/2017.
 */
package com.hulkdx.moneymanager.data.local;

import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import com.hulkdx.moneymanager.util.RxUtil;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

@Singleton
public class DatabaseHelper {

    private final Provider<Realm> mRealmProvider;

    @Inject
    public DatabaseHelper(Provider<Realm> realmProvider) {
        mRealmProvider = realmProvider;
    }

    // Helper Function to remove all data
    public void removeAllTransactions(){
        Realm realm = mRealmProvider.get();
        realm.executeTransaction(realm1 -> realm1.deleteAll());
    }

    /************************* Transactions Section *************************/
    public Flowable<List<Transaction>> getTransactions(){
        Realm realm = mRealmProvider.get();
        RealmResults<Transaction> results = realm.where(Transaction.class).findAllAsync();
        return RxUtil.createFlowableFromRealmResult(realm, results)
                .filter(RealmResults::isLoaded)
                .map(transactions -> transactions);
    }

    public Flowable<Transaction> addTransaction(final Transaction newTransaction, final long CategoryId) {
        return Flowable.create(subscriber -> {
            Realm realm = null;
            try {
                realm = mRealmProvider.get();
                realm.executeTransactionAsync(
                        bgRealm -> {
                            Number currentIdNum = bgRealm.where(Transaction.class).max("id");
                            int nextId = currentIdNum == null ? 1 : currentIdNum.intValue() + 1;
                            newTransaction.setId(nextId);
                            Category c = bgRealm.where(Category.class).equalTo("id", CategoryId).findFirst();
                            newTransaction.setCategory(c);
                            bgRealm.copyToRealm(newTransaction);
                        },
                        subscriber::onComplete,
                        subscriber::onError);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }, BackpressureStrategy.LATEST);
    }
    /*
     * Search the db for the specify date.
     * @param day, @param months, @param year is the date to search.
     * @param isDailyOrMonthlyOrYearly: 0 -> daily, 1 -> Monthly, 2 -> yearly.
     */
    public Flowable<List<Transaction>> searchTransactionWithDate(int day, int month, int year,
                                                                 int isDailyOrMonthlyOrYearly) {
        Realm realm = mRealmProvider.get();
        RealmQuery<Transaction> query = realm.where(Transaction.class)
                .equalTo("year", year);
        if (isDailyOrMonthlyOrYearly == 1) { query.equalTo("month", month); }
        if (isDailyOrMonthlyOrYearly == 0) { query.equalTo("day", day); }
        RealmResults<Transaction> results = query.findAllAsync();
        return RxUtil.createFlowableFromRealmResult(realm, results)
                .filter(RealmResults::isLoaded)
                .map(transactions -> transactions);
    }

    /************************* Category Section *************************/
    public Flowable<List<Category>> getCategories(){
        Realm realm = mRealmProvider.get();
        RealmResults<Category> results = realm.where(Category.class).findAllAsync();
        return RxUtil.createFlowableFromRealmResult(realm, results)
                .filter(RealmResults::isLoaded)
                .map(category -> category);
    }

    public Flowable<Category> addCategory(final Category newCategory) {
        return Flowable.create(subscriber -> {
            Realm realm = null;
            try {
                realm = mRealmProvider.get();
                realm.executeTransactionAsync(
                        bgRealm -> {
                            // Auto Increment Id
                            Number currentIdNum = bgRealm.where(Category.class).max("id");
                            int nextId = currentIdNum == null ? 1 : currentIdNum.intValue() + 1;
                            newCategory.setId(nextId);
                            bgRealm.insert(newCategory);
                        },
                        subscriber::onComplete,
                        subscriber::onError);
            }
            finally {
                if (realm != null) {
                    realm.close();
                }
            }
        }, BackpressureStrategy.LATEST);
    }
}
