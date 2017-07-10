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
}
