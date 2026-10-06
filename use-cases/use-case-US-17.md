# USE CASE: 17 Capital cities in region

## CHARACTERISTIC INFORMATION

### Goal in Context

As a system user, I want all capital cities in a region ordered by largest population to smallest, so that I can compare capitals within a region.

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

A request is made for the capital cities in region report.

## MAIN SUCCESS SCENARIO

1. The system user requests the capital cities in region report.
2. The system user provides the region.
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
