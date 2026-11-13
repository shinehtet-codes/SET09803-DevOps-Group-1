package org.devops.report;

import org.devops.model.City;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void allCitiesAreOrderedByPopulationDescending() {
        ArrayList<City> rows = report.getAllCities(connection);

        assertFalse(rows.isEmpty(), "US-07 should return cities.");
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getPopulation() >= rows.get(i).getPopulation(),
                    "Cities must be ordered by population descending.");
        }
    }

    @Test
    void countryFilterOnlyReturnsCitiesInChina() {
        ArrayList<City> rows = report.getCitiesByCountry(connection, "China");

        assertFalse(rows.isEmpty(), "The world database should contain cities in China.");
        for (City row : rows) {
            assertEquals("China", row.getCountry());
        }
    }

    @Test
    void topFiveCitiesDoesNotExceedLimitAndIsOrdered() {
        int limit = 5;
        ArrayList<City> rows = report.getTopNCitiesInWorld(connection, limit);

        assertFalse(rows.isEmpty());
        assertTrue(rows.size() <= limit);
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getPopulation() >= rows.get(i).getPopulation());
        }
    }
}
