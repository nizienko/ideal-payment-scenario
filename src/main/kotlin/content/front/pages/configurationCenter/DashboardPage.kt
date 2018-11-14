package content.front.pages.configurationCenter


import org.openqa.selenium.WebDriver

fun <R> WebDriver.dashboardPage(
    function: DashboardPage.() -> R
) = DashboardPage(this).function()

class DashboardPage(webDriver: WebDriver) : ConfigurationCenterPage(webDriver)