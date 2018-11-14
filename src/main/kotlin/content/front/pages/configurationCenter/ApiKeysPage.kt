package content.front.pages.configurationCenter

import content.ApiKey
import engine.Block
import engine.BlockSettings
import engine.block
import engine.utils.attempt
import engine.utils.uiStep
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

fun <R> WebDriver.apiKeyPage(function: ApiKeysPage.() -> R) = ApiKeysPage(this).function()

class ApiKeysPage(webDriver: WebDriver) {
    val apiKeyTable = webDriver.block<ApiKeyTable>("Api key table") { sc ->
        attempt {
            sc.findElement(By.className("table-apikeys"))
        }
    }

    class ApiKeyTable(override val settings: BlockSettings) : Block() {
        private val data: Map<String, String> by lazy {
            thisElement.findElements(By.tagName("tr"))
                .map { it.findElements(By.tagName("td")) }
                .filter { it.size == 2 }
                .map { it[0].text to it[1].text }
                .toMap()
        }

        fun readApiKey(): ApiKey = webDriver.uiStep("Read API key") {
            val id = data["API key ID"] ?: throw AssertionError("API key ID is missed")
            val key = data["Secret API key"] ?: throw AssertionError("Secret API key is missed")
            return@uiStep ApiKey(id, key)
        }
    }
}

