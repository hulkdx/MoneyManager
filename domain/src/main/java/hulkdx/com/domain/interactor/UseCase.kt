package hulkdx.com.domain.interactor


import hulkdx.com.domain.executor.PostExecutionThread
import hulkdx.com.domain.executor.ThreadExecutor
import io.reactivex.Flowable
import io.reactivex.annotations.Nullable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subscribers.DisposableSubscriber

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
abstract class UseCase<T>(val mThreadExecutor: ThreadExecutor, val mPostExecutionThread: PostExecutionThread) {

    abstract class FlowableUseCase<T>(mThreadExecutor: ThreadExecutor, mPostExecutionThread: PostExecutionThread) : UseCase<T>(mThreadExecutor, mPostExecutionThread) {

        val mDisposables = CompositeDisposable()

        fun dispose() {
            if (!mDisposables.isDisposed) {
                mDisposables.dispose()
            }
        }

        internal abstract fun createFlowable(): Flowable<T>

        @JvmOverloads
        fun execute(onNext: Consumer<in T>,
                    onError: Consumer<in Throwable>,
                    @Nullable onComplete: Action? = null) {
            val flowable = this.createFlowable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(mPostExecutionThread.scheduler)
            val disposable: Disposable
            if (onComplete == null) {
                disposable = flowable.subscribe(onNext, onError)
            } else {
                disposable = flowable.subscribe(onNext, onError, onComplete)
            }

            mDisposables.add(disposable)
        }
    }

    // Add Single and Completable:
}
