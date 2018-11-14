package content.front.common

import assertk.Assert
import assertk.assertions.support.fail
import engine.Block
import engine.BlockSettings
import engine.utils.step
import engine.utils.uiStep

class TextFieldBlock(override val settings: BlockSettings) : Block() {
    fun fill(text: String) = webDriver.uiStep("Fill '$name' with text '$text'") {
        with(thisElement) {
            clear()
            sendKeys(text)
        }
    }

    val value: String?
        get() = step("Read '$name' value") {
            return@step thisElement.getAttribute("value")
        }
}

fun Assert<TextFieldBlock>.hasValue(other: String?, ignoreCase: Boolean = false) =
    step("Check that '${actual.name}' has value $other") {
        val actualValue = actual.value
        if (actualValue.equals(other, ignoreCase)) return@step
        fail(other, actualValue)
    }