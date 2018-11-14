package content.front.pages.configurationCenter


import content.front.common.ButtonBlock
import engine.Block
import engine.BlockSettings
import engine.block
import org.openqa.selenium.By

class SideBarBlock(override val settings: BlockSettings) : Block() {

    val dashboard = link("Dashboard")
    val merchantIDs = link("Merchant IDs")
    val apiKeys = link("API keys")
    val userManagement = link("User management")

    private fun link(text: String) = block<ButtonBlock>(text, cached = false) {
        it.findElement(By.xpath(".//a[text()='$text']"))
    }
}