package au.edu.cqu.se.hms.models;

import au.edu.cqu.se.hms.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The `Migration` class represents a database schema migration record, which
 * stores information about database schema changes, such as the migration
 * script and its unique identifier. It provides methods to execute a migration
 * and maintain a history of applied migrations.
 *
 * This class encapsulates the functionality to interact with the "migration"
 * table in the database.
 *
 */
public class Migration {

    private int id;
    private String script;

    private Connection conn = null;

    public Migration(int id, String script) {

        conn = DBConnection.getInstance().getConnection();

        this.id = id;
        this.script = script;

    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the script
     */
    public String getScript() {
        return script;
    }

    /**
     * @param script the script to set
     */
    public void setScript(String script) {
        this.script = script;
    }

    /**
     * Creates the database schema record table if it does not already exist.
     * The table is used to store migration history records, including migration
     * IDs and corresponding migration scripts.
     */
    public static void createTable() {
        String createMigrationTableSQL = "CREATE TABLE IF NOT EXISTS migrations ("
                + "id INT PRIMARY KEY,"
                + "script text NOT NULL"
                + ")";
        try {
            DBConnection.getInstance()
                    .getConnection()
                    .createStatement()
                    .executeUpdate(createMigrationTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a database schema migration script if it has not already been
     * applied and records it in the history table.
     *
     * script to execute.
     */
    public void executeMigration() {
        try {
            // Check if the migration has already been applied
            if (!isQueryApplied(this.getId())) {
                try (Statement statement = conn.createStatement()) {
                    statement.executeUpdate(this.getScript());
                }

                // Record the schemaRecord in the history table
                recordQueryHistory();
            } else {
                System.out.println("Migration " + this.getId() + " has already been applied.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a migration query with a specified version has been applied by
     * querying the migrations table.
     *
     * @param version The version of the migration query to check.
     * @return True if the migration query with the given version has been
     * applied; otherwise, false.
     * @throws SQLException If there is an issue with executing the SQL query or
     * accessing the database.
     */
    private boolean isQueryApplied(int version) throws SQLException {
        String sql = "SELECT id FROM migrations WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, version);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    /**
     * Records a migration query and its version in the database schema record
     * table for future reference.
     *
     * @param version The version of the migration query being recorded.
     * @param script The SQL script of the migration query.
     * @throws SQLException If there is an issue with executing the SQL query or
     * accessing the database.
     */
    private void recordQueryHistory() throws SQLException {
        String sql = "INSERT INTO migrations (id, script) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, this.getId());
            preparedStatement.setString(2, this.getScript());
            preparedStatement.executeUpdate();
        }
    }

}
