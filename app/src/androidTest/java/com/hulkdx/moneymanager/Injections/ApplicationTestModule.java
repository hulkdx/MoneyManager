package com.hulkdx.moneymanager.injections;

import android.app.Application;
import android.content.Context;
import com.hulkdx.moneymanager.data.DataManager;
import com.hulkdx.moneymanager.data.local.PreferencesHelper;
import com.hulkdx.moneymanager.data.remote.HulkService;
import com.hulkdx.moneymanager.injection.ApplicationContext;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

import static org.mockito.Mockito.mock;


@Module
public class ApplicationTestModule {

    private final Application mApplication;

    public ApplicationTestModule(Application application) {
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

    /************* MOCKS *************/

    @Provides
    @Singleton
    DataManager provideDataManager() {
        return mock(DataManager.class);
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper() {
        return mock(PreferencesHelper.class);
    }

    @Provides
    @Singleton
    HulkService provideHulkService() {
        return mock(HulkService.class);
    }

    @Provides
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

}
