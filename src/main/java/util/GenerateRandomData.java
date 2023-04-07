package util;

import com.github.javafaker.Faker;

public class GenerateRandomData {
    Faker faker = new Faker();
    public String getRandomName() {
        return faker.name().firstName();
    }

    public String getRandomEmail() {
        return faker.bothify("??????@gmail.com");
    }

    public String getRandomPassword() {
        return faker.regexify("[a-z1-9]{10}");
    }
}
