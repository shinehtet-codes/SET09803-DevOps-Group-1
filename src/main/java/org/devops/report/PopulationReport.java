package org.devops.report;

import org.devops.model.PopulationSummary;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PopulationReport
{
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
}