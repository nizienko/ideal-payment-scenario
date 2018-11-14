package content.back.data

data class HostedCheckoutResponse(
    val RETURNMAC: String,
    val hostedCheckoutId: String,
    val partialRedirectUrl: String
)