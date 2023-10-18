package au.edu.cqu.se.hms.daos;

import au.edu.cqu.se.hms.models.Assistant;
import au.edu.cqu.se.hms.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides Data Access Object (DAO) functionalities for the Assistant model.
 * This class is responsible for querying the database for operations related to
 * assistants.
 *
 */
public class AssistantDao {

    private final DBConnection dbConnection;
    private static AssistantDao instance;

    /**
     * Initializes the AssistantDao with a DBConnection instance.
     */
    public AssistantDao() {
        dbConnection = DBConnection.getInstance();
    }

    /**
     * Returns a singleton instance of AssistantDao. If an instance doesn't
     * exist, a new one is created.
     *
     * @return An instance of AssistantDao.
     */
    public static AssistantDao getInstance() {
        if (instance == null) {
            instance = new AssistantDao();
        }
        return instance;
    }

    /**
     * Adds a new Assistant to the database.
     *
     * @param assistant The Assistant object to be added.
     * @return true if the assistant was added successfully, false otherwise.
     */
    public boolean addAssistant(Assistant assistant) {
        Connection connection = dbConnection.getConnection();

        String doctorQuery = "INSERT INTO assistant (user_id, reports) VALUES (?, ?)";

        try {

            PreparedStatement doctorStatement = connection.prepareStatement(doctorQuery);
            doctorStatement.setInt(1, assistant.getAssistantId());
            doctorStatement.setString(2, assistant.getReportsTo());
            int rowsAffectedAssistant = doctorStatement.executeUpdate();

            return rowsAffectedAssistant > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches all the assistants from the database.
     *
     * @return A list of Assistant objects.
     */
    public List<Assistant> findAll() {
        Connection connection = dbConnection.getConnection();
        List<Assistant> assistants = new ArrayList<>();

        String query = "SELECT * FROM assistant";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int assistantId = resultSet.getInt("id");
                String reportsTo = resultSet.getString("reports");

                Assistant assistant = new Assistant();
                assistant.setId(assistantId);
                assistant.setAssistantId(assistantId);
                assistant.setReportsTo(reportsTo);
                assistant.setUserId(resultSet.getInt("user_id"));
                assistants.add(assistant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assistants;
    }

}
