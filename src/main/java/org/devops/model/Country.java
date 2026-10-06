package org.devops.model;

/* Represents a country from the world database.
 */
public class Country
{
    /* Country code.
        */
    private String code;

    /* Country name.
        */
    private String name;

    /* Continent where the country is located.
        */
    private String continent;

    /* Region where the country is located.
        */
    private String region;

    /* Population of the country.
        */
    private long population;

    /* ID of the country's capital city.
        */
    private Integer capital;

    /* Creates a country.
     *
             * @param code Country code.
     * @param name Country name.
     * @param continent Country continent.
     * @param region Country region.
     * @param population Country population.
     * @param capital Capital city ID.
        */
    public Country(
            String code,
            String name,
            String continent,
            String region,
            long population,
            Integer capital)
    {
        this.code = code;
        this.name = name;
        this.continent = continent;
        this.region = region;
        this.population = population;
        this.capital = capital;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }

    public String getContinent()
    {
        return continent;
    }

    public String getRegion()
    {
        return region;
    }

    public long getPopulation()
    {
        return population;
    }

    public Integer getCapital()
    {
        return capital;
    }
}