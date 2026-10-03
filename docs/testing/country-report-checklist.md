# Country Report Verification Checklist

## Purpose

This document defines the initial manual verification checklist for Country Reports US-01 through US-06.

The checks are based on the supplied `world` database. Country data is stored in the `country` table.

Important fields used by these reports are:

- `Code`
- `Name`
- `Continent`
- `Region`
- `Population`
- `Capital`

Testing of the Java implementation will begin after the country reports have been developed.

---

# Database Checks Before Report Testing

Before testing country reports:

- [ ] The `world` database has been imported successfully.
- [ ] The `country` table exists.
- [ ] Country records can be queried.
- [ ] `Code` values are available.
- [ ] `Name` values are available.
- [ ] `Continent` values are available.
- [ ] `Region` values are available.
- [ ] `Population` values are available.
- [ ] `Capital` values are available.

The database defines the following continent values:

- Asia
- Europe
- North America
- Africa
- Oceania
- Antarctica
- South America

---

# General Country Report Checks

For country reports:

- [ ] Report executes without an error.
- [ ] Country code is displayed.
- [ ] Country name is displayed.
- [ ] Continent is displayed.
- [ ] Region is displayed.
- [ ] Population is displayed.
- [ ] Capital is displayed.
- [ ] Population values correspond to database values.
- [ ] Results are ordered by population from largest to smallest where required.
- [ ] No unexpected records are included.
- [ ] Output is readable.

---

# US-01 — All Countries by Population

## Expected Behaviour

Return all countries in the world organised by population from largest to smallest.

## Verification Checklist

- [ ] Report executes successfully.
- [ ] All countries from the `country` table are represented.
- [ ] Results are ordered by `Population` from largest to smallest.
- [ ] `Code` is displayed.
- [ ] `Name` is displayed.
- [ ] `Continent` is displayed.
- [ ] `Region` is displayed.
- [ ] `Population` is displayed.
- [ ] Capital information is displayed.
- [ ] No country is incorrectly excluded.

---

# US-02 — Countries in a Continent

## Expected Behaviour

Return countries belonging to a specified continent organised by population from largest to smallest.

## Verification Checklist

- [ ] A continent can be specified.
- [ ] Report executes successfully.
- [ ] Only countries with the selected `Continent` value are returned.
- [ ] Countries from other continents are excluded.
- [ ] Results are ordered by `Population` from largest to smallest.
- [ ] Required country information is displayed.

## Suggested Manual Checks

Test with more than one continent, for example:

- [ ] Asia
- [ ] Europe
- [ ] Africa

---

# US-03 — Countries in a Region

## Expected Behaviour

Return countries belonging to a specified region organised by population from largest to smallest.

## Verification Checklist

- [ ] A region can be specified.
- [ ] Report executes successfully.
- [ ] Only countries with the selected `Region` value are returned.
- [ ] Countries from other regions are excluded.
- [ ] Results are ordered by `Population` from largest to smallest.
- [ ] Required country information is displayed.

---

# US-04 — Top N Countries in World

## Expected Behaviour

Return the Top N populated countries in the world.

## Verification Checklist

- [ ] A value for N can be specified.
- [ ] Report executes successfully.
- [ ] The report returns no more than N countries.
- [ ] Returned countries have the highest population values.
- [ ] Results are ordered by `Population` from largest to smallest.
- [ ] Required country information is displayed.

---

# US-05 — Top N Countries in Continent

## Expected Behaviour

Return the Top N populated countries in a specified continent.

## Verification Checklist

- [ ] A continent can be specified.
- [ ] A value for N can be specified.
- [ ] Report executes successfully.
- [ ] Only countries from the selected continent are returned.
- [ ] Countries from other continents are excluded.
- [ ] The report returns no more than N countries.
- [ ] Returned countries have the highest populations in that continent.
- [ ] Results are ordered by `Population` from largest to smallest.
- [ ] Required country information is displayed.

---

# US-06 — Top N Countries in Region

## Expected Behaviour

Return the Top N populated countries in a specified region.

## Verification Checklist

- [ ] A region can be specified.
- [ ] A value for N can be specified.
- [ ] Report executes successfully.
- [ ] Only countries from the selected region are returned.
- [ ] Countries from other regions are excluded.
- [ ] The report returns no more than N countries.
- [ ] Returned countries have the highest populations in that region.
- [ ] Results are ordered by `Population` from largest to smallest.
- [ ] Required country information is displayed.

---

# Top N Boundary Checks

When Top N functionality is implemented, test several values:

- [ ] N = 1
- [ ] N = 5
- [ ] N = 10
- [ ] N smaller than the available number of records
- [ ] N equal to the available number of records

Invalid N behaviour should be tested according to the agreed application requirements once input validation has been implemented.

---

# Data Accuracy Checks

For a sample of report rows:

- [ ] Compare the country code with `country.Code`.
- [ ] Compare the country name with `country.Name`.
- [ ] Compare the continent with `country.Continent`.
- [ ] Compare the region with `country.Region`.
- [ ] Compare the population with `country.Population`.
- [ ] Verify that capital information corresponds to the country's `Capital` value and the related city record when capital-name output is implemented.

---

# Testing Status

| User Story | Report | Status |
|---|---|---|
| US-01 | All Countries by Population | Not Implemented |
| US-02 | Countries in a Continent | Not Implemented |
| US-03 | Countries in a Region | Not Implemented |
| US-04 | Top N Countries in World | Not Implemented |
| US-05 | Top N Countries in Continent | Not Implemented |
| US-06 | Top N Countries in Region | Not Implemented |

## Current Status

The supplied `world` database is now available, so the tester can use its actual schema and data as the reference when report implementation begins.
