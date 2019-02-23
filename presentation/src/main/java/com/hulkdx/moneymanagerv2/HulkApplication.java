package com.hulkdx.moneymanagerv2;

import android.app.Application;
import android.content.Context;

import com.hulkdx.moneymanagerv2.injection.component.ApplicationComponent;
import com.hulkdx.moneymanagerv2.injection.component.DaggerApplicationComponent;
import com.hulkdx.moneymanagerv2.injection.module.ApplicationModule;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 6/13/2017.
 * Updated on 23/2/2019.
 */
public class HulkApplication extends Application {
    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        initDagger();
        initLeakDetection();
        initTimberLog();
    }

    private void initDagger() {
        mApplicationComponent =
                DaggerApplicationComponent.builder()
                        .applicationModule(new ApplicationModule(this))
                        .build();
    }

    private void initLeakDetection() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());

            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This process is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
    }

    private void initTimberLog() {
        // This will initialise Timber
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static HulkApplication get(Context context) {
        return (HulkApplication) context.getApplicationContext();
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

}
