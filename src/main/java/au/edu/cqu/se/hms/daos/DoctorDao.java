package au.edu.cqu.se.hms.daos;

import au.edu.cqu.se.hms.enums.Role;
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
public class DoctorDao {

    private final DBConnection dbConnection;
    private static DoctorDao instance;

    public DoctorDao() {
        dbConnection = DBConnection.getInstance();
    }

    public static DoctorDao getInstance() {
        if (instance == null) {
            instance = new DoctorDao();
        }
        return instance;
    }

    public boolean addDoctor(Doctor doctor) {
        Connection connection = dbConnection.getConnection();

        String doctorQuery = "INSERT INTO doctor (user_id, specialization) VALUES (?, ?)";

        try {

            PreparedStatement doctorStatement = connection.prepareStatement(doctorQuery);
            doctorStatement.setInt(1, doctor.getDoctorId());
            doctorStatement.setString(2, doctor.getSpecialization());
            int rowsAffectedDoctor = doctorStatement.executeUpdate();

            return rowsAffectedDoctor > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
