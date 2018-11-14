package content.front.pages.hostedCheckOut

import content.front.common.ButtonBlock
import engine.Block
import engine.BlockSettings
import engine.block
import org.openqa.selenium.By

class PaymentProductsBlock(override val settings: BlockSettings) : Block() {
    val unionPay = paymentProduct("UnionPay")
    val boku = paymentProduct("Boku")
    val visaDebit = paymentProduct("Visa Debit")
    val paySafeCard = paymentProduct("paysafecard")
    val masterCardDebit = paymentProduct("MasterCard Debit")
    val sofort = paymentProduct("Sofort")
    val bankTransfer = paymentProduct("Bank Transfer")
    val visa = paymentProduct("Visa")
    val visaElectron = paymentProduct("Visa Electron")
    val americanExpress = paymentProduct("American Express")
    val westernUnion = paymentProduct("Western Union")
    val masterCard = paymentProduct("MasterCard")
    val jcb = paymentProduct("JCB")
    val payPal = paymentProduct("PayPal")
    val webMoney = paymentProduct("WebMoney")
    val skrill = paymentProduct("Skrill")
    val konbini = paymentProduct("Konbini")
    val ideal = paymentProduct("iDEAL")
    val carteBancaire = paymentProduct("Carte Bancaire")
    val dinersClub = paymentProduct("Diners Club")

    fun paymentProduct(name: String) = block<ButtonBlock>("$name payment product") {
        it.findElement(By.xpath(".//li[@data-sortablelisttext = '$name']//button"))
    }
}