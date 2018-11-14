package content.front.common

import assertk.Assert
import assertk.assertions.support.fail
import engine.Block
import engine.BlockSettings
import engine.utils.step

open class TextBlock(override val settings: BlockSettings) : Block() {
    fun text(): String = thisElement.text
}

fun Assert<TextBlock>.textIs(other: String?, ignoreCase: Boolean = false) =
    step("Check that '${actual.name}' text is '$other'") {
        val actualText = actual.text()
        if (actualText.equals(other, ignoreCase)) return@step
        fail(other, actualText)
    }