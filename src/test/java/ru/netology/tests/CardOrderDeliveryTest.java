package ru.netology.tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.RegistrationInfo;
import ru.netology.generator.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.generator.DataGenerator.*;

public class CardOrderDeliveryTest {
    @BeforeEach
    void openLink() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldAcceptOrder() {
        RegistrationInfo newUser = DataGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(calculatedDate(3,1));
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать"))
                .click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofMillis(15000))
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на "
                        + calculatedDate(3,1)));
    }

    @Test
    void shouldChangeOrder() {
        RegistrationInfo newUser = DataGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(calculatedDate(3,1));
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать"))
                .click();
        $("[data-test-id='success-notification'] .notification__title")
                .waitUntil(Condition.visible, 15000)
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на "
                        + calculatedDate(3,1)));
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(calculatedDate(10,1));
        $(".grid-row .button__text").click();
        $("[data-test-id='replan-notification'] .notification__title")
                .shouldHave(Condition.exactText("Необходимо подтверждение"));
        $("[data-test-id='replan-notification'] .button__text")
                .click();
        $("[data-test-id=success-notification] .notification__content")
                .shouldBe(Condition.visible, Duration.ofMillis(15000))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на "
                        + calculatedDate(10,1)));
    }

    @Test
    void notAcceptEmptyCity() {
        RegistrationInfo newUser = DataGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(generateEmptyField());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(calculatedDate(5,20));
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать"))
                .click();
        $(".input_invalid .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void cityNotAvailable() {
        RegistrationInfo newUser = DataGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']")
                .setValue(generateNotAvailableCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(calculatedDate(5,6));
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать"))
                .click();
        $(".input_invalid .input__sub")
                .shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void notAcceptInvalidPhone() {
        RegistrationInfo newUser = DataGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(calculatedDate(3,10));
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(generateInvalidPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать"))
                .click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void orderImpossibleOnThisDate() {
        RegistrationInfo newUser = DataGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(calculatedDate(0,1));
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать"))
                .click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void notAcceptValidName() {
        RegistrationInfo newUser = DataGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(calculatedDate(3,1));
        $("[data-test-id='name'] input").setValue(generateProblematicName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать"))
                .click();
        $("[data-test-id='success-notification'] .notification__title")
                .shouldBe(Condition.visible, Duration.ofMillis(15000))
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на "
                        + calculatedDate(3,1)));
    }

    @Test
    void checkboxIsEmpty() {
        RegistrationInfo newUser = DataGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(calculatedDate(6,4));
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать"))
                .click();
        $(".input_invalid[data-test-id='agreement']")
                .shouldHave(Condition.exactText("Я соглашаюсь с условиями обработки и" +
                        " использования моих персональных данных"));
    }
}
