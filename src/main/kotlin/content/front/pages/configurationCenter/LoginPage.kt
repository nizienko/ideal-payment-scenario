package content.front.pages.configurationCenter

import content.LoginData
import content.front.common.ButtonBlock
import content.front.common.TextFieldBlock
import engine.block
import engine.utils.uiStep
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

fun WebDriver.loginPage(function: LoginPage.() -> Unit) = LoginPage(this).apply(function)

class LoginPage(private val webDriver: WebDriver) {

    val username = webDriver.block<TextFieldBlock>("User name") {
        it.findElement(By.id("username"))
    }

    val password = webDriver.block<TextFieldBlock>("Password") {
        it.findElement(By.id("loginPassword"))
    }

    val logInButton = webDriver.block<ButtonBlock>("LogIn button") {
        it.findElement(By.id("kc-login"))
    }

    fun open() = webDriver.uiStep("Open configuration center") {
        get("https://sandbox.account.ingenico.com")
    }

    fun logInWith(loginData: LoginData) = webDriver.uiStep("Log in with $loginData") {
        username.fill(loginData.userName)
        password.fill(loginData.password)
        logInButton.click()
    }
}

