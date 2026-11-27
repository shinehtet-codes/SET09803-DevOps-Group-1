package org.devops.report;

import org.devops.model.CapitalCity;
import org.devops.model.City;
import org.devops.model.Country;
import org.devops.model.PopulationSummary;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for defensive behaviour in the report classes:
 * a missing database connection, invalid parameters, a null list
 * and null rows must never crash the application.
 *
 * No database is used in this class.
 */
class ReportGuardUnitTest {

    private final CountryReport countries = new CountryReport();
    private final CityReport cities = new CityReport();
    private final CapitalReport capitals = new CapitalReport();
    private final PopulationReport populations = new PopulationReport();

    private PrintStream originalOut;
    private ByteArrayOutputStream captured;

    @BeforeEach
    void captureOutput() {
        originalOut = System.out;
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    private String output() {
        return captured.toString(StandardCharsets.UTF_8);
    }

    // ----- Null connection: every query method returns an empty list -----

    @Test
    void countryReportsReturnEmptyListWithoutConnection() {
        assertTrue(countries.getAllCountries(null).isEmpty());
        assertTrue(countries.getCountriesByContinent(null, "Asia").isEmpty());
        assertTrue(countries.getCountriesByRegion(null, "Caribbean").isEmpty());
        assertTrue(countries.getTopNCountriesInWorld(null, 5).isEmpty());
        assertTrue(countries.getTopNCountriesByContinent(null, "Asia", 5).isEmpty());
        assertTrue(countries.getTopNCountriesByRegion(null, "Caribbean", 5).isEmpty());
    }

    @Test
    void cityReportsReturnEmptyListWithoutConnection() {
        assertTrue(cities.getAllCities(null).isEmpty());
        assertTrue(cities.getCitiesByContinent(null, "Asia").isEmpty());
        assertTrue(cities.getCitiesByRegion(null, "Caribbean").isEmpty());
        assertTrue(cities.getCitiesByCountry(null, "China").isEmpty());
        assertTrue(cities.getCitiesByDistrict(null, "California").isEmpty());
        assertTrue(cities.getTopNCitiesInWorld(null, 5).isEmpty());
        assertTrue(cities.getTopNCitiesByContinent(null, "Asia", 5).isEmpty());
        assertTrue(cities.getTopNCitiesByRegion(null, "Caribbean", 5).isEmpty());
        assertTrue(cities.getTopNCitiesByCountry(null, "China", 5).isEmpty());
        assertTrue(cities.getTopNCitiesByDistrict(null, "California", 5).isEmpty());
    }

    @Test
    void capitalReportsReturnEmptyListWithoutConnection() {
        assertTrue(capitals.getAllCapitalCities(null).isEmpty());
        assertTrue(capitals.getCapitalCitiesByContinent(null, "Asia").isEmpty());
        assertTrue(capitals.getCapitalCitiesByRegion(null, "Caribbean").isEmpty());
        assertTrue(capitals.getTopNCapitalCitiesInWorld(null, 5).isEmpty());
        assertTrue(capitals.getTopNCapitalCitiesByContinent(null, "Asia", 5).isEmpty());
        assertTrue(capitals.getTopNCapitalCitiesByRegion(null, "Caribbean", 5).isEmpty());
    }

    @Test
    void populationReportsReturnEmptyListWithoutConnection() {
        assertTrue(populations.getPopulationInEachContinent(null).isEmpty());
        assertTrue(populations.getPopulationByContinent(null).isEmpty());
        assertTrue(populations.getPopulationByRegion(null).isEmpty());
        assertTrue(populations.getPopulationByCountry(null).isEmpty());
        assertTrue(populations.getWorldPopulation(null).isEmpty());
    }

    @Test
    void failedQueryPrintsTheReportIdForDebugging() {
        countries.getAllCountries(null);

        assertTrue(output().contains("US-01"));
    }

    // ----- Invalid parameters -----

    @Test
    void zeroOrNegativeNReturnsEmptyList() {
        assertTrue(countries.getTopNCountriesInWorld(null, 0).isEmpty());
        assertTrue(cities.getTopNCitiesInWorld(null, -1).isEmpty());
        assertTrue(capitals.getTopNCapitalCitiesInWorld(null, 0).isEmpty());
    }

    @Test
    void nullOrBlankFilterReturnsEmptyList() {
        assertTrue(countries.getCountriesByContinent(null, null).isEmpty());
        assertTrue(cities.getCitiesByContinent(null, "   ").isEmpty());
        assertTrue(capitals.getCapitalCitiesByContinent(null, "").isEmpty());
    }

    // ----- Print methods: null list and null rows -----

    @Test
    void printMethodsHandleNullListWithMessage() {
        assertDoesNotThrow(() -> countries.printCountries(null));
        assertDoesNotThrow(() -> cities.printCities(null));
        assertDoesNotThrow(() -> capitals.printCapitalCities(null));
        assertDoesNotThrow(() -> populations.printPopulationSummaries(null));

        assertTrue(output().contains("No countries to display."));
        assertTrue(output().contains("No cities to display."));
        assertTrue(output().contains("No capital cities to display."));
        assertTrue(output().contains("No population data to display."));
    }

    @Test
    void printMethodsSkipNullRows() {
        ArrayList<Country> countryRows = new ArrayList<>();
        countryRows.add(null);
        ArrayList<City> cityRows = new ArrayList<>();
        cityRows.add(null);
        ArrayList<CapitalCity> capitalRows = new ArrayList<>();
        capitalRows.add(null);
        ArrayList<PopulationSummary> populationRows = new ArrayList<>();
        populationRows.add(null);

        assertDoesNotThrow(() -> countries.printCountries(countryRows));
        assertDoesNotThrow(() -> cities.printCities(cityRows));
        assertDoesNotThrow(() -> capitals.printCapitalCities(capitalRows));
        assertDoesNotThrow(() -> populations.printPopulationSummaries(populationRows));

        // Only the four header lines should be printed.
        assertEquals(4, output().lines().count());
    }
}
