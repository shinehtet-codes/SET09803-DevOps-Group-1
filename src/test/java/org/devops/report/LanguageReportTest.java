package org.devops.report;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class LanguageReportTest {

    private static Connection connection;
    private final LanguageReport report = new LanguageReport();

    private static final Set<String> REQUIRED_LANGUAGES =
            Set.of("Chinese", "English", "Hindi", "Spanish", "Arabic");

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
    void reportIncludesTheFiveRequiredLanguages() {
        ArrayList<String> rows =
                report.getMajorLanguageSpeakers(connection);

        assertFalse(rows.isEmpty(), "US-30 should return language rows.");

        Set<String> actualLanguages = rows.stream()
                .map(LanguageReportTest::parseLanguage)
                .collect(Collectors.toSet());

        assertEquals(REQUIRED_LANGUAGES, actualLanguages);
    }

    @Test
    void speakerCountsMatchReferenceSql() throws Exception {
        Map<String, Long> expected = referenceSpeakers();
        ArrayList<String> rows =
                report.getMajorLanguageSpeakers(connection);

        for (String row : rows) {
            String language = parseLanguage(row);
            long speakers = parseSpeakers(row);

            assertEquals(
                    expected.get(language),
                    speakers,
                    "Speaker estimate differs for " + language
            );
        }
    }

    @Test
    void rowsAreOrderedBySpeakerCountDescending() {
        ArrayList<String> rows =
                report.getMajorLanguageSpeakers(connection);

        assertFalse(rows.isEmpty());

        for (int i = 1; i < rows.size(); i++) {
            assertTrue(
                    parseSpeakers(rows.get(i - 1))
                            >= parseSpeakers(rows.get(i)),
                    "Language rows must be sorted by speakers descending."
            );
        }
    }

    @Test
    void percentagesMatchReferenceSql() throws Exception {
        Map<String, Double> expected = referencePercentages();
        ArrayList<String> rows =
                report.getMajorLanguageSpeakers(connection);

        for (String row : rows) {
            String language = parseLanguage(row);

            assertEquals(
                    expected.get(language),
                    parsePercentage(row),
                    0.01,
                    "World percentage differs for " + language
            );
        }
    }

    @Test
    void nullConnectionReturnsEmptyList() {
        assertTrue(report.getMajorLanguageSpeakers(null).isEmpty());
    }

    private static Map<String, Long> referenceSpeakers()
            throws Exception {

        Map<String, Long> values = new HashMap<>();

        String sql =
                "SELECT cl.Language AS Language, " +
                        "ROUND(SUM(c.Population * cl.Percentage / 100.0)) AS Speakers " +
                        "FROM countrylanguage cl " +
                        "JOIN country c ON c.Code = cl.CountryCode " +
                        "WHERE cl.Language IN " +
                        "('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                        "GROUP BY cl.Language";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                values.put(
                        result.getString("Language"),
                        result.getLong("Speakers")
                );
            }
        }

        return values;
    }

    private static Map<String, Double> referencePercentages()
            throws Exception {

        Map<String, Double> values = new HashMap<>();

        String sql =
                "SELECT cl.Language AS Language, " +
                        "ROUND(100.0 * SUM(c.Population * cl.Percentage / 100.0) / " +
                        "NULLIF((SELECT SUM(Population) FROM country), 0), 2) " +
                        "AS WorldPercent " +
                        "FROM countrylanguage cl " +
                        "JOIN country c ON c.Code = cl.CountryCode " +
                        "WHERE cl.Language IN " +
                        "('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                        "GROUP BY cl.Language";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                values.put(
                        result.getString("Language"),
                        result.getDouble("WorldPercent")
                );
            }
        }

        return values;
    }

    private static String parseLanguage(String row) {
        return row.split("\\|", -1)[0].trim();
    }

    private static long parseSpeakers(String row) {
        String[] columns = row.split("\\|", -1);
        return Long.parseLong(columns[1].trim().replace(",", ""));
    }

    private static double parsePercentage(String row) {
        String[] columns = row.split("\\|", -1);
        return Double.parseDouble(
                columns[2].trim().replace("%", "")
        );
    }
}
