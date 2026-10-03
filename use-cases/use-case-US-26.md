# USE CASE: 26 Region population

## CHARACTERISTIC INFORMATION

### Goal in Context

As a system user, I want the population of a region, so that I can report region population.

### Scope

Population Information System.

### Level

Primary task.

### Preconditions

We know the region.  The population database contains the data required for the report.

### Success End Condition

The requested report is available to the system user.

### Failed End Condition

No report is produced.

### Primary Actor

System User.

### Trigger

A request is made for the region population report.

## MAIN SUCCESS SCENARIO

1. The system user requests the region population report.
2. The system user provides the region.
3. The system retrieves the required population information from the database.
4. The system produces the requested report.

## EXTENSIONS

2. **Input is invalid or does not exist**:
    1. The system user is informed that the requested report cannot be produced with the supplied input.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 0.5.0-beta

---
