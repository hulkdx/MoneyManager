package hulkdx.com.domain.interactor.base

import hulkdx.com.domain.executor.PostExecutionThread
import hulkdx.com.domain.executor.ThreadExecutor
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


abstract class SingleUseCase<T>(threadExecutor: ThreadExecutor,
                                postExecutionThread: PostExecutionThread) : UseCase(threadExecutor, postExecutionThread) {

    private val mDisposables = CompositeDisposable()

    protected abstract fun create(): Single<T>

    fun execute(onComplete: Consumer<in T>?,
                onError: Consumer<in Throwable>?) {
        val single = this.create()
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.scheduler)

        val disposable: Disposable

        if (onComplete == null && onError == null) {
            disposable = single.subscribe()
        } else if (onComplete == null) {
            throw UnsupportedOperationException()
        } else if (onError == null) {
            throw UnsupportedOperationException()
        } else {
            disposable = single.subscribe(onComplete, onError)
        }

        mDisposables.add(disposable)
    }

    fun dispose() {
        if (!mDisposables.isDisposed) {
            mDisposables.dispose()
        }
    }
}
