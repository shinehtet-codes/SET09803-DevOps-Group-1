# Population Reporting System - Report Requirements

## Purpose

This document organises the 32 User Stories into the five report categories required by the Population Reporting System and identifies the database tables that will support each category.

The supplied `world` database contains:

- `country`
- `city`
- `countrylanguage`

---

# 1. Country Reports

**Main database table:** `country`

Country reports use fields including `Code`, `Name`, `Continent`, `Region`, `Population`, and `Capital`.

## US-01 — All Countries by Population
Produce all countries in the world organised by population from largest to smallest.

## US-02 — Countries in a Continent
Produce all countries in a specified continent organised by population from largest to smallest.

## US-03 — Countries in a Region
Produce all countries in a specified region organised by population from largest to smallest.

## US-04 — Top N Countries in World
Produce the Top N populated countries in the world.

## US-05 — Top N Countries in Continent
Produce the Top N populated countries in a specified continent.

## US-06 — Top N Countries in Region
Produce the Top N populated countries in a specified region.

### Required Country Information

- Code
- Name
- Continent
- Region
- Population
- Capital

---

# 2. City Reports

**Main database tables:** `city` and `country`

`city.CountryCode` references `country.Code`, allowing city records to be filtered using country, continent, and region information.

## US-07 — All Cities in World
Produce all cities in the world organised by population from largest to smallest.

## US-08 — Cities in a Continent
Produce all cities in a specified continent organised by population from largest to smallest.

## US-09 — Cities in a Region
Produce all cities in a specified region organised by population from largest to smallest.

## US-10 — Cities in a Country
Produce all cities in a specified country organised by population from largest to smallest.

## US-11 — Cities in a District
Produce all cities in a specified district organised by population from largest to smallest.

## US-12 — Top N Cities in World
Produce the Top N populated cities in the world.

## US-13 — Top N Cities in Continent
Produce the Top N populated cities in a specified continent.

## US-14 — Top N Cities in Region
Produce the Top N populated cities in a specified region.

## US-31 — Top N Cities in Country
Produce the Top N populated cities in a specified country.

## US-32 — Top N Cities in District
Produce the Top N populated cities in a specified district.

### Required City Information

- Name
- Country
- District
- Population

---

# 3. Capital City Reports

**Main database tables:** `country` and `city`

The supplied database stores the capital city ID in `country.Capital`. This can be matched with `city.ID`.

```text
country.Capital → city.ID
```

## US-15 — All Capital Cities in World
Produce all capital cities in the world organised by population from largest to smallest.

## US-16 — Capital Cities in Continent
Produce all capital cities in a specified continent organised by population from largest to smallest.

## US-17 — Capital Cities in Region
Produce all capital cities in a specified region organised by population from largest to smallest.

## US-18 — Top N Capital Cities in World
Produce the Top N populated capital cities in the world.

## US-19 — Top N Capital Cities in Continent
Produce the Top N populated capital cities in a specified continent.

## US-20 — Top N Capital Cities in Region
Produce the Top N populated capital cities in a specified region.

### Required Capital City Information

- Name
- Country
- Population

---

# 4. Population Reports

**Main database tables:** `country` and `city`

Country population is available from `country.Population`. City population is available from `city.Population`.

These values can be aggregated to calculate total population, population living in cities, and population not living in cities.

## US-21 — Population in Each Continent

For each continent, provide:

- Continent name
- Total population
- Population living in cities
- Population not living in cities
- Percentage living in cities
- Percentage not living in cities

## US-22 — Population in Each Region

For each region, provide:

- Region name
- Total population
- Population living in cities
- Population not living in cities
- Percentage living in cities
- Percentage not living in cities

## US-23 — Population in Each Country

For each country, provide:

- Country name
- Total population
- Population living in cities
- Population not living in cities
- Percentage living in cities
- Percentage not living in cities

## US-24 — World Population
Provide the population of the world.

## US-25 — Continent Population
Provide the population of a specified continent.

## US-26 — Region Population
Provide the population of a specified region.

## US-27 — Country Population
Provide the population of a specified country.

## US-28 — District Population
Provide the population of a specified district.

## US-29 — City Population
Provide the population of a specified city.

---

# 5. Language Report

**Main database tables:** `countrylanguage` and `country`

`countrylanguage.CountryCode` references `country.Code`.

The supplied database stores the percentage of a country's population speaking each language in `countrylanguage.Percentage`.

## US-30 — Language Speakers

Produce a report for:

- Chinese
- English
- Hindi
- Spanish
- Arabic

For each language, provide:

- Language name
- Number of speakers
- Percentage of the world population

The languages should be organised from the largest number of speakers to the smallest.

The speaker population can be calculated using country population and language percentage.

---

# Requirement Summary

| Category | User Stories | Number |
|---|---|---:|
| Country Reports | US-01 – US-06 | 6 |
| City Reports | US-07 – US-14, US-31 – US-32 | 10 |
| Capital City Reports | US-15 – US-20 | 6 |
| Population Reports | US-21 – US-29 | 9 |
| Language Report | US-30 | 1 |
| **Total** | **US-01 – US-32** | **32** |

# Database Mapping Summary

| Category | Database Tables |
|---|---|
| Country | `country` |
| City | `city`, `country` |
| Capital City | `country`, `city` |
| Population | `country`, `city` |
| Language | `countrylanguage`, `country` |

## Verification

- [x] US-01 through US-32 represented
- [x] 32 report requirements included
- [x] Requirements grouped into five categories
- [x] Categories mapped to the supplied `world` database tables
