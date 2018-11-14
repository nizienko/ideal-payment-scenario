package engine

import engine.utils.attempt
import engine.utils.uiStep
import engine.utils.webDriver
import org.openqa.selenium.*
import kotlin.NoSuchElementException
import kotlin.reflect.full.primaryConstructor

abstract class Block {
    abstract val settings: BlockSettings

    private var _thisElement: WebElement? = null

    val thisElement: WebElement
        get() = with(settings) {
            return if (cached.not()) {
                attempt {
                    searchFunction(searchContext())
                }
            } else {
                if (_thisElement == null) {
                    _thisElement = attempt { searchFunction(searchContext()) }
                    _thisElement!!
                } else {
                    _thisElement!!
                }
            }
        }

    protected val webDriver: WebDriver
        get() = thisElement.webDriver()

    val name
        get() = settings.name

    fun isDisplayed(): Boolean = webDriver.uiStep("Check whether if '$name' is displayed") {
        return@uiStep thisElement.isDisplayed
    }

    fun click() = webDriver.uiStep("Click at '$name'") {
        thisElement.click()
    }
}

data class BlockSettings(
    val searchContext: () -> SearchContext,
    val searchFunction: (SearchContext) -> WebElement,
    val name: String,
    val cached: Boolean
)

inline fun <reified T : Block> WebDriver.block(
    name: String,
    cached: Boolean = true,
    noinline searchFunction: (SearchContext) -> WebElement
): T {
    return T::class.primaryConstructor?.call(BlockSettings({ this }, searchFunction, name, cached))
        ?: throw IllegalArgumentException("Block must have primary constructor")
}

inline fun <reified T : Block> Block.block(
    name: String,
    cached: Boolean = true,
    noinline searchFunction: (SearchContext) -> WebElement
): T {
    return T::class.primaryConstructor?.call(BlockSettings({ this.thisElement }, searchFunction, name, cached))
        ?: throw IllegalArgumentException("Block must have primary constructor")
}