# World Database Overview

## Purpose

The Population Reporting System uses the MySQL `world` database supplied for the project.

The downloaded database is provided in `world.sql` and contains the three tables required by the reporting system:

- `country`
- `city`
- `countrylanguage`

These tables provide the source data for the country, city, capital city, population, and language reports.

---

# 1. City Table

The `city` table stores information about cities.

## Table Structure

| Field | SQL Type | Null | Description |
|---|---|---|---|
| `ID` | `int` | No | Unique city ID and primary key |
| `Name` | `char(35)` | No | City name |
| `CountryCode` | `char(3)` | No | Code of the country containing the city |
| `District` | `char(20)` | No | District/state/area containing the city |
| `Population` | `int` | No | Population of the city |

## Keys and Relationships

- Primary Key: `ID`
- Foreign Key: `CountryCode`
- `city.CountryCode` references `country.Code`

```text
city.CountryCode → country.Code
```

## Project Usage

The `city` table will be used for:

- All-city reports
- City reports by continent, region, country, and district
- Top N city reports
- Capital city reports
- City population reports
- District population reports
- Calculating the population living in cities

---

# 2. Country Table

The `country` table stores information about countries.

## Table Structure

| Field | SQL Type | Null | Description |
|---|---|---|---|
| `Code` | `char(3)` | No | Country code and primary key |
| `Name` | `char(52)` | No | Country name |
| `Continent` | `enum` | No | Continent |
| `Region` | `char(26)` | No | Region |
| `SurfaceArea` | `decimal(10,2)` | No | Surface area |
| `IndepYear` | `smallint` | Yes | Independence year |
| `Population` | `int` | No | Country population |
| `LifeExpectancy` | `decimal(3,1)` | Yes | Life expectancy |
| `GNP` | `decimal(10,2)` | Yes | Gross National Product |
| `GNPOld` | `decimal(10,2)` | Yes | Previous GNP value |
| `LocalName` | `char(45)` | No | Local country name |
| `GovernmentForm` | `char(45)` | No | Form of government |
| `HeadOfState` | `char(60)` | Yes | Head of state |
| `Capital` | `int` | Yes | ID of the capital city |
| `Code2` | `char(2)` | No | Two-character country code |

## Continent Values

The database defines these continent values:

- Asia
- Europe
- North America
- Africa
- Oceania
- Antarctica
- South America

## Project Usage

The most important fields for the coursework reports are:

- `Code`
- `Name`
- `Continent`
- `Region`
- `Population`
- `Capital`

The table will be used for:

- Country reports
- Country reports by continent and region
- Top N country reports
- Population reports
- Connecting cities to continents and regions
- Identifying capital cities
- Calculating language-speaker populations

---

# 3. CountryLanguage Table

The `countrylanguage` table stores information about languages spoken in each country.

## Table Structure

| Field | SQL Type | Null | Description |
|---|---|---|---|
| `CountryCode` | `char(3)` | No | Country code |
| `Language` | `char(30)` | No | Language name |
| `IsOfficial` | `enum('T','F')` | No | Whether the language is official |
| `Percentage` | `decimal(4,1)` | No | Percentage of the country's population speaking the language |

## Keys and Relationships

The table has a composite primary key:

```text
CountryCode + Language
```

`CountryCode` is also a foreign key:

```text
countrylanguage.CountryCode → country.Code
```

## Project Usage

This table will be used for the language report covering:

- Chinese
- English
- Hindi
- Spanish
- Arabic

The number of speakers can be calculated by combining `countrylanguage.Percentage` with `country.Population`.

Conceptually:

```text
language speakers =
country population × language percentage / 100
```

---

# 4. Database Relationships

## Country → City

```text
country.Code
     ↑
     │
city.CountryCode
```

This relationship is required when a city report needs country, continent, or region information.

## Country → CountryLanguage

```text
country.Code
     ↑
     │
countrylanguage.CountryCode
```

This relationship connects a language percentage to the population of its country.

## Country → Capital City

The `country.Capital` field stores the ID of the country's capital city.

```text
country.Capital → city.ID
```

This relationship can be used to produce capital city reports.

---

# 5. Relationship Summary

```text
                     country
              ┌──────────────────┐
              │ Code (PK)        │
              │ Name             │
              │ Continent        │
              │ Region           │
              │ Population       │
              │ Capital          │
              └──────────────────┘
                  │          │
        Code      │          │ Capital
                  │          │
                  ▼          ▼
       countrylanguage      city
       ┌────────────────┐   ┌─────────────────┐
       │ CountryCode    │   │ ID (PK)         │
       │ Language       │   │ Name            │
       │ IsOfficial     │   │ CountryCode     │
       │ Percentage     │   │ District        │
       └────────────────┘   │ Population      │
                            └─────────────────┘
```

---

# 6. Report-to-Table Mapping

| Report Type | Main Tables |
|---|---|
| Country reports | `country` |
| City reports | `city`, `country` |
| Capital city reports | `country`, `city` |
| Population reports | `country`, `city` |
| Language report | `countrylanguage`, `country` |

---

# Summary

The supplied `world.sql` database contains the three tables needed by the Population Reporting System. The `country` table provides country and population information, the `city` table provides city and district information, and the `countrylanguage` table provides language percentages.

The relationships between these tables allow the application to generate all required report categories.
