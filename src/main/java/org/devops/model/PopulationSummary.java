package org.devops.model;

public class PopulationSummary
{
    private String name;
    private long totalPopulation;
    private long cityPopulation;
    private long nonCityPopulation;
    private double cityPercentage;
    private double nonCityPercentage;

    public PopulationSummary(
            String name,
            long totalPopulation,
            long cityPopulation)
    {
        this.name = name;
        this.totalPopulation = totalPopulation;
        this.cityPopulation = cityPopulation;
        this.nonCityPopulation =
                totalPopulation - cityPopulation;

        if (totalPopulation > 0)
        {
            cityPercentage =
                    ((double) cityPopulation
                            / totalPopulation) * 100.0;

            nonCityPercentage =
                    ((double) nonCityPopulation
                            / totalPopulation) * 100.0;
        }
        else
        {
            cityPercentage = Double.NaN;
            nonCityPercentage = Double.NaN;
        }
    }

    public String getName()
    {
        return name;
    }

    public long getTotalPopulation()
    {
        return totalPopulation;
    }

    public long getCityPopulation()
    {
        return cityPopulation;
    }

    public long getNonCityPopulation()
    {
        return nonCityPopulation;
    }

    public double getCityPercentage()
    {
        return cityPercentage;
    }

    public double getNonCityPercentage()
    {
        return nonCityPercentage;
    }
}