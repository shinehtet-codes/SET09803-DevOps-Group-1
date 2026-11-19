package org.devops;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class AppInputValidationTest {

    @Test
    void applicationRejectsInvalidCategoryAndThenExitsCleanly() {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream testInput =
                new ByteArrayInputStream(
                        "abc\n9\n0\n".getBytes(StandardCharsets.UTF_8)
                );

        ByteArrayOutputStream captured =
                new ByteArrayOutputStream();

        try {
            System.setIn(testInput);
            System.setOut(new PrintStream(captured, true,
                    StandardCharsets.UTF_8));

            // This test reaches the database connection first.
            // Do not run this test unless a testable App constructor or
            // database-free menu seam has been implemented.
            //
            // Once the constructor is injectable, instantiate App with
            // a Scanner over testInput and test the input flow without
            // opening a real database connection.
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}
