package org.devops;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the console menu in {@link App}.
 *
 * These tests never open a database connection. The App is created with a
 * Scanner over fixed test input, and System.out is captured so the printed
 * menu text can be checked.
 */
class AppMenuUnitTest {

    private PrintStream originalOut;
    private ByteArrayOutputStream captured;

    @BeforeEach
    void captureOutput() {
        originalOut = System.out;
        captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured, true, StandardCharsets.UTF_8));
    }

    @AfterEach
    void restoreOutput() {
        System.setOut(originalOut);
    }

    /**
     * Runs the menu with the given keyboard input and returns everything printed.
     */
    private String runMenuWith(String input) {
        Scanner scanner = new Scanner(
                new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8
        );
        App app = new App(scanner);
        app.runMenu();
        return captured.toString(StandardCharsets.UTF_8);
    }

    @Test
    void mainMenuShowsAllCategoriesAndDisconnectedStatus() {
        String output = runMenuWith("0\n");

        assertTrue(output.contains("Country Reports"));
        assertTrue(output.contains("City Reports"));
        assertTrue(output.contains("Capital City Reports"));
        assertTrue(output.contains("Population Reports"));
        assertTrue(output.contains("Language Reports"));
        assertTrue(output.contains("Database Status : DISCONNECTED"));
        assertTrue(output.contains("Thank you for using the application. Goodbye!"));
    }

    @Test
    void nonNumericAndOutOfRangeCategoriesAreRejected() {
        String output = runMenuWith("abc\n9\n0\n");

        assertTrue(output.contains("Please enter a valid whole number."));
        assertTrue(output.contains("Invalid category. Please select a number from 0 to 5."));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void categoryMenuListsReportsAndReturnsToMainMenu() {
        String output = runMenuWith("1\n0\n0\n");

        assertTrue(output.contains("COUNTRY REPORTS"));
        assertTrue(output.contains("[US-01] All Countries by Population"));
        assertTrue(output.contains("[US-06] Top N Countries in a Region"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void invalidReportSelectionIsRejected() {
        String output = runMenuWith("2\n99\n0\n0\n");

        assertTrue(output.contains("Invalid report selection. Choose a listed option."));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void zeroAndNegativeNAreRejectedBeforeReportRuns() {
        // Category 1, report 4 (Top N countries), N = -3 (rejected), N = 0
        // (rejected), N = 5 (accepted), ENTER, back, exit.
        String output = runMenuWith("1\n4\n-3\n0\n5\n\n0\n0\n");

        assertTrue(output.contains("N must be greater than zero."));
        assertTrue(output.contains("REPORT EXECUTION FINISHED"));
        assertTrue(output.contains("Goodbye!"));
    }

    @Test
    void blankTextInputIsRejected() {
        // Category 1, report 3 (countries in a region), blank region
        // (rejected), "Caribbean", ENTER, back, exit.
        String output = runMenuWith("1\n3\n\nCaribbean\n\n0\n0\n");

        assertTrue(output.contains("This field cannot be empty."));
        assertTrue(output.contains("REPORT EXECUTION FINISHED"));
    }

    @Test
    void reportHeaderUsesTheSameTitleAsTheMenu() {
        String output = runMenuWith("1\n4\n3\n\n0\n0\n");

        assertTrue(output.contains("[US-04] Top N Countries Worldwide"));
    }

    @Test
    void emptyInputStopsTheMenuWithoutCrashing() {
        String output = assertDoesNotThrow(() -> runMenuWith(""));

        assertFalse(output.contains("Goodbye!"),
                "Closed input should stop the menu without the normal exit message.");
    }

    @Test
    void inputEndingInsideAReportStopsTheMenuWithoutCrashing() {
        // Category 1, report 2 asks for a continent, but the input ends.
        assertDoesNotThrow(() -> runMenuWith("1\n2\n"));
    }
}
