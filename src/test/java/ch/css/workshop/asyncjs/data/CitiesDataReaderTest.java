package ch.css.workshop.asyncjs.data;

import javaslang.collection.Array;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(JUnitPlatform.class)
public class CitiesDataReaderTest {
    @TestFactory
    public Iterable<DynamicTest> parseTestData() {
        final CitiesDataReader citiesReader = new CitiesDataReader("/data/cities_test.txt");
        return Array.of(
                DynamicTest.dynamicTest(
                        "Cities number should be:",
                        () -> {
                            assertEquals (92, citiesReader.getCities().get().size());
                        })
        );
    }

    @TestFactory
    public Iterable<DynamicTest> parseRealData() {
        final CitiesDataReader citiesReader = new CitiesDataReader("/data/worldcitiespop.txt");
        return Array.of(
                DynamicTest.dynamicTest(
                        "Cities number should be:",
                        () -> {
                            assertEquals (3173958, citiesReader.getCities().get().size());
                        })
        );
    }
}
