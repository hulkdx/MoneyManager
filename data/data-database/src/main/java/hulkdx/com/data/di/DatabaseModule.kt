package hulkdx.com.data.di


import android.content.Context
import dagger.Module
import io.realm.RealmConfiguration
import dagger.Provides
import hulkdx.com.domain.di.ApplicationContext
import io.realm.Realm


/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Module
object DatabaseModule {
    @Provides
    @JvmStatic
    fun provideRealmConfiguration(@ApplicationContext context: Context): RealmConfiguration {
        Realm.init(context)
        return RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name("realm-db")
                .build()
    }
}
