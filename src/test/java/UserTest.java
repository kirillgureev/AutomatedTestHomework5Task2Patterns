import com.codeborne.selenide.Condition;
import data.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;
import static data.DataGenerator.Registration.getRegisteredUser;
import static data.DataGenerator.Registration.getUser;
import static data.DataGenerator.*;
import static io.restassured.RestAssured.given;

public class UserTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(withText("Личный кабинет")).shouldBe(Condition.visible);
        //пересмотреть лекцию по selenide
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[name='login']").setValue(notRegisteredUser.getLogin());
        $("[name='password']").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[name='login']").setValue(blockedUser.getLogin());
        $("[name='password']").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();
        $x("//*[contains(text(),'Ошибка!')]").shouldBe(Condition.visible);
        $(".notification__content")
                .shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[name='login']").setValue(wrongLogin);
        $("[name='password']").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(wrongPassword);
        $("[data-test-id='action-login']").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(Condition.visible);
    }
}
