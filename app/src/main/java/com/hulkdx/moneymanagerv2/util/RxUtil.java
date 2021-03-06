package com.hulkdx.moneymanagerv2.util;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposables;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class RxUtil {

    /**
     *  Create Observable because realm doesn't work with Rxjava 2 from source code.
     *
     *  @link : https://github.com/realm/realm-java/blob/master/realm/realm-library/src/main/java/io/realm/rx/RealmObservableFactory.java
     */
    public static <E extends RealmModel> Flowable<RealmResults<E>> createFlowableFromRealmResult(
            final RealmConfiguration RealmConfiguration, final RealmResults<E> results) {

// TODO       final RealmConfiguration realmConfig = realm.getConfiguration();
        return Flowable.create(emitter -> {
            // Gets instance to make sure that the Realm is open for as long as the
            // Observable is subscribed to it.
            final Realm observableRealm = Realm.getInstance(RealmConfiguration);
            final RealmChangeListener<RealmResults<E>> listener = result -> emitter.onNext(results);
            results.addChangeListener(listener);
            emitter.setDisposable(Disposables.fromRunnable(() -> {
                results.removeChangeListener(listener);
                observableRealm.close();
            }));
            emitter.onNext(results);
        }, BackpressureStrategy.LATEST);
    }
}
