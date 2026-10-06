# USE CASE: 5 Top N countries in continent

## CHARACTERISTIC INFORMATION

### Goal in Context

As a system user, I want the top N populated countries in a continent (N provided by me), so that I can focus on top countries in a continent.

### Scope

Population Information System.

### Level

Primary task.

### Preconditions

We know the continent.  We know the value of N.  The population database contains the data required for the report.

### Success End Condition

The requested report is available to the system user.

### Failed End Condition

No report is produced.

### Primary Actor

System User.

### Trigger

A request is made for the top n countries in continent report.

## MAIN SUCCESS SCENARIO

1. The system user requests the top n countries in continent report.
2. The system user provides the value of N.
3. The system user provides the continent.
4. The system retrieves the required population information from the database.
5. The system limits the results to the requested top N records.
6. The system produces the requested report.

## EXTENSIONS

2. **Input is invalid or does not exist**:
    1. The system user is informed that the requested report cannot be produced with the supplied input.

## SUB-VARIATIONS

None.

## SCHEDULE

**DUE DATE**: Release 0.1-alpha-5

---
