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
 *
 * @author sangharshachaulagain
 */
public class AssistantDao {

    private final DBConnection dbConnection;
    private static AssistantDao instance;

    public AssistantDao() {
        dbConnection = DBConnection.getInstance();
    }

    public static AssistantDao getInstance() {
        if (instance == null) {
            instance = new AssistantDao();
        }
        return instance;
    }

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
