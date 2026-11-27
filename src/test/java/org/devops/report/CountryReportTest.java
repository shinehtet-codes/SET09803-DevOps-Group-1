package org.devops.report;

import org.devops.model.Country;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link CountryReport} (US-01 to US-06).
 * Requires the MySQL world database (see WorldDatabaseTestSupport).
 */
class CountryReportTest {
    private static Connection connection;
    private final CountryReport report = new CountryReport();

    @BeforeAll
    static void openConnection() throws Exception {
        connection = WorldDatabaseTestSupport.openConnection();
    }

    @AfterAll
    static void closeConnection() throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    // ----- Helpers -----

    private static void assertPopulationDescending(List<Country> rows) {
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getPopulation() >= rows.get(i).getPopulation(),
                    "Countries must be ordered by population descending.");
        }
    }

    private static List<String> codes(List<Country> rows) {
        List<String> result = new ArrayList<>();
        for (Country row : rows) {
            result.add(row.getCode());
        }
        return result;
    }

    // ----- US-01 -----

    @Test
    void allCountriesAreOrderedByPopulationDescending() {
        ArrayList<Country> rows = report.getAllCountries(connection);

        assertFalse(rows.isEmpty(), "US-01 should return countries.");
        assertPopulationDescending(rows);
    }

    @Test
    void allCountriesReturnsEveryCountryWithAllColumns() {
        ArrayList<Country> rows = report.getAllCountries(connection);

        // The standard world database contains 239 countries.
        assertEquals(239, rows.size());
        for (Country row : rows) {
            assertNotNull(row.getCode());
            assertNotNull(row.getName());
            assertNotNull(row.getContinent());
            assertNotNull(row.getRegion());
            assertNotNull(row.getCapital(), "Missing capitals should be shown as N/A, not null.");
        }
    }

    @Test
    void mostPopulatedCountryIsChina() {
        ArrayList<Country> rows = report.getAllCountries(connection);

        assertEquals("CHN", rows.get(0).getCode());
        assertEquals("Peking", rows.get(0).getCapital());
    }

    // ----- US-02 -----

    @Test
    void continentFilterOnlyReturnsAsia() {
        ArrayList<Country> rows = report.getCountriesByContinent(connection, "Asia");

        assertFalse(rows.isEmpty(), "The world database should contain Asian countries.");
        for (Country row : rows) {
            assertEquals("Asia", row.getContinent());
        }
        assertPopulationDescending(rows);
    }

    @Test
    void unknownContinentReturnsEmptyList() {
        assertTrue(report.getCountriesByContinent(connection, "Atlantis").isEmpty());
    }

    // ----- US-03 -----

    @Test
    void regionFilterOnlyReturnsCaribbean() {
        ArrayList<Country> rows = report.getCountriesByRegion(connection, "Caribbean");

        assertFalse(rows.isEmpty(), "The world database should contain Caribbean countries.");
        for (Country row : rows) {
            assertEquals("Caribbean", row.getRegion());
        }
        assertPopulationDescending(rows);
    }

    // ----- US-04 -----

    @Test
    void topFiveCountriesDoesNotExceedLimitAndIsOrdered() {
        int limit = 5;
        ArrayList<Country> rows = report.getTopNCountriesInWorld(connection, limit);

        assertEquals(limit, rows.size());
        assertPopulationDescending(rows);
    }

    @Test
    void topNWorldMatchesStartOfFullCountryReport() {
        List<String> expected = codes(report.getAllCountries(connection)).subList(0, 10);

        assertEquals(expected, codes(report.getTopNCountriesInWorld(connection, 10)));
    }

    // ----- US-05 -----

    @Test
    void topNContinentMatchesStartOfContinentReport() {
        List<String> expected =
                codes(report.getCountriesByContinent(connection, "Europe")).subList(0, 5);

        ArrayList<Country> rows = report.getTopNCountriesByContinent(connection, "Europe", 5);

        assertEquals(expected, codes(rows));
        for (Country row : rows) {
            assertEquals("Europe", row.getContinent());
        }
    }

    // ----- US-06 -----

    @Test
    void topNRegionMatchesStartOfRegionReport() {
        List<String> expected =
                codes(report.getCountriesByRegion(connection, "Southeast Asia")).subList(0, 3);

        ArrayList<Country> rows = report.getTopNCountriesByRegion(connection, "Southeast Asia", 3);

        assertEquals(expected, codes(rows));
        for (Country row : rows) {
            assertEquals("Southeast Asia", row.getRegion());
        }
    }

    @Test
    void nLargerThanRegionReturnsWholeRegion() {
        int regionSize = report.getCountriesByRegion(connection, "Caribbean").size();

        assertEquals(regionSize,
                report.getTopNCountriesByRegion(connection, "Caribbean", 1000).size());
    }
}
