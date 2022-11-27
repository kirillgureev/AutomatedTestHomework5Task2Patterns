package ru.netology.testmode;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;
import static ru.netology.testmode.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.DataGenerator.Registration.getUser;
import static ru.netology.testmode.DataGenerator.getRandomLogin;
import static ru.netology.testmode.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $x("//*[@data-test-id=\"login\"]//self::input").setValue(registeredUser.getLogin());
        $x("//*[@data-test-id=\"password\"]//self::input").setValue(registeredUser.getPassword());
        $x("//span[text()='Продолжить']").click();
        $x("//*[@id='root']").shouldHave(Condition.text("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $x("//*[@data-test-id=\"login\"]//self::input").setValue(notRegisteredUser.getLogin());
        $x("//*[@data-test-id=\"password\"]//self::input").setValue(notRegisteredUser.getPassword());
        $x("//span[text()='Продолжить']").click();
        $x("//*[@data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(8)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $x("//*[@data-test-id=\"login\"]//self::input").setValue(blockedUser.getLogin());
        $x("//*[@data-test-id=\"password\"]//self::input").setValue(blockedUser.getPassword());
        $x("//span[text()='Продолжить']").click();
        $x("//*[@data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(8)).shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $x("//*[@data-test-id=\"login\"]//self::input").setValue(wrongLogin);
        $x("//*[@data-test-id=\"password\"]//self::input").setValue(registeredUser.getPassword());
        $x("//span[text()='Продолжить']").click();
        $x("//*[@data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(8)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $x("//*[@data-test-id=\"login\"]//self::input").setValue(registeredUser.getLogin());
        $x("//*[@data-test-id=\"password\"]//self::input").setValue(wrongPassword);
        $x("//span[text()='Продолжить']").click();
        $x("//*[@data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(8)).shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));
    }
}
