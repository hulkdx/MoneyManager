package com.hulkdx.moneymanager.data;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import com.hulkdx.moneymanager.HulkApplication;
import com.hulkdx.moneymanager.util.NetworkUtil;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 20/09/2017.
 * Sync Service for syncing transactions and categories.
 */

public class SyncService extends Service {

    @Inject DataManager mDataManager;
    private CompositeDisposable mDisposables;

    private boolean syncTransactionsComplete = false;
    private boolean syncCategoriesComplete = false;

    @Override
    public void onCreate() {
        super.onCreate();
        HulkApplication.get(this).getComponent().inject(this);
        mDisposables = new CompositeDisposable();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Timber.i("Starting sync...");

        if (!NetworkUtil.isNetworkConnected(this)) {
            Timber.i("Sync canceled, connection not available");
            stopSelf(startId);
            return START_NOT_STICKY;
        }


        if (mDisposables != null) mDisposables.clear();
        mDisposables.add(
                mDataManager.syncTransactions(mDataManager.getPreferencesHelper().getToken())
                        .subscribe(
                                transactions -> {
                                    Timber.i("sync Transactions onNext");
                                    mDataManager.getPreferencesHelper().setUserMoney(
                                            transactions.getAmountCount());
                                    stopService(true);
                                },
                                error -> {
                                    Timber.e("sync Transactions onError" + error.toString());
                                    stopService(true);
                                },
                                () -> {
                                    Timber.i("sync Transactions onComplete");
                                    stopService(true);
                                }
                        )
        );

        mDisposables.add(
                mDataManager.syncCategories(mDataManager.getPreferencesHelper().getToken())
                        .subscribe(
                                categories -> {
                                    Timber.i("sync Categories onNext");
                                    stopService(false);
                                },
                                error -> {
                                    Timber.e("sync Categories onError" + error.toString());
                                    stopService(false);
                                },
                                () -> {
                                    Timber.i("sync Categories onComplete");
                                    stopService(false);
                                }
                        )
        );

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mDisposables != null) mDisposables.clear();
        super.onDestroy();
    }

    // To make the code more readable, I added this function to stop the transaction upon checking
    // if sync transaction and sync category is completed.
    // @param isSyncTransaction: if its true then this function runs on syncTransactions
    // otherwise syncCategories
    private void stopService(boolean isSyncTransaction) {
        if (isSyncTransaction) {
            syncTransactionsComplete = true;
            if (syncCategoriesComplete) {
                Timber.i("sync stopped...");
                stopSelf();
            }
        } else {
            syncCategoriesComplete = true;
            if (syncTransactionsComplete) {
                Timber.i("sync stopped...");
                stopSelf();
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * BroadcastReceiver for checking connectivity and it will start the service if it is
     * connected again.
     *
     * This Class is registered (onResume) and unregistered (onPause) in MainActivity.
     */
    public static class CheckConnectivity extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.i("onReceive...");
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Timber.i("Connection is now available, triggering sync...");
                context.startService(new Intent(context, SyncService.class));
            }
        }
    }

}
