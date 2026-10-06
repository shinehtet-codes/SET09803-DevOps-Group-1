package org.devops.model;

/* Represents a capital city used in capital city reports.
 */
public class CapitalCity
{
    /* Capital city name.
     */
    private String name;

    /* Country name.
        */
    private String country;

    /* Population of the capital city.
     */
    private long population;

    /**
     * Creates a capital city.
     *
     * @param name Capital city name.
     * @param country Country name.
     * @param population Capital city population.
     */
    public CapitalCity(
            String name,
            String country,
            long population)
    {
        this.name = name;
        this.country = country;
        this.population = population;
    }

    public String getName()
    {
        return name;
    }

    public String getCountry()
    {
        return country;
    }

    public long getPopulation()
    {
        return population;
    }
}