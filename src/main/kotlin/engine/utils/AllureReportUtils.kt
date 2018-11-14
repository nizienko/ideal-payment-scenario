package engine.utils

import io.qameta.allure.Allure
import io.qameta.allure.model.Status
import io.qameta.allure.model.StepResult
import io.qameta.allure.util.ResultsUtils
import org.openqa.selenium.WebDriver
import java.util.*

object allure {
    fun startStep(uuid: UUID, title: String) =
        Allure.getLifecycle().startStep(uuid.toString(), StepResult().withName(title))

    fun passed(uuid: UUID) = Allure.getLifecycle().updateStep(uuid.toString()) { it.withStatus(Status.PASSED) }
    fun failed(uuid: UUID, e: Throwable) = Allure.getLifecycle().updateStep(uuid.toString()) {
        it.withStatus(ResultsUtils.getStatus(e).orElseGet { Status.FAILED })
            .withStatusDetails(ResultsUtils.getStatusDetails(e).orElseGet { null })
    }

    fun stop(uuid: UUID) = Allure.getLifecycle().stopStep(uuid.toString())
}

inline fun <O> step(text: String, crossinline block: () -> O): O {
    val uuid = UUID.randomUUID()
    allure.startStep(uuid, text)
    try {
        val stepResult = block()
        allure.passed(uuid)
        return stepResult
    } catch (e: Throwable) {
        allure.failed(uuid, e)
        throw e
    } finally {
        allure.stop(uuid)
    }
}

inline fun <W> WebDriver.uiStep(text: String, crossinline block: WebDriver.() -> W): W {
    val uuid = UUID.randomUUID()
    allure.startStep(uuid, text)
    try {
        val stepResult = block()
        allure.passed(uuid)
        return stepResult
    } catch (e: Throwable) {
        allure.failed(uuid, e)
        throw e
    } finally {
        this.saveScreenshot("step result")
        allure.stop(uuid)
    }
}

fun addAttachment(name: String = "Screenshot", text: String) {
    addAttachment("response", "text/html", text.toByteArray())
}

fun addAttachment(name: String = "Screenshot", type: String = "image/png", data: ByteArray) {
    val extension = when (type) {
        "image/png" -> "png"
        "text/html" -> "txt"
        else -> ""
    }
    Allure.getLifecycle().addAttachment(name, type, extension, data)
}

