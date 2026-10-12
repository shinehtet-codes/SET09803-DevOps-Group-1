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

    /**
     * US-09: Cities in a region.
     */
    public ArrayList<City> getCitiesByRegion(Connection con, String region)
    {
        ArrayList<City> results = new ArrayList<>();

        if (con == null)
        {
            System.out.println("No active database connection.");
            return results;
        }

        String sql =
                "SELECT ci.Name, co.Name AS Country, ci.District, ci.Population " +
                        "FROM city ci JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Region = ? " +
                        "ORDER BY ci.Population DESC, ci.ID ASC";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, region);

            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    results.add(
                            new City(rs.getString("Name"), rs.getString("Country"),
                                    rs.getString("District"), rs.getLong("Population"))
                    );
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Failed to execute US-09: " + e.getMessage());
        }

        return results;
    }

    /**
     * US-10: Cities in a country
     */
    public ArrayList<City> getCitiesByCountry(Connection con, String country)
    {
        ArrayList<City> results = new ArrayList<>();
        if (con == null || country == null || country.isBlank())
        {
            System.out.println("Invalid database connection or report parameter.");
            return results;
        }

        String sql =
                "SELECT ci.Name, co.Name AS Country, ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Name = ? " +
                        "ORDER BY ci.Population DESC, ci.ID ASC";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, country.trim());
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    results.add(new City(rs.getString("Name"), rs.getString("Country"), rs.getString("District"), rs.getLong("Population")));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-10 failed: " + e.getMessage());
        }
        return results;
    }

    /**
     *US-11-cities-in-district
     */
    public ArrayList<City> getCitiesByDistrict(Connection con, String district)
    {
        ArrayList<City> cities = new ArrayList<>();
        if (con == null || district == null || district.isBlank())
        {
            System.out.println("Valid connection and district required.");
            return cities;
        }

        String sql =
                "SELECT ci.Name, co.Name AS Country, ci.District, ci.Population " +
                        "FROM city ci " +
                        "JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE ci.District = ? " +
                        "ORDER BY ci.Population DESC, ci.ID ASC";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, district.trim());
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    cities.add(new City(
                            rs.getString("Name"),
                            rs.getString("Country"),
                            rs.getString("District"),
                            rs.getLong("Population")
                    ));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-11 failed: " + e.getMessage());
        }
        return cities;
    }

    /**
     * US-12-top-n-cities-world
     */
    public ArrayList<City> getTopNCitiesInWorld(Connection con, int n)
    {
        ArrayList<City> cities = new ArrayList<>();
        if (con == null || n <= 0)
        {
            System.out.println("Positive N and valid connection required.");
            return cities;
        }
        String sql = "SELECT ci.Name, co.Name AS Country, ci.District, ci.Population " +
                "FROM city ci JOIN country co ON ci.CountryCode = co.Code " +
                "ORDER BY ci.Population DESC, ci.ID ASC LIMIT ?";
        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setInt(1, n);
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    cities.add(new City(rs.getString("Name"), rs.getString("Country"),
                            rs.getString("District"), rs.getLong("Population")));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-12 failed: " + e.getMessage());
        }
        return cities;
    }

    /**
     * US-13-top-n-cities-continent
     * Returns the N most populated cities in a selected continent.
     */
    public ArrayList<City> getTopNCitiesByContinent(Connection con, String continent, int n)
    {
        ArrayList<City> cities = new ArrayList<>();
        if (con == null || continent == null || continent.isBlank() || n <= 0)
        {
            System.out.println("Valid connection, continent and positive N required.");
            return cities;
        }

        String sql =
                "SELECT ci.Name, co.Name AS Country, ci.District, ci.Population " +
                        "FROM city ci JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Continent = ? " +
                        "ORDER BY ci.Population DESC, ci.ID ASC LIMIT ?";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, continent.trim());
            stmt.setInt(2, n);
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    cities.add(new City(rs.getString("Name"), rs.getString("Country"),
                            rs.getString("District"), rs.getLong("Population")));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-13 failed: " + e.getMessage());
        }
        return cities;
    }

    /**
     * US-31-top-n-cities-country
     * Returns the N most populated cities in the selected country.
     */
    public ArrayList<City> getTopNCitiesByCountry(Connection con, String country, int n)
    {
        ArrayList<City> cities = new ArrayList<>();
        if (con == null || country == null || country.isBlank() || n <= 0)
        {
            System.out.println("Valid connection, country and positive N required.");
            return cities;
        }

        String sql =
                "SELECT ci.Name, co.Name AS Country, ci.District, ci.Population " +
                        "FROM city ci JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE co.Name = ? " +
                        "ORDER BY ci.Population DESC, ci.ID ASC LIMIT ?";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, country.trim());
            stmt.setInt(2, n);
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    cities.add(new City(rs.getString("Name"), rs.getString("Country"),
                            rs.getString("District"), rs.getLong("Population")));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-31 failed: " + e.getMessage());
        }
        return cities;
    }

    /**
     * US-32-top-n-cities-district
     * Returns the N most populated cities in the selected district.
     */
    public ArrayList<City> getTopNCitiesByDistrict(Connection con, String district, int n)
    {
        ArrayList<City> cities = new ArrayList<>();
        if (con == null || district == null || district.isBlank() || n <= 0)
        {
            System.out.println("Valid connection, district and positive N required.");
            return cities;
        }

        String sql =
                "SELECT ci.Name, co.Name AS Country, ci.District, ci.Population " +
                        "FROM city ci JOIN country co ON ci.CountryCode = co.Code " +
                        "WHERE ci.District = ? " +
                        "ORDER BY ci.Population DESC, ci.ID ASC LIMIT ?";

        try (PreparedStatement stmt = con.prepareStatement(sql))
        {
            stmt.setString(1, district.trim());
            stmt.setInt(2, n);
            try (ResultSet rs = stmt.executeQuery())
            {
                while (rs.next())
                {
                    cities.add(new City(rs.getString("Name"), rs.getString("Country"),
                            rs.getString("District"), rs.getLong("Population")));
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("US-32 failed: " + e.getMessage());
        }
        return cities;
    }
}