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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the report print methods with empty and normal lists.
 * No database is used: the lists are built in the test.
 */
class ReportPrintUnitTest {

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

    private long lineCount() {
        return output().lines().count();
    }

    @Test
    void emptyCountryListPrintsHeaderOnly() {
        new CountryReport().printCountries(new ArrayList<>());

        assertEquals(1, lineCount());
        assertTrue(output().contains("Code"));
        assertTrue(output().contains("Capital"));
    }

    @Test
    void countryRowContainsEveryRequiredColumn() {
        ArrayList<Country> countries = new ArrayList<>(List.of(
                new Country("GBR", "United Kingdom", "Europe", "British Islands", 59_623_400L, "London")
        ));

        new CountryReport().printCountries(countries);

        assertEquals(2, lineCount());
        assertTrue(output().contains("GBR"));
        assertTrue(output().contains("United Kingdom"));
        assertTrue(output().contains("Europe"));
        assertTrue(output().contains("British Islands"));
        assertTrue(output().contains("59623400"));
        assertTrue(output().contains("London"));
    }

    @Test
    void emptyCityListPrintsHeaderOnly() {
        new CityReport().printCities(new ArrayList<>());

        assertEquals(1, lineCount());
        assertTrue(output().contains("District"));
    }

    @Test
    void cityRowContainsEveryRequiredColumn() {
        ArrayList<City> cities = new ArrayList<>(List.of(
                new City("Edinburgh", "United Kingdom", "Scotland", 450_180L)
        ));

        new CityReport().printCities(cities);

        assertEquals(2, lineCount());
        assertTrue(output().contains("Edinburgh"));
        assertTrue(output().contains("United Kingdom"));
        assertTrue(output().contains("Scotland"));
        assertTrue(output().contains("450180"));
    }

    @Test
    void emptyCapitalListPrintsHeaderOnly() {
        new CapitalReport().printCapitalCities(new ArrayList<>());

        assertEquals(1, lineCount());
        assertTrue(output().contains("Population"));
    }

    @Test
    void capitalRowContainsEveryRequiredColumn() {
        ArrayList<CapitalCity> capitals = new ArrayList<>(List.of(
                new CapitalCity("London", "United Kingdom", 7_285_000L)
        ));

        new CapitalReport().printCapitalCities(capitals);

        assertEquals(2, lineCount());
        assertTrue(output().contains("London"));
        assertTrue(output().contains("United Kingdom"));
        assertTrue(output().contains("7285000"));
    }

    @Test
    void populationRowShowsTotalsAndPercentages() {
        ArrayList<PopulationSummary> summaries = new ArrayList<>(List.of(
                new PopulationSummary("Europe", 1000L, 250L)
        ));

        new PopulationReport().printPopulationSummaries(summaries);

        assertEquals(2, lineCount());
        assertTrue(output().contains("Europe"));
        assertTrue(output().contains("1000"));
        assertTrue(output().contains("250"));
        assertTrue(output().contains("750"));
        assertTrue(output().contains("25.00%"));
        assertTrue(output().contains("75.00%"));
    }

    @Test
    void populationRowWithZeroTotalShowsNotApplicable() {
        ArrayList<PopulationSummary> summaries = new ArrayList<>(List.of(
                new PopulationSummary("Antarctica", 0L, 0L)
        ));

        new PopulationReport().printPopulationSummaries(summaries);

        assertTrue(output().contains("Antarctica"));
        assertTrue(output().contains("N/A"));
    }
}
