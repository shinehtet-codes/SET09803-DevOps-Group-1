package org.devops.model;

/* Represents a city used in city reports.
 */
public class City
{
    /* City name.
        */
    private String name;

    /* Country name.
        */
    private String country;

    /* District where the city is located.
        */
    private String district;

    /* Population of the city.
        */
    private long population;

    /* Creates a city.
     *
             * @param name City name.
     * @param country Country name.
     * @param district City district.
     * @param population City population.
     */
    public City(
            String name,
            String country,
            String district,
            long population)
    {
        this.name = name;
        this.country = country;
        this.district = district;
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

    public String getDistrict()
    {
        return district;
    }

    public long getPopulation()
    {
        return population;
    }
}