package org.devops.report;

import org.devops.model.Country;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void allCountriesAreOrderedByPopulationDescending() {
        ArrayList<Country> rows = report.getAllCountries(connection);

        assertFalse(rows.isEmpty(), "US-01 should return countries.");
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getPopulation() >= rows.get(i).getPopulation(),
                    "Countries must be ordered by population descending.");
        }
    }

    @Test
    void continentFilterOnlyReturnsAsia() {
        ArrayList<Country> rows = report.getCountriesByContinent(connection, "Asia");

        assertFalse(rows.isEmpty(), "The world database should contain Asian countries.");
        for (Country row : rows) {
            assertEquals("Asia", row.getContinent());
        }
    }

    @Test
    void topFiveCountriesDoesNotExceedLimitAndIsOrdered() {
        int limit = 5;
        ArrayList<Country> rows = report.getTopNCountriesInWorld(connection, limit);

        assertFalse(rows.isEmpty());
        assertTrue(rows.size() <= limit);
        for (int i = 1; i < rows.size(); i++) {
            assertTrue(rows.get(i - 1).getPopulation() >= rows.get(i).getPopulation());
        }
    }
}
