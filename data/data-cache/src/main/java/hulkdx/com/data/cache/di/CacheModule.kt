package hulkdx.com.data.cache.di

import dagger.Module
import dagger.Provides
import hulkdx.com.data.cache.CacheManagerImpl
import hulkdx.com.domain.data.local.CacheManager

/**
 * Created by Mohammad Jafarzadeh Rezvan on 09/11/2018.
 */
@Module
object CacheModule {
    @Provides
    @JvmStatic
    fun provideCacheManager(cacheManagerImpl: CacheManagerImpl): CacheManager = cacheManagerImpl
}
