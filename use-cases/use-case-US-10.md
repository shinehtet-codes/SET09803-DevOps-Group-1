# USE CASE: 10 Cities in a country

## CHARACTERISTIC INFORMATION

### Goal in Context

As a system user, I want all cities in a country ordered by largest population to smallest, so that I can compare cities within a country.

### Scope

Population Information System.

### Level

Primary task.

### Preconditions

We know the country.  The population database contains the data required for the report.

### Success End Condition

The requested report is available to the system user.

### Failed End Condition

No report is produced.

### Primary Actor

System User.

### Trigger

A request is made for the cities in a country report.

## MAIN SUCCESS SCENARIO

1. The system user requests the cities in a country report.
2. The system user provides the country.
3. The system retrieves the required population information from the database.
4. The system orders the results from largest to smallest.
5. The system produces the requested report.

## EXTENSIONS

2. **Input is invalid or does not exist**:
    1. The system user is informed that the requested report cannot be produced with the supplied input.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 0.1-alpha-4

---
