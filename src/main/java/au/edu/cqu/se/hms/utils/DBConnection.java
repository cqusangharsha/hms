package au.edu.cqu.se.hms.utils;

import java.sql.*;

/**
 *
 * @author sangharshachaulagain
 */
public class DBConnection {

    private String dbHost = "localhost";
    private int dbPort = 3306;
    private String dbUsername = "root";
    private String dbPassword = "root";
    private String dbName = "hms_db";

    private Connection con = null;

    private static DBConnection dbConnection = null;

    /**
     * Private constructor to create a single instance of the database
     * connection. This constructor initializes the connection to the MySQL
     * database. It is invoked only once when the class is loaded.
     */
    private DBConnection() {
        if (con == null) {
            try {
                String dbUrl = String.format("jdbc:mysql://%s:%d/%s", dbHost, dbPort, "");
                Class.forName("com.mysql.cj.jdbc.Driver");
                con = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

                // Check if the specified database exists; create it if it doesn't.
                if (!databaseExists(dbName)) {
                    createDatabase(dbName);
                }

                // Connect to the specified database.
                String useDbSql = "USE " + dbName;
                Statement stmt = con.createStatement();
                stmt.executeUpdate(useDbSql);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Retrieves the single instance of the database connection.
     *
     * @return The database connection instance.
     */
    public static DBConnection getInstance() {
        if (dbConnection == null) {
            dbConnection = new DBConnection();
        }
        return dbConnection;
    }

    /**
     * Gets the connection to the MySQL database.
     *
     * @return The database connection.
     */
    public Connection getConnection() {
        return con;
    }

    /**
     * Checks if the database with the given name exists.
     *
     * @param databaseName The name of the database to check.
     * @return True if the database exists, false otherwise.
     * @throws SQLException If a database access error occurs.
     */
    private boolean databaseExists(String databaseName) throws SQLException {
        ResultSet resultSet = con.getMetaData().getCatalogs();
        while (resultSet.next()) {
            String existingDatabaseName = resultSet.getString(1);
            if (existingDatabaseName.equals(databaseName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a MySQL database with the given name.
     *
     * @param databaseName The name of the database to create.
     * @throws SQLException If a database access error occurs.
     */
    private void createDatabase(String databaseName) throws SQLException {
        String createDbSql = "CREATE DATABASE " + databaseName;
        Statement stmt = con.createStatement();
        stmt.executeUpdate(createDbSql);
    }
}
