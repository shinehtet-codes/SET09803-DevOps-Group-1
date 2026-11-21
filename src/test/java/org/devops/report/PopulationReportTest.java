
package org.devops.report;

import org.devops.model.PopulationSummary;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PopulationReportTest {

    private static Connection connection;
    private static PopulationReport populationReport;

    @BeforeAll
    static void setUp() throws Exception {
        connection = WorldDatabaseTestSupport.openConnection();
        populationReport = new PopulationReport();
    }

    @AfterAll
    static void tearDown() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Test
    void us21ReturnsPopulationBreakdownForEachContinent() {
        ArrayList<PopulationSummary> results =
                populationReport.getPopulationInEachContinent(connection);

        assertFalse(results.isEmpty());
        assertTrue(results.stream()
                .anyMatch(row -> "Asia".equals(row.getName())));
        assertPopulationSummariesAreValid(results);
        assertNamesAreAscending(results);
    }

    @Test
    void us22ReturnsPopulationBreakdownForEachRegion() {
        ArrayList<PopulationSummary> results =
                populationReport.getPopulationByRegion(connection);

        assertFalse(results.isEmpty());
        assertTrue(results.stream()
                .anyMatch(row -> "Southeast Asia".equals(row.getName())));
        assertPopulationSummariesAreValid(results);
        assertNamesAreAscending(results);
    }

    @Test
    void us23ReturnsPopulationBreakdownForEachCountry() {
        ArrayList<PopulationSummary> results =
                populationReport.getPopulationByCountry(connection);

        assertFalse(results.isEmpty());
        assertTrue(results.stream()
                .anyMatch(row -> "China".equals(row.getName())));
        assertPopulationSummariesAreValid(results);
        assertNamesAreAscending(results);
    }

    @Test
    void us24ReturnsOneWorldPopulationSummaryMatchingReferenceSql()
            throws Exception {
        ArrayList<PopulationSummary> results =
                populationReport.getWorldPopulation(connection);

        assertEquals(1, results.size());

        PopulationSummary world = results.get(0);
        assertEquals("World", world.getName());

        long expectedTotal = queryLong(
                "SELECT COALESCE(SUM(Population), 0) FROM country"
        );

        long expectedCityPopulation = queryLong(
                "SELECT COALESCE(SUM(Population), 0) FROM city"
        );

        assertEquals(expectedTotal, world.getTotalPopulation());
        assertEquals(expectedCityPopulation, world.getCityPopulation());
        assertEquals(
                expectedTotal - expectedCityPopulation,
                world.getNonCityPopulation()
        );
    }

    @Test
    void us25ReturnsPopulationTotalForEachContinent() throws Exception {
        ArrayList<PopulationSummary> results =
                populationReport.getPopulationByContinent(connection);

        assertFalse(results.isEmpty());

        PopulationSummary asia = results.stream()
                .filter(row -> "Asia".equals(row.getName()))
                .findFirst()
                .orElseThrow();

        long expected = queryLong(
                "SELECT COALESCE(SUM(Population), 0) " +
                        "FROM country WHERE Continent = 'Asia'"
        );

        assertEquals(expected, asia.getTotalPopulation());
        assertNamesAreAscending(results);
    }

    @Test
    void us26ReturnsPopulationForSoutheastAsia() throws Exception {
        long actual = populationReport.getRegionPopulation(
                connection, "Southeast Asia"
        );

        long expected = queryLong(
                "SELECT COALESCE(SUM(Population), 0) " +
                        "FROM country WHERE Region = 'Southeast Asia'"
        );

        assertEquals(expected, actual);
    }

    @Test
    void us27ReturnsPopulationForChina() throws Exception {
        Long actual = populationReport.getCountryPopulation(
                connection, "China"
        );

        long expected = queryLong(
                "SELECT Population FROM country WHERE Name = 'China'"
        );

        assertNotNull(actual);
        assertEquals(expected, actual.longValue());
    }

    @Test
    void us28ReturnsPopulationForCaliforniaDistrict() throws Exception {
        Long actual = populationReport.getDistrictPopulation(
                connection, "California"
        );

        long expected = queryLong(
                "SELECT COALESCE(SUM(Population), 0) " +
                        "FROM city WHERE District = 'California'"
        );

        assertNotNull(actual);
        assertEquals(expected, actual.longValue());
    }

    @Test
    void us29ReturnsPopulationForNewYorkCity() throws Exception {
        Long actual = populationReport.getCityPopulation(
                connection, "New York"
        );

        long expected = queryLong(
                "SELECT Population FROM city " +
                        "WHERE Name = 'New York' ORDER BY ID ASC LIMIT 1"
        );

        assertNotNull(actual);
        assertEquals(expected, actual.longValue());
    }

    @Test
    void singleValueMethodsHandleNullAndBlankInputs() {
        assertEquals(
                0L,
                populationReport.getRegionPopulation(null, "Southeast Asia")
        );
        assertEquals(
                0L,
                populationReport.getRegionPopulation(connection, " ")
        );

        assertNull(populationReport.getCountryPopulation(null, "China"));
        assertNull(populationReport.getCountryPopulation(connection, ""));
        assertNull(populationReport.getDistrictPopulation(null, "California"));
        assertNull(populationReport.getDistrictPopulation(connection, " "));
        assertNull(populationReport.getCityPopulation(null, "New York"));
        assertNull(populationReport.getCityPopulation(connection, ""));
    }

    private static long queryLong(String sql) throws Exception {
        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            assertTrue(result.next(), "Reference query should return a row.");
            return result.getLong(1);
        }
    }

    private static void assertPopulationSummariesAreValid(
            List<PopulationSummary> rows) {

        for (PopulationSummary row : rows) {
            assertNotNull(row.getName());
            assertTrue(row.getTotalPopulation() >= 0);
            assertTrue(row.getCityPopulation() >= 0);

            assertEquals(
                    row.getTotalPopulation() - row.getCityPopulation(),
                    row.getNonCityPopulation()
            );

            if (row.getTotalPopulation() > 0) {
                assertEquals(
                        100.0,
                        row.getCityPercentage() + row.getNonCityPercentage(),
                        0.01
                );
            }
        }
    }

    private static void assertNamesAreAscending(
            List<PopulationSummary> rows) {

        for (int i = 1; i < rows.size(); i++) {
            String previousName = rows.get(i - 1).getName();
            String currentName = rows.get(i).getName();

            assertTrue(
                    previousName.compareToIgnoreCase(currentName) <= 0,
                    "Names should be ordered alphabetically. Previous: "
                            + previousName
                            + ", Current: "
                            + currentName
            );
        }
    }
}
