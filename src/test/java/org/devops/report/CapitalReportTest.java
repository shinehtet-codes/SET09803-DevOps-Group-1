package org.devops.report;

import org.devops.model.CapitalCity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CapitalReportTest {

    private static Connection connection;
    private final CapitalReport report = new CapitalReport();

    @BeforeAll
    static void openConnection() throws Exception {
        connection = WorldDatabaseTestSupport.openConnection();
    }

    @AfterAll
    static void closeConnection() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void allCapitalCitiesAreReturnedAndOrderedByPopulationDescending() {
        ArrayList<CapitalCity> rows =
                report.getAllCapitalCities(connection);

        assertFalse(rows.isEmpty(), "US-15 should return capital cities.");

        for (int i = 1; i < rows.size(); i++) {
            assertTrue(
                    rows.get(i - 1).getPopulation()
                            >= rows.get(i).getPopulation(),
                    "Capital cities must be ordered by population descending."
            );
        }

        for (CapitalCity row : rows) {
            assertNotNull(row.getName());
            assertNotNull(row.getCountry());
            assertTrue(row.getPopulation() >= 0);
        }
    }

    @Test
    void continentFilterReturnsOnlyCountriesInSelectedContinent() throws Exception {
        String continent = "Asia";

        ArrayList<CapitalCity> rows =
                report.getCapitalCitiesByContinent(connection, continent);

        assertFalse(rows.isEmpty(), "Asia should have capital cities.");

        Set<String> expectedCountries = countriesForContinent(continent);

        for (CapitalCity row : rows) {
            assertTrue(
                    expectedCountries.contains(row.getCountry()),
                    "Unexpected country in continent result: " + row.getCountry()
            );
        }

        assertPopulationDescending(rows);
    }

    @Test
    void regionFilterReturnsOnlyCountriesInSelectedRegion() throws Exception {
        String region = "Southeast Asia";

        ArrayList<CapitalCity> rows =
                report.getCapitalCitiesByRegion(connection, region);

        assertFalse(rows.isEmpty(), "The selected region should have capitals.");

        Set<String> expectedCountries = countriesForRegion(region);

        for (CapitalCity row : rows) {
            assertTrue(
                    expectedCountries.contains(row.getCountry()),
                    "Unexpected country in region result: " + row.getCountry()
            );
        }

        assertPopulationDescending(rows);
    }

    @Test
    void topNWorldwideDoesNotExceedRequestedLimit() {
        int limit = 5;

        ArrayList<CapitalCity> rows =
                report.getTopNCapitalCitiesInWorld(connection, limit);

        assertFalse(rows.isEmpty());
        assertTrue(rows.size() <= limit);
        assertPopulationDescending(rows);
    }

    @Test
    void topNByContinentOnlyReturnsSelectedContinent() throws Exception {
        String continent = "Asia";
        int limit = 5;

        ArrayList<CapitalCity> rows =
                report.getTopNCapitalCitiesByContinent(
                        connection, continent, limit
                );

        assertTrue(rows.size() <= limit);

        Set<String> expectedCountries = countriesForContinent(continent);

        for (CapitalCity row : rows) {
            assertTrue(expectedCountries.contains(row.getCountry()));
        }

        assertPopulationDescending(rows);
    }

    @Test
    void topNByRegionOnlyReturnsSelectedRegion() throws Exception {
        String region = "Southeast Asia";
        int limit = 5;

        ArrayList<CapitalCity> rows =
                report.getTopNCapitalCitiesByRegion(
                        connection, region, limit
                );

        assertTrue(rows.size() <= limit);

        Set<String> expectedCountries = countriesForRegion(region);

        for (CapitalCity row : rows) {
            assertTrue(expectedCountries.contains(row.getCountry()));
        }

        assertPopulationDescending(rows);
    }

    @Test
    void topNWorldwideRejectsZeroAndNegativeLimits() {
        assertTrue(
                report.getTopNCapitalCitiesInWorld(connection, 0).isEmpty()
        );
        assertTrue(
                report.getTopNCapitalCitiesInWorld(connection, -1).isEmpty()
        );
    }

    @Test
    void continentFilterWithUnknownContinentReturnsNoRows() {
        assertTrue(
                report.getCapitalCitiesByContinent(
                        connection, "Not A Real Continent"
                ).isEmpty()
        );
    }

    private static void assertPopulationDescending(
            ArrayList<CapitalCity> rows) {

        for (int i = 1; i < rows.size(); i++) {
            assertTrue(
                    rows.get(i - 1).getPopulation()
                            >= rows.get(i).getPopulation()
            );
        }
    }

    private static Set<String> countriesForContinent(String continent)
            throws Exception {

        Set<String> countries = new HashSet<>();

        String sql = "SELECT Name FROM country WHERE Continent = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, continent);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    countries.add(result.getString("Name"));
                }
            }
        }

        return countries;
    }

    private static Set<String> countriesForRegion(String region)
            throws Exception {

        Set<String> countries = new HashSet<>();

        String sql = "SELECT Name FROM country WHERE Region = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, region);

            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    countries.add(result.getString("Name"));
                }
            }
        }

        return countries;
    }
}
