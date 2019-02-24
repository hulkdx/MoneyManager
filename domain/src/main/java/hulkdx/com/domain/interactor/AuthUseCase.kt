package hulkdx.com.domain.interactor

import hulkdx.com.domain.executor.PostExecutionThread
import hulkdx.com.domain.executor.ThreadExecutor
import hulkdx.com.domain.models.User
import hulkdx.com.domain.repository.IUserRepository
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 23/02/2019.
 */
@Singleton
class AuthUseCase @Inject constructor(private val mUserRepository: IUserRepository,
                                      private val threadExecutor: ThreadExecutor,
                                      private val postExecutionThread: PostExecutionThread) {

    private var disposable: Disposable? = null

    fun isLoggedInSync(): Boolean {
        val user = mUserRepository.getUser()
        return user != null
    }

    fun isLoggedInAsync(onNext: Consumer<Boolean>) {
        disposable = Single
                .fromCallable { isLoggedInSync() }
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.scheduler)
                .subscribe(onNext)
    }

    fun dispose() {
        disposable?.apply {
            if (!isDisposed) dispose()
        }
    }

}