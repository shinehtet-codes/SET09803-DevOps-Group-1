# USE CASE: 22 Population in each region

## CHARACTERISTIC INFORMATION

### Goal in Context

As a system user, I want the population of people, people living in cities, and people not living in cities in each region, so that I can understand urbanisation by region.

### Scope

Population Information System.

### Level

Primary task.

### Preconditions

The population database contains the data required for the report.

### Success End Condition

The requested report is available to the system user.

### Failed End Condition

No report is produced.

### Primary Actor

System User.

### Trigger

A request is made for the population in each region report.

## MAIN SUCCESS SCENARIO

1. The system user requests the population in each region report.
2. The system retrieves the required population information from the database.
3. The system produces the requested report.

## EXTENSIONS

3. **Required data is unavailable**:
    1. No report is produced.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 0.5.0-beta

---
