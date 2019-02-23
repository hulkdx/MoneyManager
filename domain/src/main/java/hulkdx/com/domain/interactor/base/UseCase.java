package hulkdx.com.domain.interactor.base;


import hulkdx.com.domain.executor.PostExecutionThread;
import hulkdx.com.domain.executor.ThreadExecutor;


@SuppressWarnings("WeakerAccess")
public abstract class UseCase {

    protected final ThreadExecutor mThreadExecutor;
    protected final PostExecutionThread mPostExecutionThread;

    public UseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.mThreadExecutor      = threadExecutor;
        this.mPostExecutionThread = postExecutionThread;
    }
}
