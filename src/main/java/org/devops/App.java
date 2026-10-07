package org.devops;

import org.devops.model.CapitalCity;
import org.devops.report.CapitalReport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class App
{
    /* Connection to MySQL database.
        */
    private Connection con = null;

    public static void main(String[] args)
    {
        // Create new application
        App app = new App();

        // Connect to database
        app.connect();
        CapitalReport report = new CapitalReport();

        ArrayList<CapitalCity> capitals =
                report.getAllCapitalCities(app.getConnection());

        report.printCapitalCities(capitals);

        // Disconnect from database
        app.disconnect();
    }

    /**
     * Returns the active MySQL database connection.
     *
     * @return active database connection
     */
    public Connection getConnection()
    {
        return con;
    }

    /* Connect to the MySQL world database.
        */
    public void connect()
    {
        try
        {
            // Load Database driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // Number of times the application will try to connect
        int retries = 10;

        for (int i = 0; i < retries; ++i)
        {
            System.out.println("Connecting to database...");

            try
            {
                // Wait for MySQL database to start
                Thread.sleep(30000);

                // Connect to the world database
                con = DriverManager.getConnection(
                        "jdbc:mysql://db:3306/world?allowPublicKeyRetrieval=true&useSSL=false",
                        "root",
                        "example"
                );

                System.out.println("Successfully connected");

                // Exit loop after successful connection
                break;
            }
            catch (SQLException sqle)
            {
                System.out.println(
                        "Failed to connect to database attempt "
                                + Integer.toString(i)
                );

                System.out.println(sqle.getMessage());
            }
            catch (InterruptedException ie)
            {
                System.out.println(
                        "Thread interrupted? Should not happen."
                );
            }
        }
    }

    /**
     * Disconnect from the MySQL database.
     */
    public void disconnect()
    {
        if (con != null)
        {
            try
            {
                // Close connection
                con.close();

                System.out.println(
                        "Successfully disconnected from database"
                );
            }
            catch (Exception e)
            {
                System.out.println(
                        "Error closing connection to database"
                );
            }
        }
    }
}