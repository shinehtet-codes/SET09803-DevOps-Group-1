package org.devops.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the population and percentage calculations
 * in {@link PopulationSummary}.
 */
class PopulationSummaryUnitTest {

    private static final double DELTA = 0.0001;

    @Test
    void calculatesNonCityPopulationAndPercentages() {
        PopulationSummary summary = new PopulationSummary("Europe", 1000L, 250L);

        assertEquals("Europe", summary.getName());
        assertEquals(1000L, summary.getTotalPopulation());
        assertEquals(250L, summary.getCityPopulation());
        assertEquals(750L, summary.getNonCityPopulation());
        assertEquals(25.0, summary.getCityPercentage(), DELTA);
        assertEquals(75.0, summary.getNonCityPercentage(), DELTA);
    }

    @Test
    void percentagesAlwaysAddUpToOneHundred() {
        PopulationSummary summary = new PopulationSummary("Asia", 3_705_025_700L, 697_604_103L);

        assertEquals(100.0,
                summary.getCityPercentage() + summary.getNonCityPercentage(),
                DELTA);
    }

    @Test
    void wholePopulationLivingInCitiesGivesOneHundredPercent() {
        PopulationSummary summary = new PopulationSummary("Singapore", 500L, 500L);

        assertEquals(0L, summary.getNonCityPopulation());
        assertEquals(100.0, summary.getCityPercentage(), DELTA);
        assertEquals(0.0, summary.getNonCityPercentage(), DELTA);
    }

    @Test
    void noCityPopulationGivesZeroPercentInCities() {
        PopulationSummary summary = new PopulationSummary("Remote Area", 800L, 0L);

        assertEquals(800L, summary.getNonCityPopulation());
        assertEquals(0.0, summary.getCityPercentage(), DELTA);
        assertEquals(100.0, summary.getNonCityPercentage(), DELTA);
    }

    @Test
    void zeroTotalPopulationGivesNaNInsteadOfDividingByZero() {
        PopulationSummary summary = new PopulationSummary("Antarctica", 0L, 0L);

        assertEquals(0L, summary.getNonCityPopulation());
        assertTrue(Double.isNaN(summary.getCityPercentage()));
        assertTrue(Double.isNaN(summary.getNonCityPercentage()));
    }
}
