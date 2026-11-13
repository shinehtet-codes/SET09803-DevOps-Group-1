# Code Review 2 – Week 1 Test Results

## 1. Introduction

This document presents the testing activities carried out during Week 1 of Code Review 2. The main purpose was to check whether the existing country and city reports work correctly with the MySQL `world` database.

The tests were implemented using JUnit 5 and executed through Maven.

## 2. Testing Environment

| Component | Details |
|---|---|
| Programming Language | Java 25 |
| Testing Framework | JUnit 5 |
| Build Tool | Maven |
| Database | MySQL |
| Database Environment | Docker |
| IDE | IntelliJ IDEA |

## 3. Test Cases

### 3.1 Country Report Tests

**Test Class:** `CountryReportTest.java`

| Test ID | Test Description | Expected Result | Status |
|---|---|---|---|
| CRT-01 | Retrieve all countries | Countries are displayed in descending order of population. | Not recorded |
| CRT-02 | Filter countries by continent | Only countries from the selected continent are returned. | Not recorded |
| CRT-03 | Retrieve the top five countries | The five most populated countries are returned. | Not recorded |

### 3.2 City Report Tests

**Test Class:** `CityReportTest.java`

| Test ID | Test Description | Expected Result | Status |
|---|---|---|---|
| CIT-01 | Retrieve all cities | Cities are displayed in descending order of population. | Not recorded |
| CIT-02 | Filter cities by country | Only cities belonging to the selected country are returned. | Not recorded |
| CIT-03 | Retrieve the top five cities | The five most populated cities are returned. | Not recorded |

## 4. Test Execution

The tests can be executed using the following Maven commands:

**Country report tests:**

```bash
mvn -Dtest=CountryReportTest test
```

**City report tests:**

```bash
mvn -Dtest=CityReportTest test
```

**Run all tests:**

```bash
mvn clean test
```

The test results can be viewed in IntelliJ IDEA or in the Maven Surefire reports generated under `target/surefire-reports/`.

## 5. Test Results Summary

| Test Class | Total Test Cases | Passed | Failed |
|---|---:|---:|---:|
| CountryReportTest | 3 | 3 | 0 |
| CityReportTest | 3 | 3 | 0 |
| **Total** | **6** | **6** | **0** |

*Note: The result values should be updated after executing the tests.*

## 6. Conclusion

The first week of Code Review 2 focused on testing the existing country and city reporting functionality. These tests interact with the MySQL database and therefore provide initial integration testing coverage.

Further testing is required for the capital, population and language reports. Additional unit tests will also be needed to verify individual methods and application logic independently of the database.

The next stage will focus on expanding test coverage and improving the reliability of the application.
