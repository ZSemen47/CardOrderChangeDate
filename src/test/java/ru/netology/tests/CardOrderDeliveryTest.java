package ru.netology.tests;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.RegistrationInfo;
import ru.netology.generator.DateGenerator;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.generator.DateGenerator.*;

public class CardOrderDeliveryTest {
    @BeforeEach
    void openLink() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldAcceptOrder() {
        RegistrationInfo newUser = DateGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__title")
                .waitUntil(Condition.visible, 15000)
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + newUser.getDate()));
    }

    @Test
    void shouldChangeOrder() {
        RegistrationInfo newUser = DateGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать")).click();
        $("[data-test-id='success-notification'] .notification__title")
                .waitUntil(Condition.visible, 15000)
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + newUser.getDate()));
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $(".grid-row .button__text").click();
        $("[data-test-id='replan-notification'] .notification__title")
                .shouldHave(Condition.exactText("Необходимо подтверждение"));
        $("[data-test-id='replan-notification'] .button__text")
                .click();
        $("[data-test-id=success-notification] .notification__content")
                .waitUntil(Condition.visible, 15000)
                .shouldHave(Condition.exactText("Встреча успешно запланирована на "
                        + newUser.getDate()));
    }

    @Test
    void emptyCity() {
        RegistrationInfo newUser = DateGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(generateEmptyName());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать")).click();
        $(".input_invalid .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void cityNotAvailable() {
        RegistrationInfo newUser = DateGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(generateInvalidCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать")).click();
        $(".input_invalid .input__sub")
                .shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void notAcceptInvalidPhone() {
        RegistrationInfo newUser = DateGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(generateInvalidPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать")).click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void acceptInvalidPhone() {
        RegistrationInfo newUser = DateGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue("+8800");
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать")).click();
        $("[data-test-id='phone'] .input__sub")
                .shouldHave(Condition.exactText("На указанный номер моб. тел. будет отправлен смс-код " +
                        "для подтверждения заявки на карту. Проверьте, что номер ваш и введен " +
                        "корректно."));
        $("[data-test-id='success-notification'] .notification__title")
                .waitUntil(Condition.visible, 15000)
                .shouldHave(Condition.exactText("Успешно!"));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + newUser.getDate()));
    }

    @Test
    void dateInvalid() {
        RegistrationInfo newUser = DateGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать")).click();
        $("[data-test-id='date'] .input_invalid .input__sub")
                .shouldHave(Condition.exactText("Неверно введена дата"));
    }

    @Test
    void notAcceptValideName() {
        RegistrationInfo newUser = DateGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $("[data-test-id='name'] input").setValue("АлЁна");
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. " +
                        "Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void notAcceptInvalidName() {
        RegistrationInfo newUser = DateGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $("[data-test-id='name'] input").setValue(generateInvalidName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".checkbox__box").click();
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать")).click();
        $("[data-test-id='name'] .input__sub")
                .shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. " +
                        "Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void checkboxIsEmpty() {
        RegistrationInfo newUser = DateGenerator.randomUser("ru");
        $("[data-test-id='city'] [placeholder='Город']").setValue(newUser.getCity());
        $("[data-test-id='date'] [placeholder='Дата встречи']")
                .doubleClick().sendKeys(newUser.getDate());
        $("[data-test-id='name'] input").setValue(newUser.getName());
        $("[data-test-id='phone'] input").setValue(newUser.getPhone());
        $(".grid-row .button__text").shouldHave(Condition.exactText("Запланировать")).click();
        $(".input_invalid[data-test-id='agreement']")
                .shouldHave(Condition.exactText("Я соглашаюсь с условиями обработки и использования моих" +
                        " персональных данных"));
    }
}
