package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.web.data.DataHelper;

import ru.netology.web.page.LoginPageV3;

import static com.codeborne.selenide.Selenide.open;
import static ru.netology.web.data.DataHelper.*;

class MoneyTransferTest {

  @BeforeEach
  public void beforeEach() {
    ChromeOptions options = new ChromeOptions();
    Configuration.headless = true;
  }

  @Test
  void shouldTransferMoneyBetweenOwnCardsV3() {
    var loginPage = open("http://localhost:9999", LoginPageV3.class);
    var authInfo = DataHelper.getAuthInfo();
    var verificationPage = loginPage.validLogin(authInfo);
    var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
    var DashboardPage = verificationPage.validVerify(verificationCode);
    var firstCardInfo = geFirstCardInfo();
    var secondCardInfo = getSecondCardInfo();
    var firstCardBalance = DashboardPage.getCardBalance(firstCardInfo);
    var secondCardBalance = DashboardPage.getCardBalance(secondCardInfo);
    var amount = generateValidAmount(firstCardBalance);
    var expectedBalanceFirstCard = firstCardBalance - amount;
    var expectedBalanceSecondCard = secondCardBalance + amount;
    var transferPage = DashboardPage.selectCardTransfer(secondCardInfo);
    DashboardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCardInfo);
    var actualBalanceFirstCard = DashboardPage.getCardBalance(firstCardInfo);
    var actualBalanceSecondCard = DashboardPage.getCardBalance(secondCardInfo);
    assertEquals(expectedBalanceFirstCard, actualBalanceFirstCard);
    assertEquals(expectedBalanceSecondCard, actualBalanceSecondCard);
  }
}

