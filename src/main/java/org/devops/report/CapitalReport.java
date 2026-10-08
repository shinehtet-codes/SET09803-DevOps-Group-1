package org.devops.report;

import org.devops.model.CapitalCity;

import java.sql.*;
import java.util.ArrayList;

public class CapitalReport
{
    /**
     * US-15: Returns all capital cities ordered by population.
     */
    public ArrayList<CapitalCity> getAllCapitalCities(Connection con)
    {
        ArrayList<CapitalCity> capitals = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, ci.Population " +
                        "FROM country co " +
                        "JOIN city ci ON co.Capital = ci.ID " +
                        "ORDER BY ci.Population DESC, ci.ID ASC";

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                CapitalCity capital = new CapitalCity(
                        rs.getString("Name"),
                        rs.getString("Country"),
                        rs.getLong("Population")
                );

                capitals.add(capital);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println("Failed to get all capital cities.");
            System.out.println(e.getMessage());
        }

        return capitals;
    }

    public void printCapitalCities(ArrayList<CapitalCity> capitals)
    {
        System.out.printf(
                "%-40s%-40s%-15s%n",
                "Name",
                "Country",
                "Population"
        );

        for (CapitalCity capital : capitals)
        {
            System.out.printf(
                    "%-40s%-40s%-15d%n",
                    capital.getName(),
                    capital.getCountry(),
                    capital.getPopulation()
            );
        }
    }
    /**
     * US-16: Returns capital cities in a selected continent.
     */
    public ArrayList<CapitalCity> getCapitalCitiesByContinent(
            Connection con,
            String continent)
    {
        ArrayList<CapitalCity> capitals = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, ci.Population " +
                        "FROM country co " +
                        "JOIN city ci ON co.Capital = ci.ID " +
                        "WHERE co.Continent = ? " +
                        "ORDER BY ci.Population DESC, ci.ID ASC";

        try
        {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, continent);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                CapitalCity capital = new CapitalCity(
                        rs.getString("Name"),
                        rs.getString("Country"),
                        rs.getLong("Population")
                );

                capitals.add(capital);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println(
                    "Failed to get capital cities in continent: "
                            + continent
            );
            System.out.println(e.getMessage());
        }

        return capitals;
    }
}