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

        val apiKey =
            step("Task 1: Get API apiKeyId and secretApiKey") {
                loginPage {
                    open()
                    logInWith(loginData)
                }
                dashboardPage {
                    if (menuButton.isDisplayed()) menuButton.click()
                    sideBar.apiKeys.click()
                }
                apiKeyPage { apiKeyTable.readApiKey() }
            }

        val partialUrl =
            step("Task 2: Get Partial-Redirect URL") {
                apiClient(apiKey).hostedCheckoutsRequest(
                    Order(
                        AmountOfMoney("EUR", 100),
                        Customer(113, BillingAddress(countryCode = "NL")),
                        HostedCheckoutSpecificInput("testVariant", "en_GB")
                    )
                ).partialRedirectUrl
            }

        step("Task 3: Make a payment") {
            hostedCheckoutPage {
                open("https://payment.$partialUrl")
                assert(shoppingCart.amount).textIs("EUR 1.00")
                paymentProducts.ideal.click()

                with(paymentOptions) {
                    assert(shoppingCart.amount).textIs("EUR 1.00")
                    assert(paymentProductName).textIs("iDEAL")

                    selectBank("Issuer Simulation V3 - ING")
                    payButton.click()
                }
            }
            idealIssuerSimulatorPage { confrimTransaction.click() }
            hostedCheckoutPage {
                assert(shoppingCart.amount).textIs("EUR 1.00")
                assert(paymentOptions).textIs("Your payment status\nYour payment is successful.")
            }
        }
    }
}