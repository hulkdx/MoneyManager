package hulkdx.com.data.remote

import org.mockito.ArgumentCaptor
import org.mockito.Mockito

/**
 * Created by Mohammad Jafarzadeh Rezvan on 06/07/2019.
 *
 * TODO have a single util across all test module.
 */
fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
fun <T> anyKotlin(): T {
    Mockito.any<T>()
    return uninitialized()
}

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T