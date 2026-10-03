# USE CASE: 30 Language speakers

## CHARACTERISTIC INFORMATION

### Goal in Context

As a system user, I want the number of people who speak Chinese, English, Hindi, Spanish, and Arabic from greatest to smallest, including the percentage of world population, so that I can report language demographics.

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

A request is made for the language speakers report.

## MAIN SUCCESS SCENARIO

1. The system user requests the language speakers report.
2. The system retrieves the required population information from the database.
3. The system orders the results from largest to smallest.
4. The system produces the requested report.

## EXTENSIONS

3. **Required data is unavailable**:
    1. No report is produced.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 0.6.0-beta

---
