/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/10/2017.
 */
package com.hulkdx.moneymanager.data.local;

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
                            // Transaction was a success.
                            subscriber.onCompleted();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            // Transaction failed and was automatically canceled.
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
}
