/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.data;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import javax.inject.Inject;
import timber.log.Timber;
import com.hulkdx.moneymanager.HulkApplication;
import com.hulkdx.moneymanager.util.AndroidComponentUtil;
import com.hulkdx.moneymanager.util.NetworkUtil;

public class SyncService extends Service {

    @Inject DataManager mDataManager;
//    private Subscription mSubscription;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncService.class);
    }

    public static boolean isRunning(Context context) {
        return AndroidComponentUtil.isServiceRunning(context, SyncService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        HulkApplication.get(this).getComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Timber.i("Starting sync...");

        if (!NetworkUtil.isNetworkConnected(this)) {
            Timber.i("Sync canceled, connection not available");
            AndroidComponentUtil.toggleComponent(this, SyncOnConnectionAvailable.class, true);
            stopSelf(startId);
            return START_NOT_STICKY;
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
//        if (mSubscription != null) mSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static class SyncOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtil.isNetworkConnected(context)) {
                Timber.i("Connection is now available, triggering sync...");
                AndroidComponentUtil.toggleComponent(context, this.getClass(), false);
                context.startService(getStartIntent(context));
            }
        }
    }

}