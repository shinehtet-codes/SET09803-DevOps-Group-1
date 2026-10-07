package org.devops.report;

import org.devops.model.Country;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
}