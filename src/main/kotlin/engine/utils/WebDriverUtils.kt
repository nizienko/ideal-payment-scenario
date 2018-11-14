package engine.utils

import org.openqa.selenium.*

fun WebDriver.saveScreenshot(name: String = "image") = addAttachment(name, data = this.takeScreenshot())
fun WebDriver.takeScreenshot() = (this as TakesScreenshot).getScreenshotAs(OutputType.BYTES)!!
fun WebElement.webDriver(): WebDriver = (this as WrapsDriver).wrappedDriver

/*
    Helps to work with webDriver in its context. In case of fail will attach Screenshot to the report.
 */
inline fun using(webDriver: WebDriver, crossinline function: WebDriver.() -> Unit) {
    try {
        webDriver.function()
    } catch (e: Throwable) {
        webDriver.saveScreenshot("error")
        throw e
    }
}