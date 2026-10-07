package org.devops.report;

import org.devops.model.City;

import java.sql.*;
import java.util.ArrayList;

public class CityReport
{
    /**
     * US-07: Returns all cities ordered by population descending.
     */
    public ArrayList<City> getAllCities(Connection con)
    {
        ArrayList<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "ORDER BY ci.Population DESC, ci.ID ASC";

        try
        {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next())
            {
                City city = new City(
                        rs.getString("Name"),
                        rs.getString("Country"),
                        rs.getString("District"),
                        rs.getLong("Population")
                );

                cities.add(city);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println("Failed to get all cities.");
            System.out.println(e.getMessage());
        }

        return cities;
    }

    public void printCities(ArrayList<City> cities)
    {
        System.out.printf(
                "%-40s%-40s%-30s%-15s%n",
                "Name",
                "Country",
                "District",
                "Population"
        );

        for (City city : cities)
        {
            System.out.printf(
                    "%-40s%-40s%-30s%-15d%n",
                    city.getName(),
                    city.getCountry(),
                    city.getDistrict(),
                    city.getPopulation()
            );
        }
    }
    /**
     * US-08: Returns cities in a selected continent.
     */
    public ArrayList<City> getCitiesByContinent(
            Connection con,
            String continent)
    {
        ArrayList<City> cities = new ArrayList<>();

        String sql =
                "SELECT ci.Name, co.Name AS Country, " +
                        "ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Continent = ? " +
                        "ORDER BY ci.Population DESC, ci.ID ASC";

        try
        {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, continent);

            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                City city = new City(
                        rs.getString("Name"),
                        rs.getString("Country"),
                        rs.getString("District"),
                        rs.getLong("Population")
                );

                cities.add(city);
            }

            rs.close();
            stmt.close();
        }
        catch (SQLException e)
        {
            System.out.println(
                    "Failed to get cities in continent: " + continent
            );
            System.out.println(e.getMessage());
        }

        return cities;
    }

}