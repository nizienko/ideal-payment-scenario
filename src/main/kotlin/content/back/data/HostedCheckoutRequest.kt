package content.back.data

import java.math.BigDecimal
import java.math.RoundingMode

data class HostedCheckoutRequest(val order: Order)

data class Order(
    val amountOfMoney: AmountOfMoney,
    val customer: Customer,
    val hostedCheckoutSpecificInput: HostedCheckoutSpecificInput
)

data class AmountOfMoney(val currencyCode: String, val amount: Int)
data class Customer(val merchantCustomerId: Int, val billingAddress: BillingAddress)
data class BillingAddress(val countryCode: String)
data class HostedCheckoutSpecificInput(val variant: String, val locale: String)

fun AmountOfMoney.toUiString(): String =
    "$currencyCode ${BigDecimal(amount).divide(BigDecimal(100), RoundingMode.HALF_UP).setScale(2).toPlainString()}"