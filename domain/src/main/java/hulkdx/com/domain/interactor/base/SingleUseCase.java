package hulkdx.com.domain.interactor.base;

import hulkdx.com.domain.executor.PostExecutionThread;
import hulkdx.com.domain.executor.ThreadExecutor;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;



public abstract class SingleUseCase<T> extends UseCase {

    private final CompositeDisposable mDisposables = new CompositeDisposable();

    public SingleUseCase(ThreadExecutor      threadExecutor,
                         PostExecutionThread postExecutionThread)
    {
        super(threadExecutor, postExecutionThread);
    }

    protected abstract Single<T> create();

    public void execute(Consumer<? super T>         onComplete,
                        Consumer<? super Throwable> onError)
    {
        final Single<T> single = this.create()
                .subscribeOn(Schedulers.from(mThreadExecutor))
                .observeOn(mPostExecutionThread.getScheduler());

        Disposable disposable;

        if (onComplete == null && onError == null) {
            disposable = single.subscribe();
        }
        else if (onComplete == null) {
            throw new UnsupportedOperationException();
        }
        else if (onError == null) {
            throw new UnsupportedOperationException();
        }
        else {
            disposable = single.subscribe(onComplete, onError);
        }

        mDisposables.add(disposable);
    }

    public void dispose() {
        if (!mDisposables.isDisposed()) {
            mDisposables.dispose();
        }
    }
}
