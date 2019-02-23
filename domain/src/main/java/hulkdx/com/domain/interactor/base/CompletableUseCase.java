package hulkdx.com.domain.interactor.base;

import hulkdx.com.domain.executor.PostExecutionThread;
import hulkdx.com.domain.executor.ThreadExecutor;
import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;



public abstract class CompletableUseCase extends UseCase {

    private final CompositeDisposable mDisposables = new CompositeDisposable();

    public CompletableUseCase(ThreadExecutor      threadExecutor,
                              PostExecutionThread postExecutionThread)
    {
        super(threadExecutor, postExecutionThread);
    }

    protected abstract Completable create(Object... params);

    public void execute(Action                      onComplete,
                        Consumer<? super Throwable> onError,
                        Object...                   params)
    {
        final Completable completable = this.create(params)
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler());

        Disposable disposable = completable.subscribe(onComplete, onError);
        
        mDisposables.add(disposable);
    }

    public void dispose() {
        if (!mDisposables.isDisposed()) {
            mDisposables.dispose();
        }
    }
}
