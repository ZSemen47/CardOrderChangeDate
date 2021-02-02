package ru.netology.generator;

import com.github.javafaker.Faker;
import ru.netology.data.RegistrationInfo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;


public class DataGenerator {
    private DataGenerator() {
    }

    private static LocalDate time = LocalDate.now();
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static Random random = new Random();

    public static String calculatedDate(int days, int band) {
        LocalDate newDate = time.plusDays(days).plusDays(random.nextInt(band));
        return formatter.format(newDate);
    }

    public static String generateProblematicName(){return "Алёна";}

    public static String generateNotAvailableCity() {
        return "NYC";
    }

    public static String generateEmptyField(){return "";}

    public static String generateInvalidPhone(){return "+8800";}

    public static RegistrationInfo randomUser(String Locale) {
        Faker faker = new Faker(new Locale("ru"));
        String[] citiesList = new String[]{"Абакан", "Барнаул", "Великий Новгород", "Горно-Алтайск",
                "Ижевск", "Йошкар-Ола", "Калуга", "Липецк", "Мурманск", "Нижний Новгород", "Омск",
                "Псков",
                "Рязань", "Самара", "Томск", "Уфа", "Хабаровск", "Челябинск", "Элиста",
                "Южно-Сахалинск",
                "Якутск"};
        String randomCity = citiesList[random.nextInt(citiesList.length)];
        return new RegistrationInfo(randomCity, faker.name().fullName(), faker.phoneNumber()
                .phoneNumber());
    }
}
