package hulkdx.com.data.database.di


import android.content.Context
import dagger.Module
import io.realm.RealmConfiguration
import dagger.Provides
import hulkdx.com.data.database.DatabaseManagerImpl
import hulkdx.com.domain.data.local.DatabaseManager
import hulkdx.com.domain.di.ApplicationContext
import io.realm.Realm
import javax.inject.Singleton


/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Module
object DatabaseModule {
    @Provides
    @Singleton
    @JvmStatic
    fun provideRealmConfiguration(@ApplicationContext context: Context): RealmConfiguration {
        Realm.init(context)
        return RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name("realm-db")
                .build()
    }
    @Provides
    @JvmStatic
    fun provideDatabaseManager(databaseManagerImpl: DatabaseManagerImpl): DatabaseManager = databaseManagerImpl
}
