package au.edu.cqu.se.hms.daos;

import au.edu.cqu.se.hms.models.Doctor;
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

    public List<Doctor> findAll() {
        List<Doctor> doctorList = new ArrayList<>();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM doctor;";

        try {
            PreparedStatement statement = connection.prepareStatement(query);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int doctorId = resultSet.getInt("id");
                String specialization = resultSet.getString("specialization");
                Doctor doctor = new Doctor(doctorId, specialization);
                doctor.setUserId(resultSet.getInt("user_id"));
                doctorList.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctorList;

    }

    public boolean addDoctor(Doctor doctor) {
        Connection connection = dbConnection.getConnection();

        String doctorQuery = "INSERT INTO doctor (user_id, specialization) VALUES (?, ?)";

        try {

            PreparedStatement doctorStatement = connection.prepareStatement(doctorQuery);
            doctorStatement.setInt(1, doctor.getId());
            doctorStatement.setString(2, doctor.getSpecialization());
            int rowsAffectedDoctor = doctorStatement.executeUpdate();

            return rowsAffectedDoctor > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Doctor findDoctorByUserId(int userId) {
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM doctor WHERE user_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int doctorId = resultSet.getInt("id");
                String specialization = resultSet.getString("specialization");
                return new Doctor(doctorId, specialization);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
