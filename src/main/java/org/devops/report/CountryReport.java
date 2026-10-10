package org.devops.report;

import org.devops.model.Country;

import java.sql.*;
import java.util.ArrayList;

public class CountryReport
{
    /**
     * US-01: Returns all countries ordered by population descending.
     */
    public ArrayList<Country> getAllCountries(Connection con)
    {
        ArrayList<Country> countries = new ArrayList<>();

        String sql =
                "SELECT c.Code, c.Name, c.Continent, c.Region, " +
                        "c.Population, cap.Name AS Capital " +
                        "FROM country c " +
                        "LEFT JOIN city cap ON c.Capital = cap.ID " +
                        "ORDER BY c.Population DESC, c.Code ASC";

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                String capital = rs.getString("Capital");

                if (capital == null)
                {
                    capital = "N/A";
                }

                Country country = new Country(
                        rs.getString("Code"),
                        rs.getString("Name"),
                        rs.getString("Continent"),
                        rs.getString("Region"),
                        rs.getLong("Population"),
                        capital
                );

                countries.add(country);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println("Failed to get all countries.");
            System.out.println(e.getMessage());
        }

        return countries;
    }

    /**
     * US-02: Returns countries in a selected continent.
     */
    public ArrayList<Country> getCountriesByContinent(
            Connection con,
            String continent)
    {
        ArrayList<Country> countries = new ArrayList<>();

        String sql =
                "SELECT c.Code, c.Name, c.Continent, c.Region, " +
                        "c.Population, cap.Name AS Capital " +
                        "FROM country c " +
                        "LEFT JOIN city cap ON c.Capital = cap.ID " +
                        "WHERE c.Continent = ? " +
                        "ORDER BY c.Population DESC, c.Code ASC";

        try
        {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, continent);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                String capital = rs.getString("Capital");

                if (capital == null)
                {
                    capital = "N/A";
                }

                Country country = new Country(
                        rs.getString("Code"),
                        rs.getString("Name"),
                        rs.getString("Continent"),
                        rs.getString("Region"),
                        rs.getLong("Population"),
                        capital
                );

                countries.add(country);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println(
                    "Failed to get countries in continent: " + continent
            );
            System.out.println(e.getMessage());
        }

        return countries;
    }

    /**
     * Prints a country report.
     */
    public void printCountries(ArrayList<Country> countries)
    {
        System.out.printf(
                "%-5s%-40s%-20s%-30s%-15s%-30s%n",
                "Code",
                "Name",
                "Continent",
                "Region",
                "Population",
                "Capital"
        );

        for (Country country : countries)
        {
            System.out.printf(
                    "%-5s%-40s%-20s%-30s%-15d%-30s%n",
                    country.getCode(),
                    country.getName(),
                    country.getContinent(),
                    country.getRegion(),
                    country.getPopulation(),
                    country.getCapital()
            );
        }
    }
    /**
     * US-03: Countries in a region.
     */
    public ArrayList<Country> getCountriesByRegion(Connection con, String region)
    {
        ArrayList<Country> results = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No active database connection.");
            return results;
        }
        String sql =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital " +
                        "FROM country c LEFT JOIN city cap ON c.Capital = cap.ID " +
                        "WHERE c.Region = ? " +
                        "ORDER BY c.Population DESC, c.Code ASC";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, region);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    results.add(
                            new Country(rs.getString("Code"), rs.getString("Name"),
                                    rs.getString("Continent"), rs.getString("Region"),
                                    rs.getLong("Population"),
                                    rs.getString("Capital") == null ? "N/A" : rs.getString("Capital"))
                    );
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Failed to execute US-03: " + e.getMessage());
        }

        return results;
    }
    /**
     * US-04: Top N countries in the world
     */

    public ArrayList<Country> getTopNCountriesInWorld(Connection con, int n)
    {
        ArrayList<Country> results = new ArrayList<>();
        if (con == null || n <= 0)
        {
            System.out.println("Invalid database connection or report parameter.");
            return results;
        }

        String sql =
                "SELECT c.Code, c.Name, c.Continent, c.Region, c.Population, cap.Name AS Capital " +
                        "FROM country c " +
                        "LEFT JOIN city cap ON c.Capital = cap.ID " +
                        "ORDER BY c.Population DESC, c.Code ASC " +
                        "LIMIT ?";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setInt(1, n);
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    results.add(new Country(rs.getString("Code"), rs.getString("Name"), rs.getString("Continent"), rs.getString("Region"), rs.getLong("Population"), rs.getString("Capital") == null ? "N/A" : rs.getString("Capital")));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-04 failed: " + e.getMessage());
        }
        return results;
    }
}