package engine.utils

import com.google.gson.Gson
import org.apache.logging.log4j.LogManager
import java.time.Duration

val log = LogManager.getLogger("logger")

inline fun waitFor(duration: Duration = Duration.ofSeconds(5), condition: () -> Boolean) {
    if (repeatInTime(duration) { condition() }) return
    throw AssertionError("Exceeded timeout ($duration) for condition function")
}

inline fun repeatInTime(duration: Duration, interval: Duration = Duration.ofMillis(500), condition: () -> Boolean): Boolean {
    val endTime = System.currentTimeMillis() + duration.toMillis()
    while (System.currentTimeMillis() < endTime) {
        if (condition())
            return true
        else {
            Thread.sleep(interval.toMillis())
        }
    }
    return false
}

inline fun <O> tryTimes(tries: Int, onError: () -> Unit = {},
                        finalException: (Throwable) -> Exception,
                        block: (tryNumber: Int) -> O): O {
    var finalError: Throwable? = null
    for (i in 1..tries) {
        try {
            return block(i)
        } catch (e: Throwable) {
            onError()
            finalError = e
            Thread.sleep(500)
        }
    }
    val reason = finalError?.let { finalError } ?: IllegalStateException("Unknown error")

    throw finalException(reason)
}

inline fun <O> attempt(tries: Int = 2, onError: () -> Unit = {}, block: (attemptNumber: Int) -> O): O =
    tryTimes(tries, onError, finalException = { OutOfAttemptsException(it) }, block = block)


class OutOfAttemptsException(e: Throwable) : Exception(e)

inline fun <reified T> Gson.toData(jsonString: String): T = this.fromJson(jsonString, T::class.java)

