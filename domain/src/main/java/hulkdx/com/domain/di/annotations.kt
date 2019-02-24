package hulkdx.com.domain.di

import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/02/2019.
 */

//------------
// Qualifiers
//------------

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityContext


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationContext

//------------
// Scopes
//------------

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfigPersistent

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity
