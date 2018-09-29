package com.hulkdx.moneymanagerv2.data;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Binder;
import android.os.IBinder;
import com.hulkdx.moneymanagerv2.HulkApplication;
import com.hulkdx.moneymanagerv2.util.NetworkUtil;
import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import timber.log.Timber;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 20/09/2017.
 * Sync Service for syncing transactions and categories.
 */

public class SyncService extends Service {

    @Inject DataManager mDataManager;
    private CompositeDisposable mDisposables;
    private final IBinder mBinder = new LocalBinder();
    private SyncServiceListener mMainActivityListener;

    private boolean mSyncTransactionsComplete = false;
    private boolean mSyncCategoriesComplete = false;

    private boolean isRedirectingScreen;
    private final Boolean LOCK = Boolean.TRUE;

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

        isRedirectingScreen = false;

        if (mDisposables != null) mDisposables.clear();
        mDisposables.add(
                mDataManager
                        .syncTransactions(mDataManager.getPreferencesHelper().getToken())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                transactions -> {
                                    Timber.i("sync Transactions onNext");
                                    stopService(true);
                                },
                                error -> {
                                    handlingRedirectUnauthorized(error);
                                    Timber.e("sync Transactions onError " + error.toString());
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
                                    handlingRedirectUnauthorized(error);
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
        // Avoiding memory leaks
        if (mDisposables != null) mDisposables.clear();
        if (mMainActivityListener != null) mMainActivityListener = null;
        super.onDestroy();
    }

    // To make the code more readable, I added this function to stop the transaction upon checking
    // if sync transaction and sync category is completed.
    // @param isSyncTransaction: if its true then this function runs on syncTransactions
    // otherwise syncCategories
    private void stopService(boolean isSyncTransaction) {
        if (isSyncTransaction) {
            mSyncTransactionsComplete = true;
            if (mSyncCategoriesComplete) {
                Timber.i("sync stopped...");
                stopSelf();
            }
        } else {
            mSyncCategoriesComplete = true;
            if (mSyncTransactionsComplete) {
                Timber.i("sync stopped...");
                stopSelf();
            }
        }
    }

    public class LocalBinder extends Binder {
        public SyncService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SyncService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void handlingRedirectUnauthorized(Throwable error) {
        if (error instanceof HttpException) {
            // TODO maybe change this later to Switch case.
            if (((HttpException) error).code() == 401) {
                // this is here to not make the redirection two times.
                // (calls are called from different thread)
                synchronized (LOCK) {
                    if (isRedirectingScreen) return;
                }
                isRedirectingScreen = true;
                mDataManager.getPreferencesHelper().clear();
                // Tell the MainActivity to finish and start ChooserActivity again...
                if (mMainActivityListener != null) mMainActivityListener.finishAndStartChooser();
            }
        }
    }

    public void registerListener(SyncServiceListener listener) {
        mMainActivityListener = listener;
    }

    public void unregisterListener() {
        mMainActivityListener = null;
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
