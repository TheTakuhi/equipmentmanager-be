package com.interstellar.equipmentmanager.faker;

import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;

import java.util.Random;
import java.util.UUID;

public class TestFakeGeneratorProvider {
    private final static Settings settings = Settings.defaults()
            .set(Keys.BEAN_VALIDATION_ENABLED, true)
            .set(Keys.ARRAY_NULLABLE, false)
            .set(Keys.MAP_NULLABLE, false)
            .set(Keys.MAP_KEYS_NULLABLE, false)
            .set(Keys.ARRAY_ELEMENTS_NULLABLE, false)
            .set(Keys.COLLECTION_ELEMENTS_NULLABLE, false)
            .set(Keys.DOUBLE_MIN, 0d)
            .set(Keys.DOUBLE_MAX, 1000d)
            .set(Keys.ARRAY_MIN_LENGTH, 0)
            .set(Keys.MAP_MIN_SIZE, 0)
            .set(Keys.MAP_MAX_SIZE, 3)
            .set(Keys.MAX_DEPTH, 4);

    public static <T> InstancioApi<T> getFakeModelGenerator(Class<T> clazz, Long seed) {

        var faker = new Faker(new Random(seed));
        return Instancio.of(clazz)
                .withSettings(settings)
                .lenient()
                .generate(Select.fields(field -> field.getName().toLowerCase().contains("id") && field.getType() == String.class),
                        gen -> gen.string().prefix(UUID.randomUUID().toString()).length(0))
                .generate(Select.fields(field -> field.getName().toLowerCase().contains("email") && field.getType() == String.class),
                        gen -> gen.string().prefix(faker.internet().emailAddress()).length(0))
                .generate(Select.fields(field -> field.getName().toLowerCase().contains("name") && field.getType() == String.class),
                        gen -> gen.string().prefix(faker.funnyName().name()).length(0))
                .generate(Select.fields(field -> field.getName().toLowerCase().contains("firstname") && field.getType() == String.class),
                        gen -> gen.string().prefix(faker.name().firstName()).length(0))
                .generate(Select.fields(field -> field.getName().toLowerCase().contains("lastname") && field.getType() == String.class),
                        gen -> gen.string().prefix(faker.name().lastName()).length(0))
                .generate(Select.fields(field -> field.getName().toLowerCase().contains("description") && field.getType() == String.class),
                        gen -> gen.string().prefix(faker.yoda().quote()).length(0))
                .generate(Select.fields(field -> field.getName().toLowerCase().contains("login") && field.getType() == String.class),
                        gen -> gen.string().prefix(faker.name().username()).length(0))
                .generate(Select.fields(field -> field.getName().toLowerCase().contains("number") && field.getType() == String.class),
                        gen -> gen.string().prefix(faker.idNumber().valid()).length(0));
    }
}
