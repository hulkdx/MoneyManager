package com.hulkdx.moneymanagerv2.data.local;

import com.hulkdx.moneymanagerv2.data.model.Transaction;
import com.hulkdx.moneymanagerv2.util.RxUtil;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Provider;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.realm.Realm;
import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 14/01/2018.
 */

class RealmHelper {

    final static BackpressureStrategy DEFAULT_STRATEGY = BackpressureStrategy.LATEST;

    private final Lock lock = new ReentrantLock();
    Provider<Realm> mRealmProvider;


    RealmHelper(Provider<Realm> realmProvider) {
        mRealmProvider = realmProvider;
    }

    <T> Flowable<T> executeTransactionAsync(RealmExecution<T> exec) {
        return executeTransactionAsync(true, true, exec);
    }

    <T> Flowable<T> executeTransactionAsync(boolean onComplete, boolean onError, RealmExecution<T> exec) {
        return Flowable.create(subscriber -> {
            Realm realm = null;
            try {
                lock.lock();
                realm = mRealmProvider.get();
                realm.executeTransactionAsync(bgRealm ->
                        {
                            exec.executeTransactionBlock(bgRealm, subscriber);
                        },
                        () ->
                        {
                            if (onComplete) {
                                subscriber.onComplete();
                            }
                        },
                        error ->
                        {
                            if (onError) {
                                subscriber.onError(error);
                            }
                        }
                );

            } finally {
                if (realm != null) {
                    realm.close();
                }
                lock.unlock();
            }
        }, DEFAULT_STRATEGY);
    }

    <T extends RealmModel> Flowable<List<T>> getData(Class<T> clazz, DoQuery<T> realmQuery) {
        Realm realm = null;
        try {
            lock.lock();
            realm = mRealmProvider.get();

            RealmQuery<T> query = realm.where(clazz);
            RealmResults<T> results = realmQuery.doQuery(query);

            return RxUtil.createFlowableFromRealmResult(realm.getConfiguration(), results)
                    .filter(RealmResults::isLoaded)
                    .map(transactions -> transactions);

        } finally {
            lock.unlock();
        }
    }

    <T extends RealmModel> Flowable<List<T>> findAllAsyncData(Class<T> clazz) {
        return getData(clazz, RealmQuery::findAllAsync);
    }

    public interface RealmExecution<T> {
        void executeTransactionBlock(Realm realm, FlowableEmitter<T> subscriber);
    }

    public interface DoQuery<T> {
        RealmResults<T> doQuery(RealmQuery<T> realmQuery);
    }

}
