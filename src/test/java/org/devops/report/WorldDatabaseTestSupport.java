package org.devops.report;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

final class WorldDatabaseTestSupport {

    private WorldDatabaseTestSupport() {
        // Prevent creating instances of this helper class.
    }

    static Connection openConnection() throws SQLException {
        String url = System.getenv().getOrDefault(
                "TEST_DB_URL",
                "jdbc:mysql://localhost:33060/world?allowPublicKeyRetrieval=true&useSSL=false"
        );

        String user = System.getenv().getOrDefault("TEST_DB_USER", "root");
        String password = System.getenv().getOrDefault("TEST_DB_PASSWORD", "example");

        return DriverManager.getConnection(url, user, password);
    }
}
