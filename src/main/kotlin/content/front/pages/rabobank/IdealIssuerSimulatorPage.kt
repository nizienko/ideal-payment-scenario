package content.front.pages.rabobank

import content.front.common.ButtonBlock
import content.front.common.TextFieldBlock
import engine.block
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

fun WebDriver.idealIssuerSimulatorPage(function: IdealIssuerSimulatorPage.()->Unit) = IdealIssuerSimulatorPage(this).function()

class IdealIssuerSimulatorPage(webDriver: WebDriver) {
    val merchantReturnUrl = webDriver.block<TextFieldBlock>("Merchant return url") {
        it.findElement(By.name("merchantReturnURL"))
    }
    val confrimTransaction = webDriver.block<ButtonBlock>("Confirm transaction") {
        it.findElement(By.xpath(".//input[@value='Confirm Transaction']"))
    }
}