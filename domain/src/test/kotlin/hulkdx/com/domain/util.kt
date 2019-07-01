package hulkdx.com.domain

import org.mockito.ArgumentCaptor
import org.mockito.Mockito


fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
fun <T> anyKotlin(): T {
    Mockito.any<T>()
    return uninitialized()
}

@Suppress("UNCHECKED_CAST")
private fun <T> uninitialized(): T = null as T