package content.back.data

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