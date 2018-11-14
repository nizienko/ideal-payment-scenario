package content.front.pages.configurationCenter

import content.front.common.ButtonBlock
import engine.block
import engine.utils.attempt
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

open class ConfigurationCenterPage(webDriver: WebDriver) {
    val menuButton = webDriver.block<ButtonBlock>("Toggle menu button") {
        it.findElement(By.className("toggle-menu-button"))
    }

    val sideBar = webDriver.block<SideBarBlock>("Side bar") { sc ->
        attempt(3) {
            sc.findElement(By.id("sidebar"))
        }
    }
}