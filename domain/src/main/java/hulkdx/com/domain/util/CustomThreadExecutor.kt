package hulkdx.com.domain.util

import java.util.concurrent.Executor
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Mohammad Jafarzadeh Rezvan on 2019-05-30.
 */

private const val CORE_SIZE = 3
private const val MAX_CORE_SIZE = 10
private const val KEEP_ALIVE_TIME_SECOND = 1L

@Singleton
class CustomThreadExecutor @Inject constructor() : Executor {

    private val mThreadPoolExecutor: ThreadPoolExecutor
    private val mThreadCounter = AtomicInteger()

    init {
        mThreadPoolExecutor = ThreadPoolExecutor(CORE_SIZE, MAX_CORE_SIZE,
                KEEP_ALIVE_TIME_SECOND, TimeUnit.SECONDS, LinkedBlockingQueue()) {
            runnable -> Thread(runnable, "custom_thread_" + mThreadCounter.getAndIncrement())
        }
    }

    override fun execute(runnable: Runnable) {
        mThreadPoolExecutor.execute(runnable)
    }
}
