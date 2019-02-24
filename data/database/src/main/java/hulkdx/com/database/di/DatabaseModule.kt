package hulkdx.com.database.di

import android.content.Context
import dagger.Module
import io.realm.RealmConfiguration
import dagger.Provides
import hulkdx.com.domain.di.ApplicationContext
import io.realm.Realm
import javax.inject.Singleton


/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Module
open class DatabaseModule {
    @Provides
    @Singleton
    fun provideRealmConfiguration(@ApplicationContext context: Context): RealmConfiguration {
        Realm.init(context)
        return RealmConfiguration.Builder()
                                 .deleteRealmIfMigrationNeeded()
                                 .name("realm-db")
                                 .build()
    }

    @Provides
    @Singleton
    fun provideRealm(realmConfiguration: RealmConfiguration): Realm {
        return Realm.getInstance(realmConfiguration)
    }
}
