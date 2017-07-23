/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/10/2017.
 */
package com.hulkdx.moneymanager.data.local;

import com.hulkdx.moneymanager.data.model.Category;
import com.hulkdx.moneymanager.data.model.Transaction;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

@Singleton
public class DatabaseHelper {

    private final Provider<Realm> mRealmProvider;

    @Inject
    public DatabaseHelper(Provider<Realm> realmProvider) {
        mRealmProvider = realmProvider;
    }

    /*
     * Transactions Section
     */
    public Observable<List<Transaction>> getTransactions(){
        Realm realm = mRealmProvider.get();

        return realm.where(Transaction.class).findAllAsync().asObservable()
                .filter(RealmResults::isLoaded)
                .map(transactions -> transactions);
    }

    public void removeAllTransactions(){
        Realm realm = mRealmProvider.get();

        realm.executeTransaction(realm1 -> realm1.deleteAll());
    }

    public Observable<Transaction> addTransaction(final Transaction newTransaction, final long CategoryId) {
        return Observable.create(subscriber -> {
            Realm realm = null;
            try {
                realm = mRealmProvider.get();
                realm.executeTransactionAsync(
                        bgRealm -> {
                            Number currentIdNum = bgRealm.where(Category.class).max("id");
                            int nextId = currentIdNum == null ? 1 : currentIdNum.intValue() + 1;
                            newTransaction.setId(nextId);
                            Category c = bgRealm.where(Category.class).equalTo("id", CategoryId).findFirst();
                            newTransaction.setCategory(c);
                            bgRealm.copyToRealm(newTransaction);
                        },
                        subscriber::onCompleted,
                        subscriber::onError);
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    /*
     * Category Section
     */
    public Observable<List<Category>> getCategories(){
        Realm realm = mRealmProvider.get();

        return realm.where(Category.class).findAllAsync().asObservable()
                .filter(RealmResults::isLoaded)
                .map(category -> category);
    }

    public Observable<Category> addCategory(final Category newCategory) {
        return Observable.create(subscriber -> {
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
                        subscriber::onCompleted,
                        subscriber::onError);
            }
            finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }
}
