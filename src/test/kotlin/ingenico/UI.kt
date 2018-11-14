package ingenico

import org.openqa.selenium.remote.DesiredCapabilities
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
abstract class UI {
    @Container
    protected val chromeContainer = KBrowserWebDriverContainer()
        .withDesiredCapabilities(DesiredCapabilities.chrome())!!

    init {
        System.setProperty("webdriver.chrome.driver", "chromedriver")
    }
}

class KBrowserWebDriverContainer : BrowserWebDriverContainer<KBrowserWebDriverContainer>()
