package hulkdx.com.domain.interactor.base;

import hulkdx.com.domain.executor.PostExecutionThread;
import hulkdx.com.domain.executor.ThreadExecutor;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;



public abstract class FlowableUseCase<T> extends UseCase {

    private final CompositeDisposable mDisposables = new CompositeDisposable();

    public FlowableUseCase(ThreadExecutor      threadExecutor,
                           PostExecutionThread postExecutionThread)
    {
        super(threadExecutor, postExecutionThread);
    }

    protected abstract Flowable<T> create(Object... params);

    public void execute(Consumer<? super T>         onNext,
                        Consumer<? super Throwable> onError,
                        Object...                   params)
    {
        final Flowable<T> single = this.create(params)
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler());

        Disposable disposable = single.subscribe(onNext, onError);

        mDisposables.add(disposable);
    }

    public void dispose() {
        if (!mDisposables.isDisposed()) {
            mDisposables.dispose();
        }
    }
}
