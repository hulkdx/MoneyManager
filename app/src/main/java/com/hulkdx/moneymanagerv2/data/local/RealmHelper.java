package com.hulkdx.moneymanagerv2.data.local;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Provider;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;
import io.realm.RealmChangeListener;
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

    <T> Flowable<T> executeTransaction(RealmExecution<T> exec) {
        return executeTransaction(true, true, exec);
    }

    <T> Flowable<T> executeTransaction(boolean onComplete, boolean onError, RealmExecution<T> exec) {
        return Flowable.create(subscriber -> {
            Realm realm = null;
            try {
                lock.lock();
                realm = mRealmProvider.get();
                try {
                    realm.beginTransaction();
                    exec.executeTransactionBlock(realm, subscriber);
                    realm.commitTransaction();
                } catch (Exception ex) {
                    if (onError) subscriber.onError(ex);
                }
                if (onComplete) subscriber.onComplete();
            } finally {
                if (realm != null) {
                    realm.close();
                }
                lock.unlock();
            }
        }, DEFAULT_STRATEGY);
    }

    <T extends RealmModel> Flowable<List<T>> getData(Class<T> clazz, DoQuery<T> realmQuery) {
        return Flowable.defer( () -> Flowable.create(emitter -> {
            lock.lock();
            Realm realm = mRealmProvider.get();
            lock.unlock();
            try {

                RealmQuery<T> query = realm.where(clazz);
                final RealmResults<T> results = realmQuery.doQuery(query);

                final Realm observableRealm = Realm.getInstance(realm.getConfiguration());

                final RealmChangeListener<RealmResults<T>> listener =
                        result -> {
                            List<T> resz = observableRealm.copyFromRealm(results);
                            emitter.onNext(resz);
                        };
                results.addChangeListener(listener);
                emitter.setDisposable(Disposables.fromRunnable(() -> {
                    results.removeChangeListener(listener);
                    observableRealm.close();
                }));
                emitter.onNext(observableRealm.copyFromRealm(results));
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        }, BackpressureStrategy.LATEST));
    }

    <T extends RealmModel> Flowable<List<T>> findAllData(Class<T> clazz) {
        return getData(clazz, RealmQuery::findAll);
    }

    public interface RealmExecution<T> {
        void executeTransactionBlock(Realm realm, FlowableEmitter<T> subscriber);
    }

    public interface DoQuery<T> {
        RealmResults<T> doQuery(RealmQuery<T> realmQuery);
    }

}
