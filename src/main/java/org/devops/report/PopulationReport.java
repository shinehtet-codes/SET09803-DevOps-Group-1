package org.devops.report;

import org.devops.model.PopulationSummary;

import java.sql.*;
import java.util.ArrayList;

public class PopulationReport
{
    /**
     * US-21-population-in-each-continent
     * Returns total, city and non-city population (with percentages)
     * for every continent.Reuses the existing US-25 aggregation.
     */
    public ArrayList<PopulationSummary> getPopulationInEachContinent(Connection con)
    {
        if (con == null)
        {
            System.out.println("Valid database connection required.");
            return new ArrayList<>();
        }
        return getPopulationByContinent(con);
    }

    /**
     * US-22: Returns a population breakdown for every region.
     */
    public ArrayList<PopulationSummary> getPopulationByRegion(
            Connection con)
    {
        ArrayList<PopulationSummary> summaries =
                new ArrayList<>();

        String sql =
                "SELECT c.Region AS AreaName, " +
                        "SUM(c.Population) AS TotalPopulation, " +
                        "SUM(COALESCE(cp.CityPopulation, 0)) AS CityPopulation " +
                        "FROM country c " +
                        "LEFT JOIN (" +
                        "SELECT CountryCode, SUM(Population) AS CityPopulation " +
                        "FROM city " +
                        "GROUP BY CountryCode" +
                        ") cp ON c.Code = cp.CountryCode " +
                        "GROUP BY c.Region " +
                        "ORDER BY c.Region ASC";

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                PopulationSummary summary =
                        new PopulationSummary(
                                rs.getString("AreaName"),
                                rs.getLong("TotalPopulation"),
                                rs.getLong("CityPopulation")
                        );

                summaries.add(summary);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println(
                    "Failed to get population by region."
            );
            System.out.println(e.getMessage());
        }

        return summaries;
    }

    public void printPopulationSummaries(
            ArrayList<PopulationSummary> summaries)
    {
        System.out.printf(
                "%-30s%-18s%-18s%-12s%-20s%-12s%n",
                "Area",
                "Total Population",
                "City Population",
                "City %",
                "Non-City Population",
                "Non-City %"
        );

        for (PopulationSummary summary : summaries)
        {
            String cityPercentage =
                    Double.isNaN(summary.getCityPercentage())
                            ? "N/A"
                            : String.format(
                            "%.2f%%",
                            summary.getCityPercentage()
                    );

            String nonCityPercentage =
                    Double.isNaN(summary.getNonCityPercentage())
                            ? "N/A"
                            : String.format(
                            "%.2f%%",
                            summary.getNonCityPercentage()
                    );

            System.out.printf(
                    "%-30s%-18d%-18d%-12s%-20d%-12s%n",
                    summary.getName(),
                    summary.getTotalPopulation(),
                    summary.getCityPopulation(),
                    cityPercentage,
                    summary.getNonCityPopulation(),
                    nonCityPercentage
            );
        }
    }
    /**
     * US-23: Returns population breakdown for every country.
     */
    public ArrayList<PopulationSummary> getPopulationByCountry(
            Connection con)
    {
        ArrayList<PopulationSummary> summaries =
                new ArrayList<>();

        String sql =
                "SELECT c.Name AS AreaName, " +
                        "c.Population AS TotalPopulation, " +
                        "COALESCE(cp.CityPopulation, 0) AS CityPopulation " +
                        "FROM country c " +
                        "LEFT JOIN (" +
                        "SELECT CountryCode, SUM(Population) AS CityPopulation " +
                        "FROM city " +
                        "GROUP BY CountryCode" +
                        ") cp ON c.Code = cp.CountryCode " +
                        "ORDER BY c.Name ASC";

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                PopulationSummary summary =
                        new PopulationSummary(
                                rs.getString("AreaName"),
                                rs.getLong("TotalPopulation"),
                                rs.getLong("CityPopulation")
                        );

                summaries.add(summary);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println(
                    "Failed to get population by country."
            );
            System.out.println(e.getMessage());
        }

        return summaries;
    }
    /**
     * US-24: Returns the world population breakdown.
     */
    public ArrayList<PopulationSummary> getWorldPopulation(Connection con)
    {
        ArrayList<PopulationSummary> summaries = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No active database connection.");
            return summaries;
        }

        String sql =
                "SELECT SUM(c.Population) AS TotalPopulation, " +
                        "COALESCE(SUM(cp.CityPopulation), 0) AS CityPopulation " +
                        "FROM country c " +
                        "LEFT JOIN (" +
                        "SELECT CountryCode, SUM(Population) AS CityPopulation " +
                        "FROM city GROUP BY CountryCode" +
                        ") cp ON c.Code = cp.CountryCode";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            if (rs.next())
            {
                summaries.add(new PopulationSummary(
                        "World",
                        rs.getLong("TotalPopulation"),
                        rs.getLong("CityPopulation")
                ));
            }
        }
        catch (SQLException e)
        {
            System.out.println("Failed to execute US-24: " + e.getMessage());
        }

        return summaries;
    }
    /**
     * US-25: Population breakdown by continent
     */
    public ArrayList<PopulationSummary> getPopulationByContinent(Connection con)
    {
        ArrayList<PopulationSummary> results = new ArrayList<>();
        if (con == null)
        {
            System.out.println("No database connection.");
            return results;
        }

        String sql =
                "SELECT c.Continent AS AreaName, SUM(c.Population) AS TotalPopulation, COALESCE(SUM(cp.CityPopulation), 0) AS CityPopulation " +
                        "FROM country c " +
                        "LEFT JOIN (SELECT CountryCode, SUM(Population) AS CityPopulation " +
                        "FROM city " +
                        "GROUP BY CountryCode) cp ON c.Code = cp.CountryCode " +
                        "GROUP BY c.Continent " +
                        "ORDER BY c.Continent ASC";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next())
            {
                results.add(new PopulationSummary(rs.getString("AreaName"), rs.getLong("TotalPopulation"), rs.getLong("CityPopulation")));
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-25 failed: " + e.getMessage());
        }
        return results;
    }

    /**
     * US-26-region-population
     */
    public long getRegionPopulation(Connection con, String region)
    {
        if (con == null || region == null || region.isBlank())
        {
            System.out.println("Valid connection and region required.");
            return 0L;
        }

        String sql =
                "SELECT COALESCE(SUM(Population), 0) AS TotalPopulation " +
                        "FROM country WHERE Region = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, region.trim());
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getLong("TotalPopulation");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-26 failed: " + e.getMessage());
        }
        return 0L;
    }

    /**
     *US-27-country-population
     */

    public Long getCountryPopulation(Connection con, String countryName)
    {
        if (con == null || countryName == null || countryName.isBlank())
        {
            System.out.println("Valid connection and country name required.");
            return null;
        }
        String sql = "SELECT Population AS TotalPopulation FROM country WHERE Name = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, countryName.trim());
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getLong("TotalPopulation");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-27 failed: " + e.getMessage());
        }
        return null; // null means no matching country or query could not complete
    }

    /**
     * US-28-district-population
     * Returns total population of cities in a selected district.
     * Returns null when no matching district exists or the query fails.
     */
    public Long getDistrictPopulation(Connection con, String district)
    {
        if (con == null || district == null || district.isBlank())
        {
            System.out.println("Valid connection and district required.");
            return null;
        }

        String sql =
                "SELECT SUM(Population) AS TotalPopulation " +
                        "FROM city WHERE District = ?";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, district.trim());
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    long total = rs.getLong("TotalPopulation");
                    return rs.wasNull() ? null : total;
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-28 failed: " + e.getMessage());
        }
        return null;
    }

    /**
     * US-29-city-population
     * Returns the population of a city selected by name.
     * Returns null when no matching city exists or the query fails.
     */
    public Long getCityPopulation(Connection con, String cityName)
    {
        if (con == null || cityName == null || cityName.isBlank())
        {
            System.out.println("Valid connection and city name required.");
            return null;
        }

        String sql =
                "SELECT Population AS TotalPopulation " +
                        "FROM city WHERE Name = ? ORDER BY ID ASC LIMIT 1";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, cityName.trim());
            try (ResultSet rs = stmt.executeQuery())
            {
                if (rs.next())
                {
                    return rs.getLong("TotalPopulation");
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-29 failed: " + e.getMessage());
        }
        return null;
    }


}