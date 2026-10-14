package org.devops;

import org.devops.report.*;
import org.devops.model.PopulationSummary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class App {

    private Connection con;

    private final Scanner scanner = new Scanner(System.in);

    private final CountryReport countries = new CountryReport();
    private final CityReport cities = new CityReport();
    private final CapitalReport capitals = new CapitalReport();
    private final PopulationReport populations = new PopulationReport();
    private final LanguageReport languages = new LanguageReport();

    private static final String VERSION = "v0.1-alpha-5";

    private static final int WIDTH = 76;

    private static final String LINE = "=".repeat(WIDTH);
    private static final String DIVIDER = "-".repeat(WIDTH);

    private boolean inputClosed = false;

    /*
     * Report titles are defined in one place.
     * These titles are used by both the menu and report output.
     */
    private static final String[] REPORT_TITLES = {
            "",
            "All Countries by Population",                 // US-01
            "Countries in a Continent",                    // US-02
            "Countries in a Region",                       // US-03
            "Top N Countries Worldwide",                   // US-04
            "Top N Countries in a Continent",              // US-05
            "Top N Countries in a Region",                 // US-06
            "All Cities by Population",                    // US-07
            "Cities in a Continent",                       // US-08
            "Cities in a Region",                          // US-09
            "Cities in a Country",                         // US-10
            "Cities in a District",                        // US-11
            "Top N Cities Worldwide",                      // US-12
            "Top N Cities in a Continent",                 // US-13
            "Top N Cities in a Region",                    // US-14
            "All Capital Cities by Population",            // US-15
            "Capital Cities in a Continent",               // US-16
            "Capital Cities in a Region",                  // US-17
            "Top N Capital Cities Worldwide",              // US-18
            "Top N Capital Cities in a Continent",         // US-19
            "Top N Capital Cities in a Region",            // US-20
            "Population Breakdown by Continent",           // US-21
            "Population Breakdown by Region",              // US-22
            "Population Breakdown by Country",             // US-23
            "World Population Breakdown",                  // US-24
            "Population of a Continent",                   // US-25
            "Population of a Region",                      // US-26
            "Population of a Country",                     // US-27
            "Population of a District",                    // US-28
            "Population of a City",                        // US-29
            "Major Language Speakers",                     // US-30
            "Top N Cities in a Country",                   // US-31
            "Top N Cities in a District"                   // US-32
    };

    /*
     * Report IDs belonging to each category.
     *
     * Keeping these mappings separate from the report titles
     * makes the menu easier to maintain.
     */
    private static final int[][] CATEGORY_REPORTS = {
            {},
            {1, 2, 3, 4, 5, 6},
            {7, 8, 9, 10, 11, 12, 13, 14, 31, 32},
            {15, 16, 17, 18, 19, 20},
            {21, 22, 23, 24, 25, 26, 27, 28, 29},
            {30}
    };

    private static final String[] CATEGORY_TITLES = {
            "",
            "Country Reports",
            "City Reports",
            "Capital City Reports",
            "Population Reports",
            "Language Reports"
    };

    public static void main(String[] args) {

        App app = new App();

        app.connect();

        try {
            if (app.getConnection() != null) {
                app.runMenu();
            } else {
                System.out.println(
                        "Unable to connect to the database. Application stopped."
                );
            }
        } finally {
            app.disconnect();
        }
    }

    public Connection getConnection() {
        return con;
    }

    /*
     * DATABASE CONNECTION
     */
    public void connect() {

        String url = System.getenv().getOrDefault(
                "DB_URL",
                "jdbc:mysql://db:3306/world?allowPublicKeyRetrieval=true&useSSL=false"
        );

        String user = System.getenv().getOrDefault(
                "DB_USER",
                "root"
        );

        String password = System.getenv().getOrDefault(
                "DB_PASSWORD",
                "example"
        );

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            for (int attempt = 1; attempt <= 10; attempt++) {

                try {
                    con = DriverManager.getConnection(
                            url,
                            user,
                            password
                    );

                    printMessage(
                            "Successfully connected to the world database."
                    );

                    return;

                } catch (SQLException e) {

                    System.out.println(
                            "Connection attempt "
                                    + attempt
                                    + " failed: "
                                    + e.getMessage()
                    );

                    if (attempt == 10) {
                        break;
                    }

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e1) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

        } catch (ClassNotFoundException e) {

            printError(
                    "MySQL JDBC driver was not found: " + e.getMessage()
            );
        }
    }

    public void disconnect() {

        if (con == null) {
            return;
        }

        try {
            con.close();
            printMessage("Disconnected from database.");

        } catch (SQLException e) {
            printError(
                    "Error closing database connection: " + e.getMessage()
            );
        }
    }

    /*
     * MAIN MENU
     */
    private void runMenu() {

        while (!inputClosed) {

            showMainMenu();

            int category = readInt("Select a category: ");

            if (inputClosed) {
                return;
            }

            if (category == 0) {
                printMessage(
                        "Thank you for using the application. Goodbye!"
                );
                return;
            }

            if (category >= 1 && category <= 5) {
                showCategoryMenu(category);
            } else {
                printError(
                        "Invalid category. Please select a number from 0 to 5."
                );
            }
        }
    }

    private void showMainMenu() {

        System.out.println();
        System.out.println(LINE);
        System.out.println(center("SET09803 - DEVOPS GROUP 1"));
        System.out.println(center("WORLD POPULATION REPORTING SYSTEM"));
        System.out.println(center(VERSION));
        System.out.println(LINE);

        System.out.println();
        System.out.println("  SELECT REPORT CATEGORY");
        System.out.println(DIVIDER);

        for (int category = 1; category <= 5; category++) {

            int reportCount = CATEGORY_REPORTS[category].length;

            System.out.printf(
                    "  [%02d] %-40s %2d %s%n",
                    category,
                    CATEGORY_TITLES[category],
                    reportCount,
                    reportCount == 1 ? "report" : "reports"
            );
        }

        System.out.println(DIVIDER);
        System.out.println("  [00] Exit Application");
        System.out.println();

        System.out.println(
                "  Database Status : "
                        + (isConnected() ? "CONNECTED" : "DISCONNECTED")
        );

        System.out.println(LINE);
    }

    /*
     * CATEGORY MENU
     */
    private void showCategoryMenu(int category) {

        while (!inputClosed) {

            printCategoryHeader(category);

            int[] reportIds = CATEGORY_REPORTS[category];

            for (int i = 0; i < reportIds.length; i++) {

                int menuNumber = i + 1;
                int reportId = reportIds[i];

                printReportOption(menuNumber, reportId);
            }

            System.out.println();
            System.out.println(DIVIDER);
            System.out.println("  [00] Back to Main Menu");
            System.out.println(LINE);

            int selection = readInt("Select a report: ");

            if (inputClosed) {
                return;
            }

            if (selection == 0) {
                return;
            }

            if (selection < 1 || selection > reportIds.length) {

                printError(
                        "Invalid report selection. Choose a listed option."
                );

                continue;
            }

            int reportId = reportIds[selection - 1];

            printReportHeader(reportId);

            boolean successful = false;

            try {

                executeReport(reportId);

                successful = !inputClosed;

            } catch (RuntimeException e) {

                if (inputClosed) {
                    return;
                }

                printError(
                        "Report execution failed: " + e.getMessage()
                );
            }

            if (inputClosed) {
                return;
            }

            printReportFooter(successful);

            waitForEnter();
        }
    }

    /*
     * REPORT TITLE FORMATTING
     *
     * Example:
     * [US-05] Top N Countries in a Continent
     */
    private String getReportTitle(int reportId) {

        if (reportId < 1 || reportId >= REPORT_TITLES.length) {
            return "[UNKNOWN] Unknown Report";
        }

        return String.format(
                "[US-%02d] %s",
                reportId,
                REPORT_TITLES[reportId]
        );
    }

    /*
     * CATEGORY HEADER
     */
    private void printCategoryHeader(int category) {

        System.out.println();
        System.out.println(LINE);
        System.out.println(
                center(CATEGORY_TITLES[category].toUpperCase())
        );
        System.out.println(LINE);
        System.out.println();
    }

    /*
     * REPORT OPTION
     *
     * Example:
     * [05] [US-05] Top N Countries in a Continent
     */
    private void printReportOption(int menuNumber, int reportId) {

        System.out.printf(
                "  [%02d] %s%n",
                menuNumber,
                getReportTitle(reportId)
        );
    }

    /*
     * REPORT HEADER
     *
     * Uses exactly the same title as the category menu.
     */
    private void printReportHeader(int reportId) {

        System.out.println();
        System.out.println(LINE);
        System.out.println(center(getReportTitle(reportId)));
        System.out.println(LINE);
        System.out.println();
    }

    /*
     * REPORT FOOTER
     */
    private void printReportFooter(boolean successful) {

        System.out.println();
        System.out.println(DIVIDER);

        if (successful) {
            System.out.println(
                    center("REPORT EXECUTION FINISHED")
            );
        } else {
            System.out.println(
                    center("REPORT EXECUTION FAILED")
            );
        }

        System.out.println(DIVIDER);
    }

    /*
     * CONSOLE UTILITIES
     */
    private void printMessage(String message) {
        System.out.println();
        System.out.println("[INFO] " + message);
    }

    private void printError(String message) {
        System.out.println();
        System.out.println("[ERROR] " + message);
        System.out.println();
    }

    private String center(String text) {

        if (text.length() >= WIDTH) {
            return text;
        }

        int padding = (WIDTH - text.length()) / 2;

        return " ".repeat(padding) + text;
    }

    private boolean isConnected() {

        if (con == null) {
            return false;
        }

        try {
            return !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /*
     * INPUT VALIDATION
     */
    private int readInt(String prompt) {

        while (!inputClosed) {

            System.out.print("\n  > " + prompt);

            if (!scanner.hasNextLine()) {
                inputClosed = true;
                return 0;
            }

            String value = scanner.nextLine().trim();

            try {
                return Integer.parseInt(value);

            } catch (NumberFormatException e) {
                printError("Please enter a valid whole number.");
            }
        }

        return 0;
    }

    private String readText(String prompt) {

        while (!inputClosed) {

            System.out.print("\n  > " + prompt);

            if (!scanner.hasNextLine()) {

                inputClosed = true;

                throw new IllegalStateException(
                        "Console input is no longer available."
                );
            }

            String value = scanner.nextLine().trim();

            if (!value.isBlank()) {
                return value;
            }

            printError("This field cannot be empty.");
        }

        throw new IllegalStateException(
                "Console input is no longer available."
        );
    }

    private int readN() {

        while (!inputClosed) {

            int n = readInt("Enter positive N: ");

            if (inputClosed) {
                throw new IllegalStateException(
                        "Console input is no longer available."
                );
            }

            if (n > 0) {
                return n;
            }

            printError("N must be greater than zero.");
        }

        throw new IllegalStateException(
                "Console input is no longer available."
        );
    }

    private void waitForEnter() {

        System.out.print(
                "\n  Press ENTER to return to the category menu..."
        );

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        } else {
            inputClosed = true;
        }
    }

    /*
     * POPULATION OUTPUT
     */
    private void printPopulation(Long value) {

        if (value == null) {
            printError("No matching population value was found.");
        } else {
            System.out.printf(
                    "%n  Population: %,d%n",
                    value
            );
        }
    }

    /*
     * REPORT EXECUTION
     *
     * All existing report methods are preserved.
     */
    private void executeReport(int choice) {

        switch (choice) {

            // US-01: All countries
            case 1:
                countries.printCountries(
                        countries.getAllCountries(con)
                );
                break;

            // US-02: Countries in a continent
            case 2:
                countries.printCountries(
                        countries.getCountriesByContinent(
                                con,
                                readText("Enter continent: ")
                        )
                );
                break;

            // US-03: Countries in a region
            case 3:
                countries.printCountries(
                        countries.getCountriesByRegion(
                                con,
                                readText("Enter region: ")
                        )
                );
                break;

            // US-04: Top N countries worldwide
            case 4:
                countries.printCountries(
                        countries.getTopNCountriesInWorld(
                                con,
                                readN()
                        )
                );
                break;

            // US-05: Top N countries in a continent
            case 5:
                countries.printCountries(
                        countries.getTopNCountriesByContinent(
                                con,
                                readText("Enter continent: "),
                                readN()
                        )
                );
                break;

            // US-06: Top N countries in a region
            case 6:
                countries.printCountries(
                        countries.getTopNCountriesByRegion(
                                con,
                                readText("Enter region: "),
                                readN()
                        )
                );
                break;

            // US-07: All cities
            case 7:
                cities.printCities(
                        cities.getAllCities(con)
                );
                break;

            // US-08: Cities in a continent
            case 8:
                cities.printCities(
                        cities.getCitiesByContinent(
                                con,
                                readText("Enter continent: ")
                        )
                );
                break;

            // US-09: Cities in a region
            case 9:
                cities.printCities(
                        cities.getCitiesByRegion(
                                con,
                                readText("Enter region: ")
                        )
                );
                break;

            // US-10: Cities in a country
            case 10:
                cities.printCities(
                        cities.getCitiesByCountry(
                                con,
                                readText("Enter country: ")
                        )
                );
                break;

            // US-11: Cities in a district
            case 11:
                cities.printCities(
                        cities.getCitiesByDistrict(
                                con,
                                readText("Enter district: ")
                        )
                );
                break;

            // US-12: Top N cities worldwide
            case 12:
                cities.printCities(
                        cities.getTopNCitiesInWorld(
                                con,
                                readN()
                        )
                );
                break;

            // US-13: Top N cities in a continent
            case 13:
                cities.printCities(
                        cities.getTopNCitiesByContinent(
                                con,
                                readText("Enter continent: "),
                                readN()
                        )
                );
                break;

            // US-14: Top N cities in a region
            case 14:
                cities.printCities(
                        cities.getTopNCitiesByRegion(
                                con,
                                readText("Enter region: "),
                                readN()
                        )
                );
                break;

            // US-15: All capital cities
            case 15:
                capitals.printCapitalCities(
                        capitals.getAllCapitalCities(con)
                );
                break;

            // US-16: Capital cities in a continent
            case 16:
                capitals.printCapitalCities(
                        capitals.getCapitalCitiesByContinent(
                                con,
                                readText("Enter continent: ")
                        )
                );
                break;

            // US-17: Capital cities in a region
            case 17:
                capitals.printCapitalCities(
                        capitals.getCapitalCitiesByRegion(
                                con,
                                readText("Enter region: ")
                        )
                );
                break;

            // US-18: Top N capital cities worldwide
            case 18:
                capitals.printCapitalCities(
                        capitals.getTopNCapitalCitiesInWorld(
                                con,
                                readN()
                        )
                );
                break;

            // US-19: Top N capital cities in a continent
            case 19:
                capitals.printCapitalCities(
                        capitals.getTopNCapitalCitiesByContinent(
                                con,
                                readText("Enter continent: "),
                                readN()
                        )
                );
                break;

            // US-20: Top N capital cities in a region
            case 20:
                capitals.printCapitalCities(
                        capitals.getTopNCapitalCitiesByRegion(
                                con,
                                readText("Enter region: "),
                                readN()
                        )
                );
                break;

            // US-21: Population breakdown per continent
            case 21:
                populations.printPopulationSummaries(
                        populations.getPopulationInEachContinent(con)
                );
                break;

            // US-22: Population breakdown per region
            case 22:
                populations.printPopulationSummaries(
                        populations.getPopulationByRegion(con)
                );
                break;

            // US-23: Population breakdown per country
            case 23:
                populations.printPopulationSummaries(
                        populations.getPopulationByCountry(con)
                );
                break;

            // US-24: World population breakdown
            case 24:
                populations.printPopulationSummaries(
                        populations.getWorldPopulation(con)
                );
                break;

            // US-25: Population of a continent
            case 25: {

                String selected = readText("Enter continent: ");
                Long total = null;

                for (PopulationSummary summary :
                        populations.getPopulationByContinent(con)) {

                    if (summary.getName().equalsIgnoreCase(selected)) {
                        total = summary.getTotalPopulation();
                        break;
                    }
                }

                System.out.println("\n  Continent: " + selected);
                printPopulation(total);
                break;
            }

            // US-26: Population of a region
            case 26: {

                String selected = readText("Enter region: ");

                System.out.println("\n  Region: " + selected);

                printPopulation(
                        populations.getRegionPopulation(con, selected)
                );
                break;
            }

            // US-27: Population of a country
            case 27: {

                String selected = readText("Enter country: ");

                System.out.println("\n  Country: " + selected);

                printPopulation(
                        populations.getCountryPopulation(con, selected)
                );
                break;
            }

            // US-28: Population of a district
            case 28: {

                String selected = readText("Enter district: ");

                System.out.println("\n  District: " + selected);

                printPopulation(
                        populations.getDistrictPopulation(con, selected)
                );
                break;
            }

            // US-29: Population of a city
            case 29: {

                String selected = readText("Enter city: ");

                System.out.println("\n  City: " + selected);

                printPopulation(
                        populations.getCityPopulation(con, selected)
                );
                break;
            }

            // US-30: Major language speakers
            case 30:
                for (String row :
                        languages.getMajorLanguageSpeakers(con)) {

                    System.out.println(row);
                }
                break;

            // US-31: Top N cities in a country
            case 31:
                cities.printCities(
                        cities.getTopNCitiesByCountry(
                                con,
                                readText("Enter country: "),
                                readN()
                        )
                );
                break;

            // US-32: Top N cities in a district
            case 32:
                cities.printCities(
                        cities.getTopNCitiesByDistrict(
                                con,
                                readText("Enter district: "),
                                readN()
                        )
                );
                break;

            default:
                printError("Unknown report number.");
        }
    }
}