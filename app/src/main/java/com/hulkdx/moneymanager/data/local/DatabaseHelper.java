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
                .filter(new Func1<RealmResults<Transaction>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<Transaction> transactions) {
                        return transactions.isLoaded();
                    }
                })
                .map(new Func1<RealmResults<Transaction>, List<Transaction>>() {
                    @Override
                    public List<Transaction> call(RealmResults<Transaction> transactions) {
                        return transactions;
                    }
                });
    }

    public void removeAllTransactions(){
        Realm realm = mRealmProvider.get();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    public Observable<Transaction> addTransaction(final Transaction newTransaction) {
        return Observable.create(new Observable.OnSubscribe<Transaction>() {
            @Override
            public void call(final Subscriber<? super Transaction> subscriber) {
                Realm realm = null;
                try {
                    realm = mRealmProvider.get();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            bgRealm.copyToRealm(newTransaction);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            subscriber.onCompleted();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            subscriber.onError(error);
                        }
                    });
                } finally {
                    if (realm != null) {
                        realm.close();
                    }
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
                .filter(new Func1<RealmResults<Category>, Boolean>() {
                    @Override
                    public Boolean call(RealmResults<Category> category) {
                        return category.isLoaded();
                    }
                })
                .map(new Func1<RealmResults<Category>, List<Category>>() {
                    @Override
                    public List<Category> call(RealmResults<Category> category) {
                        return category;
                    }
                });
    }

    public Observable<Category> addCategory(final Category newCategory) {
        return Observable.create(new Observable.OnSubscribe<Category>() {
            @Override
            public void call(final Subscriber<? super Category> subscriber) {
                Realm realm = null;
                try {
                    realm = mRealmProvider.get();
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            bgRealm.copyToRealm(newCategory);
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            subscriber.onCompleted();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            subscriber.onError(error);
                        }
                    });
                }
                finally {
                    if (realm != null) {
                        realm.close();
                    }
                }
            }
        });
    }
}
