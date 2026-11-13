# Code Review 2 — Report Verification Matrix

Statuses: `Not tested`, `Pass`, `Fail`, `Blocked`.

| Story | Requirement | Input/filter | Ordering/limit | Expected output | Actual output | Status | Evidence/defect |
|---|---|---|---|---|---|---|---|
| US-01 | All countries by population | None | Population DESC, Code ASC for ties | Country fields incl. capital | | Not tested | |
| US-02 | Countries in a continent | Continent | Population DESC | Selected continent only | | Not tested | |
| US-03 | Countries in a region | Region | Population DESC | Selected region only | | Not tested | |
| US-04 | Top N countries worldwide | N | Population DESC; max N | Top N countries | | Not tested | |
| US-05 | Top N countries in continent | Continent, N | Population DESC; max N | Top N in continent | | Not tested | |
| US-06 | Top N countries in region | Region, N | Population DESC; max N | Top N in region | | Not tested | |
| US-07 | All cities by population | None | Population DESC, ID ASC for ties | City/country/district/population | | Not tested | |
| US-08 | Cities in continent | Continent | Population DESC | Selected continent only | | Not tested | |
| US-09 | Cities in region | Region | Population DESC | Selected region only | | Not tested | |
| US-10 | Cities in country | Country | Population DESC | Selected country only | | Not tested | |
| US-11 | Cities in district | District | Population DESC | Selected district only | | Not tested | |
| US-12 | Top N cities worldwide | N | Population DESC; max N | Top N cities | | Not tested | |
| US-13 | Top N cities in continent | Continent, N | Population DESC; max N | Top N in continent | | Not tested | |
| US-14 | Top N cities in region | Region, N | Population DESC; max N | Top N in region | | Not tested | |
| US-15 | All capital cities | None | Check use case | Capital city rows | | Not tested | |
| US-16 | Capital cities in continent | Continent | Check use case | Capitals in selected continent | | Not tested | |
| US-17 | Capital cities in region | Region | Check use case | Capitals in selected region | | Not tested | |
| US-18 | Top N capitals worldwide | N | Population DESC; max N | Top N capitals | | Not tested | |
| US-19 | Top N capitals in continent | Continent, N | Population DESC; max N | Top N capitals in continent | | Not tested | |
| US-20 | Top N capitals in region | Region, N | Population DESC; max N | Top N capitals in region | | Not tested | |
| US-21 | Population breakdown by continent | None | Check use case | Totals and city/non-city population | | Not tested | |
| US-22 | Population breakdown by region | None | Check use case | Totals and city/non-city population | | Not tested | |
| US-23 | Population breakdown by country | None | Check use case | Totals and city/non-city population | | Not tested | |
| US-24 | World population breakdown | None | Check use case | World total and breakdown | | Not tested | |
| US-25 | Population of a continent | Continent | Single total | Correct total | | Not tested | |
| US-26 | Population of a region | Region | Single total | Correct total | | Not tested | |
| US-27 | Population of a country | Country | Single total | Correct total | | Not tested | |
| US-28 | Population of a district | District | Single total | Correct total | | Not tested | |
| US-29 | Population of a city | City | Single total | Correct total | | Not tested | |
| US-30 | Major language speakers | None | Speaker population DESC | Language, speakers, percentage | | Not tested | |
| US-31 | Top N cities in country | Country, N | Population DESC; max N | Top N in country | | Not tested | |
| US-32 | Top N cities in district | District, N | Population DESC; max N | Top N in district | | Not tested | |

## Summary

-Stories tested:
-Passed:
-Failed:
-Blocked:
-Not tested:
-Defect issues:
-Tested commit SHA:
