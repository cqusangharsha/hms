package au.edu.cqu.se.hms.daos;

import au.edu.cqu.se.hms.enums.Role;
import au.edu.cqu.se.hms.models.Assistant;
import au.edu.cqu.se.hms.models.Doctor;
import au.edu.cqu.se.hms.models.User;
import au.edu.cqu.se.hms.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

}
