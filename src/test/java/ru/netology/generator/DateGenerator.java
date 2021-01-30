package ru.netology.generator;

import com.github.javafaker.Faker;
import ru.netology.data.RegistrationInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;


public class DateGenerator {
    public DateGenerator() {
    }

    private static LocalDate time = LocalDate.now();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static Random random = new Random();

    public static String calculatedData(int Days, int band) {
        LocalDate newDate = time.plusDays(Days).plusDays(random.nextInt(band));
        return formatter.format(newDate);
    }

    public static String generateInvalidPhone() {
        return "";
    }

    public static String generateInvalidName() {
        Faker faker = new Faker(new Locale("en"));
        return faker.name().fullName();
    }

    public static String generateEmptyName() {
        return "";
    }

    public static String generateInvalidCity() {
        return "NYC";
    }

    public static String generateInvalidDate() {
        return "20.14.4000";
    }

    public static RegistrationInfo randomUser(String Locale) {
        Faker faker = new Faker(new Locale("ru"));
        String[] citiesList = new String[]{"Абакан", "Барнаул", "Великий Новгород", "Горно-Алтайск",
                "Ижевск", "Йошкар-Ола", "Калуга", "Липецк", "Мурманск", "Нижний Новгород", "Омск", "Псков",
                "Рязань", "Самара", "Томск", "Уфа", "Хабаровск", "Челябинск", "Элиста", "Южно-Сахалинск",
                "Якутск"};
        String randomCity = citiesList[random.nextInt(citiesList.length)];
        String dateForTest = calculatedData(3, 180);
        return new RegistrationInfo(randomCity, faker.name().fullName(), faker.phoneNumber().phoneNumber(), dateForTest);
    }
}
