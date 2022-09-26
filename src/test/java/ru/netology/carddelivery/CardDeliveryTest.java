package ru.netology.carddelivery;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;

import static com.codeborne.selenide.Selenide.$x;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {
    private String planningDate;

    String dateShift(long daysForShift) {
        return LocalDate.now().plusDays(daysForShift).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

//        Date currentDate = new Date();
//        Long time = currentDate.getTime();
//        time = time + (60 * 60 * 24 * 1000 * daysForShift);
//        currentDate = new Date(time);
//        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
//        return formatter.format(currentDate);
//    }

    @BeforeEach
    void siteOpen() {
        planningDate = dateShift(4);
        open("http://localhost:9999");
    }

    @Test
    void shouldGetSuccessMessageAfterCorrectFilling() {
        Configuration.holdBrowserOpen = false;
        $x("//input[@placeholder=\"Город\"]").setValue("Ростов-на-Дону");
        $x("//input[@placeholder=\"Город\"]").sendKeys(Keys.TAB);
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick();
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder=\"Дата встречи\"]").setValue(planningDate);
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.TAB);
        $x("//*[@name='name']").setValue("Анна-Мария Иванова-Редгрейв");
        $x("//*[@name='phone']").setValue("+89991234567");
        $x("//*[@class=\"checkbox__box\"]").click();
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//div[contains(@class,'notification notification_visible')]/div[text()=\"Успешно!\"]").should(visible, Duration.ofSeconds(15));
        $x("//div[contains(@class,'notification notification_visible')]").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    @Test
    void shouldGetWarningForIncorrectTown() {
        Configuration.holdBrowserOpen = false;
        $x("//input[@placeholder=\"Город\"]").setValue("Ростов-на-Дону-1");
        $x("//input[@placeholder=\"Город\"]").sendKeys(Keys.TAB);
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick();
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder=\"Дата встречи\"]").setValue(planningDate);
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.TAB);
        $x("//*[@name='name']").setValue("Анна-Мария Иванова-Редгрейв");
        $x("//*[@name='phone']").setValue("+89991234567");
        $x("//*[@class=\"checkbox__box\"]").click();
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//span[@data-test-id=\"city\"]//span[@class=\"input__sub\"]").shouldHave(Condition.exactText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldGetWarningForIncorrectDate() {
        Configuration.holdBrowserOpen = false;
        $x("//input[@placeholder=\"Город\"]").setValue("Ростов-на-Дону");
        $x("//input[@placeholder=\"Город\"]").sendKeys(Keys.TAB);
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick();
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder=\"Дата встречи\"]").setValue(dateShift(-1));
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.TAB);
        $x("//*[@name='name']").setValue("Анна-Мария Иванова-Редгрейв");
        $x("//*[@name='phone']").setValue("+89991234567");
        $x("//*[@class=\"checkbox__box\"]").click();
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//span[contains(@class,\"calendar-input\")]//span[@class=\"input__sub\"]").shouldHave(Condition.exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldGetWarningForIncorrectName() {
        Configuration.holdBrowserOpen = false;
        $x("//input[@placeholder=\"Город\"]").setValue("Ростов-на-Дону");
        $x("//input[@placeholder=\"Город\"]").sendKeys(Keys.TAB);
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick();
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder=\"Дата встречи\"]").setValue(planningDate);
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.TAB);
        $x("//*[@name='name']").setValue("Анна Иванова11");
        $x("//*[@name='phone']").setValue("+89991234567");
        $x("//*[@class=\"checkbox__box\"]").click();
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//span[@data-test-id=\"name\"]//span[@class=\"input__sub\"]").shouldHave(Condition.exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldGetWarningForIncorrectPhone() {
        Configuration.holdBrowserOpen = false;
        $x("//input[@placeholder=\"Город\"]").setValue("Ростов-на-Дону");
        $x("//input[@placeholder=\"Город\"]").sendKeys(Keys.TAB);
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick();
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder=\"Дата встречи\"]").setValue(planningDate);
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.TAB);
        $x("//*[@name='name']").setValue("Анна Иванова");
        $x("//*[@name='phone']").setValue("+8999123456*");
        $x("//*[@class=\"checkbox__box\"]").click();
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//span[@data-test-id=\"phone\"]//span[@class=\"input__sub\"]").shouldHave(Condition.exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldGetRedTextIfCheckboxIsntSelect() {
        Configuration.holdBrowserOpen = false;
        $x("//input[@placeholder=\"Город\"]").setValue("Ростов-на-Дону");
        $x("//input[@placeholder=\"Город\"]").sendKeys(Keys.TAB);
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick();
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder=\"Дата встречи\"]").setValue(planningDate);
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.TAB);
        $x("//*[@name='name']").setValue("Анна Иванова");
        $x("//*[@name='phone']").setValue("+89991234567");
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//label[contains(@class, 'checkbox') and contains(@class, 'input_invalid')]").should(enabled);
    }

    @Test
    void shouldGetWarningIfTownIsntFilling() {
        Configuration.holdBrowserOpen = false;
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick();
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder=\"Дата встречи\"]").setValue(planningDate);
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.TAB);
        $x("//*[@name='name']").setValue("Анна-Мария Иванова-Редгрейв");
        $x("//*[@name='phone']").setValue("+89991234567");
        $x("//*[@class=\"checkbox__box\"]").click();
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//span[@data-test-id=\"city\"]//span[@class=\"input__sub\"]").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetWarningIfDateIsntFilling() {
        Configuration.holdBrowserOpen = false;
        $x("//input[@placeholder=\"Город\"]").setValue("Ростов-на-Дону");
        $x("//input[@placeholder=\"Город\"]").sendKeys(Keys.TAB);
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick();
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.TAB);
        $x("//*[@name='name']").setValue("Анна-Мария Иванова-Редгрейв");
        $x("//*[@name='phone']").setValue("+89991234567");
        $x("//*[@class=\"checkbox__box\"]").click();
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//span[contains(@class,\"calendar-input\")]//span[@class=\"input__sub\"]").shouldHave(Condition.exactText("Неверно введена дата"));
    }

    @Test
    void shouldGetWarningIfNameIsntFilling() {
        Configuration.holdBrowserOpen = false;
        $x("//input[@placeholder=\"Город\"]").setValue("Ростов-на-Дону");
        $x("//input[@placeholder=\"Город\"]").sendKeys(Keys.TAB);
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick();
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder=\"Дата встречи\"]").setValue(planningDate);
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.TAB);
        $x("//*[@name='phone']").setValue("+89991234567");
        $x("//*[@class=\"checkbox__box\"]").click();
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//span[@data-test-id=\"name\"]//span[@class=\"input__sub\"]").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldGetWarningIfPhoneIsntFilling() {
        Configuration.holdBrowserOpen = false;
        $x("//input[@placeholder=\"Город\"]").setValue("Ростов-на-Дону");
        $x("//input[@placeholder=\"Город\"]").sendKeys(Keys.TAB);
        $x("//input[@placeholder=\"Дата встречи\"]").doubleClick();
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.BACK_SPACE);
        $x("//input[@placeholder=\"Дата встречи\"]").setValue(planningDate);
        $x("//input[@placeholder=\"Дата встречи\"]").sendKeys(Keys.TAB);
        $x("//*[@name='name']").setValue("Анна Иванова");
        $x("//*[@class=\"checkbox__box\"]").click();
        $x("//div[contains(@class, 'form-field')]/button[@role='button']").click();
        $x("//span[@data-test-id=\"phone\"]//span[@class=\"input__sub\"]").shouldHave(Condition.exactText("Поле обязательно для заполнения"));
    }

}
