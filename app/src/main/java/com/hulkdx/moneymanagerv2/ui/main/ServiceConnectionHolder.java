package com.hulkdx.moneymanagerv2.ui.main;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hulkdx.moneymanagerv2.data.SyncService;
import com.hulkdx.moneymanagerv2.data.SyncServiceListener;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 26/12/2017.
 * This is called for SyncService...
 */

public class ServiceConnectionHolder implements ServiceConnection {

    private SyncService mSyncService;
    private boolean mBound = false;
    private SyncServiceListener mListener;


    public void registerListener(SyncServiceListener listener) {
        mListener = listener;
    }

    public void unregisterListener() {
        mListener = null;
        mSyncService = null;
    }

    public boolean isBound() {
        return mBound;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        SyncService.LocalBinder binder = (SyncService.LocalBinder) service;
        mSyncService = binder.getService();
        mSyncService.registerListener(mListener);
        mBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mSyncService.unregisterListener();
        mSyncService = null;
        mBound = false;
    }
}
