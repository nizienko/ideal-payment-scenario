package ingenico.acceptance

import content.LoginData
import content.back.apiClient
import content.back.data.*
import content.front.common.textIs
import content.front.pages.configurationCenter.apiKeyPage
import content.front.pages.configurationCenter.dashboardPage
import content.front.pages.configurationCenter.loginPage
import content.front.pages.hostedCheckOut.hostedCheckoutPage
import content.front.pages.rabobank.idealIssuerSimulatorPage
import engine.utils.step
import engine.utils.using
import ingenico.UI
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import assertk.assert

@DisplayName("Hosted Checkout Service")
class HostedCheckoutServiceTest : UI() {


    @Test
    @DisplayName("iDEAL payment")
    fun idealPayment() = using(chromeContainer.webDriver) {
        val loginData = LoginData("nizienko@outlook.com", "pl@giat12S")

        val amount = AmountOfMoney("EUR", 100)
        val customer = Customer(merchantCustomerId = 113, billingAddress = BillingAddress(countryCode = "NL"))
        val hostedCheckoutParameters = HostedCheckoutSpecificInput(variant = "testVariant", locale = "en_GB")

        val apiKey = step("Task 1: Get API apiKeyId and secretApiKey") {
            loginPage {
                open()
                logInWith(loginData)
            }
            dashboardPage { sideBar.apiKeys.click() }
            apiKeyPage { apiKeyTable.readApiKey() }
        }

        val partialUrl = step("Task 2: Get Partial-Redirect URL") {
            apiClient(apiKey).hostedCheckoutsRequest(Order(amount, customer, hostedCheckoutParameters))
                .partialRedirectUrl
        }

        step("Task 3: Make a payment") {
            hostedCheckoutPage {
                open("https://payment.$partialUrl")
                assert(shoppingCart.amount).textIs(amount.toUiString())
                paymentProducts.ideal.click()

                with(paymentOptions) {
                    assert(shoppingCart.amount).textIs(amount.toUiString())
                    assert(paymentProductName).textIs("iDEAL")

                    selectBank("Issuer Simulation V3 - ING")
                    payButton.click()
                }
            }
            idealIssuerSimulatorPage { confrimTransaction.click() }
            hostedCheckoutPage {
                assert(shoppingCart.amount).textIs(amount.toUiString())
                assert(paymentOptions).textIs("Your payment status\nYour payment is successful.")
            }
        }
    }
}