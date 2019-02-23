package hulkdx.com.domain.executor;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Mohammad Jafarzadeh Rezvan on 10/11/2018.
 */
@Singleton
public class CustomThreadExecutor implements ThreadExecutor {

    private static final int CORE_SIZE     = 3;
    private static final int MAX_CORE_SIZE = 10;

    private final ThreadPoolExecutor mThreadPoolExecutor;
    private final AtomicInteger      mThreadCounter = new AtomicInteger();

    @Inject
    public CustomThreadExecutor() {
        mThreadPoolExecutor = new ThreadPoolExecutor(CORE_SIZE, MAX_CORE_SIZE, 1, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                runnable -> new Thread(runnable, "custom_thread_" + mThreadCounter.getAndIncrement()));
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public void execute(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }
}
