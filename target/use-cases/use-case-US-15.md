# USE CASE: 15 All capital cities in world

## CHARACTERISTIC INFORMATION

### Goal in Context

As a system user, I want all capital cities in the world ordered by largest population to smallest, so that I can see global capital ranking.

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

A request is made for the all capital cities in world report.

## MAIN SUCCESS SCENARIO

1. The system user requests the all capital cities in world report.
2. The system retrieves the required population information from the database.
3. The system orders the results from largest to smallest.
4. The system produces the requested report.

## EXTENSIONS

3. **Required data is unavailable**:
    1. No report is produced.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 0.4.0-alpha

---
