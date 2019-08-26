package ml.wonwoo.todoweb

import org.assertj.core.api.Assertions
import org.assertj.core.api.ThrowableTypeAssert
import org.mockito.Mockito

inline fun <reified T> any(): T = Mockito.any()

inline fun <reified T : Throwable> assertThatExceptionOfType(): ThrowableTypeAssert<T> =
    Assertions.assertThatExceptionOfType(T::class.java)