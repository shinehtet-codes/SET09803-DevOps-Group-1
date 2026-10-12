package org.devops.report;

import java.sql.*;
import java.util.ArrayList;

public class LanguageReport
{
    /**
     * US-30-language-speakers
     * Estimates speakers of Chinese, English, Hindi, Spanish and Arabic.
     * Returns formatted rows: Language | Speakers | Percentage of World.
     */
    public ArrayList<String> getMajorLanguageSpeakers(Connection con)
    {
        ArrayList<String> rows = new ArrayList<>();
        if (con == null)
        {
            System.out.println("Valid database connection required.");
            return rows;
        }

        String sql =
                "SELECT cl.Language AS Language, " +
                        "ROUND(SUM(c.Population * cl.Percentage / 100.0)) AS Speakers, " +
                        "ROUND(100.0 * SUM(c.Population * cl.Percentage / 100.0) / " +
                        "NULLIF((SELECT SUM(Population) FROM country), 0), 2) AS WorldPercent " +
                        "FROM countrylanguage cl " +
                        "JOIN country c ON c.Code = cl.CountryCode " +
                        "WHERE cl.Language IN ('Chinese', 'English', 'Hindi', 'Spanish', 'Arabic') " +
                        "GROUP BY cl.Language " +
                        "ORDER BY Speakers DESC, cl.Language ASC";

        try (PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {
            while (rs.next())
            {
                rows.add(String.format("%-12s |%12d |%6.2f%%",
                        rs.getString("Language"),
                        rs.getLong("Speakers"),
                        rs.getDouble("WorldPercent")));
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-30 failed: " + e.getMessage());
        }
        return rows;
    }
}
