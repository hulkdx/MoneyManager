/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanager.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.data.local.PreferencesHelper;
import com.hulkdx.moneymanager.data.remote.HulkService;
import com.hulkdx.moneymanager.injection.ApplicationContext;
import com.hulkdx.moneymanager.injection.module.ApplicationModule;
import com.hulkdx.moneymanager.util.RxEventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext Context context();
    Application application();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    RxEventBus eventBus();
    Realm realm();
    HulkService hulkService();

}
