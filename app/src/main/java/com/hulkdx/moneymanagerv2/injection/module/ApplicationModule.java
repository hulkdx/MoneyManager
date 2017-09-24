/**
 * Created by Mohammad Jafarzadeh Rezvan on 7/6/2017.
 */

package com.hulkdx.moneymanagerv2.injection.module;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import com.hulkdx.moneymanagerv2.data.remote.HulkService;
import com.hulkdx.moneymanagerv2.injection.ApplicationContext;

import javax.inject.Singleton;

/**
 * Provide application-level dependencies.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    RealmConfiguration provideRealmConfiguration(@ApplicationContext Context context) {
        Realm.init(context);
        RealmConfiguration.Builder builder = new RealmConfiguration.Builder();
        builder.deleteRealmIfMigrationNeeded();
        builder.name("realm-db");
        builder.deleteRealmIfMigrationNeeded();
        return builder.build();
    }

    @Provides
    Realm provideRealm(RealmConfiguration realmConfiguration) {
        return Realm.getInstance(realmConfiguration);
    }

    @Provides
    @Singleton
    HulkService provideRecrumoService() {
        return HulkService.Creator.newService();
    }
}
