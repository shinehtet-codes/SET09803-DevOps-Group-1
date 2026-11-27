package org.devops.report;

import org.devops.model.City;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for {@link CityReport} (US-07 to US-14, US-31, US-32).
 * Requires the MySQL world database (see WorldDatabaseTestSupport).
 */
class CityReportTest {
    private static Connection connection;
    private final CityReport report = new CityReport();

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

    private static void assertPopulationDescending(List<City> rows) {
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getPopulation() >= rows.get(i).getPopulation(),
                    "Cities must be ordered by population descending.");
        }
    }

    private static List<String> names(List<City> rows) {
        List<String> result = new ArrayList<>();
        for (City row : rows) {
            result.add(row.getName());
        }
        return result;
    }

    /**
     * Returns the names of countries matching a column value,
     * e.g. countryNamesWhere("Continent", "Europe").
     * The column name is fixed in the tests, never user input.
     */
    private static Set<String> countryNamesWhere(String column, String value) throws Exception {
        Set<String> result = new HashSet<>();
        String sql = "SELECT Name FROM country WHERE " + column + " = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    result.add(rs.getString("Name"));
                }
            }
        }
        return result;
    }

    // ----- US-07 -----

    @Test
    void allCitiesAreOrderedByPopulationDescending() {
        ArrayList<City> rows = report.getAllCities(connection);

        assertFalse(rows.isEmpty(), "US-07 should return cities.");
        assertPopulationDescending(rows);
    }

    @Test
    void allCitiesReturnsEveryCity() {
        // The standard world database contains 4079 cities.
        assertEquals(4079, report.getAllCities(connection).size());
    }

    // ----- US-08 -----

    @Test
    void continentFilterOnlyReturnsEuropeanCities() throws Exception {
        Set<String> europeanCountries = countryNamesWhere("Continent", "Europe");
        ArrayList<City> rows = report.getCitiesByContinent(connection, "Europe");

        assertFalse(rows.isEmpty());
        for (City row : rows) {
            assertTrue(europeanCountries.contains(row.getCountry()),
                    "Unexpected country for Europe: " + row.getCountry());
        }
        assertPopulationDescending(rows);
    }

    // ----- US-09 -----

    @Test
    void regionFilterOnlyReturnsCaribbeanCities() throws Exception {
        Set<String> caribbeanCountries = countryNamesWhere("Region", "Caribbean");
        ArrayList<City> rows = report.getCitiesByRegion(connection, "Caribbean");

        assertFalse(rows.isEmpty());
        for (City row : rows) {
            assertTrue(caribbeanCountries.contains(row.getCountry()),
                    "Unexpected country for Caribbean: " + row.getCountry());
        }
        assertPopulationDescending(rows);
    }

    // ----- US-10 -----

    @Test
    void countryFilterOnlyReturnsCitiesInChina() {
        ArrayList<City> rows = report.getCitiesByCountry(connection, "China");

        assertFalse(rows.isEmpty(), "The world database should contain cities in China.");
        for (City row : rows) {
            assertEquals("China", row.getCountry());
        }
        assertPopulationDescending(rows);
    }

    // ----- US-11 -----

    @Test
    void districtFilterOnlyReturnsCitiesInCalifornia() {
        ArrayList<City> rows = report.getCitiesByDistrict(connection, "California");

        assertFalse(rows.isEmpty());
        for (City row : rows) {
            assertEquals("California", row.getDistrict());
        }
        assertPopulationDescending(rows);
    }

    @Test
    void unknownDistrictReturnsEmptyList() {
        assertTrue(report.getCitiesByDistrict(connection, "No Such District").isEmpty());
    }

    // ----- US-12 -----

    @Test
    void topFiveCitiesDoesNotExceedLimitAndIsOrdered() {
        int limit = 5;
        ArrayList<City> rows = report.getTopNCitiesInWorld(connection, limit);

        assertEquals(limit, rows.size());
        assertPopulationDescending(rows);
    }

    @Test
    void mostPopulatedCityIsMumbai() {
        assertEquals("Mumbai (Bombay)", report.getTopNCitiesInWorld(connection, 1).get(0).getName());
    }

    // ----- US-13 -----

    @Test
    void topNContinentMatchesStartOfContinentReport() {
        List<String> expected = names(report.getCitiesByContinent(connection, "Africa")).subList(0, 5);

        assertEquals(expected, names(report.getTopNCitiesByContinent(connection, "Africa", 5)));
    }

    // ----- US-14 -----

    @Test
    void topNRegionMatchesStartOfRegionReport() {
        List<String> expected = names(report.getCitiesByRegion(connection, "Caribbean")).subList(0, 5);

        assertEquals(expected, names(report.getTopNCitiesByRegion(connection, "Caribbean", 5)));
    }

    // ----- US-31 -----

    @Test
    void topNCountryReturnsOnlyThatCountryInOrder() {
        List<String> expected = names(report.getCitiesByCountry(connection, "China")).subList(0, 10);
        ArrayList<City> rows = report.getTopNCitiesByCountry(connection, "China", 10);

        assertEquals(expected, names(rows));
        for (City row : rows) {
            assertEquals("China", row.getCountry());
        }
    }

    // ----- US-32 -----

    @Test
    void topNDistrictReturnsOnlyThatDistrictInOrder() {
        List<String> expected = names(report.getCitiesByDistrict(connection, "California")).subList(0, 3);
        ArrayList<City> rows = report.getTopNCitiesByDistrict(connection, "California", 3);

        assertEquals(expected, names(rows));
        for (City row : rows) {
            assertEquals("California", row.getDistrict());
        }
    }

    @Test
    void nLargerThanDistrictReturnsWholeDistrict() {
        int districtSize = report.getCitiesByDistrict(connection, "California").size();

        assertEquals(districtSize,
                report.getTopNCitiesByDistrict(connection, "California", 1000).size());
    }
}
