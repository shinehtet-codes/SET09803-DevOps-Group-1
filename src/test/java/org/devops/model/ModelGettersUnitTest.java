package org.devops.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests checking that the report model classes store and return
 * the values given to their constructors.
 */
class ModelGettersUnitTest {

    @Test
    void countryReturnsConstructorValues() {
        Country country = new Country(
                "GBR", "United Kingdom", "Europe", "British Islands", 59_623_400L, "London");

        assertEquals("GBR", country.getCode());
        assertEquals("United Kingdom", country.getName());
        assertEquals("Europe", country.getContinent());
        assertEquals("British Islands", country.getRegion());
        assertEquals(59_623_400L, country.getPopulation());
        assertEquals("London", country.getCapital());
    }

    @Test
    void cityReturnsConstructorValues() {
        City city = new City("Edinburgh", "United Kingdom", "Scotland", 450_180L);

        assertEquals("Edinburgh", city.getName());
        assertEquals("United Kingdom", city.getCountry());
        assertEquals("Scotland", city.getDistrict());
        assertEquals(450_180L, city.getPopulation());
    }

    @Test
    void capitalCityReturnsConstructorValues() {
        CapitalCity capital = new CapitalCity("London", "United Kingdom", 7_285_000L);

        assertEquals("London", capital.getName());
        assertEquals("United Kingdom", capital.getCountry());
        assertEquals(7_285_000L, capital.getPopulation());
    }

    @Test
    void modelsAcceptZeroPopulation() {
        assertEquals(0L, new Country("ATA", "Antarctica", "Antarctica", "Antarctica", 0L, "N/A").getPopulation());
        assertEquals(0L, new City("Test City", "Test Country", "Test District", 0L).getPopulation());
        assertEquals(0L, new CapitalCity("Test Capital", "Test Country", 0L).getPopulation());
    }
}
