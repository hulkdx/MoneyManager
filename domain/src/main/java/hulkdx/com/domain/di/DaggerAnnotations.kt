package hulkdx.com.domain.di

import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class TutorialScope

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BackgroundScheduler

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class UiScheduler
