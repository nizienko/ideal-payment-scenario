package content.front.pages.hostedCheckOut

import content.front.common.ButtonBlock
import content.front.common.TextBlock
import engine.Block
import engine.BlockSettings
import engine.block
import engine.utils.uiStep
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.Select

fun WebDriver.hostedCheckoutPage(function: HostedCheckOutPage.() -> Unit) = HostedCheckOutPage(this).apply(function)

class HostedCheckOutPage(private val webDriver: WebDriver) {
    val shoppingCart = webDriver.block<ShoppingCartBlock>("Shopping cart", cached = false) { sc->
            sc.findElement(By.id("shoppingcart"))
    }

    val paymentProducts = webDriver.block<PaymentProductsBlock>("Payment products") {
        it.findElement(By.id("paymentoptionslist"))
    }

    val paymentOptions = webDriver.block<PaymentOptionsBlock>("Payment options") {
        it.findElement(By.id("paymentoptionswrapper"))
    }

    fun open(url: String) = webDriver.uiStep("Open Hosted Checkout Page") {
        webDriver.get(url)
    }

    class ShoppingCartBlock(override val settings: BlockSettings) : Block() {
        val amount = block<TextBlock>("Order amount", cached = false) { sc ->
            sc.findElements(By.className("amount")).first { it.isDisplayed }
        }
    }

    class PaymentOptionsBlock(override val settings: BlockSettings) : TextBlock(settings) {
        val paymentProductName = block<TextBlock>("Product name") { it.findElement(By.id("paymentProductName"))}

        val payButton = block<ButtonBlock>("Pay") {it.findElement(By.id("primaryButton"))}
        val cacnelButton = block<ButtonBlock>("Cancel") {it.findElement(By.id("secondaryButton"))}

        fun selectBank(name: String) = webDriver.uiStep("Select bank '$name'") {
            Select(thisElement.findElement(By.id("issuerId"))).selectByVisibleText(name)
        }
    }
}


