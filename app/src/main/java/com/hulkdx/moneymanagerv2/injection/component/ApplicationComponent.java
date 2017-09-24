/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanagerv2.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

import com.hulkdx.moneymanagerv2.data.DataManager;
import com.hulkdx.moneymanagerv2.data.SyncService;
import com.hulkdx.moneymanagerv2.data.local.PreferencesHelper;
import com.hulkdx.moneymanagerv2.data.remote.HulkService;
import com.hulkdx.moneymanagerv2.injection.ApplicationContext;
import com.hulkdx.moneymanagerv2.injection.module.ApplicationModule;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext Context context();
    Application application();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    Realm realm();
    HulkService hulkService();

}
