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
 * Provides Data Access Object (DAO) functionalities for the Doctor model. This
 * class handles database operations related to doctors such as fetching,
 * adding, and finding specific doctors by their user ID.
 *
 */
public class DoctorDao {

    private final DBConnection dbConnection;
    private static DoctorDao instance;

    /**
     * Initializes the DoctorDao with a DBConnection instance.
     */
    public DoctorDao() {
        dbConnection = DBConnection.getInstance();
    }

    /**
     * Returns a singleton instance of DoctorDao. If an instance doesn't exist,
     * a new one is created.
     *
     * @return An instance of DoctorDao.
     */
    public static DoctorDao getInstance() {
        if (instance == null) {
            instance = new DoctorDao();
        }
        return instance;
    }

    /**
     * Fetches all the doctors from the database.
     *
     * @return A list of Doctor objects.
     */
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

    /**
     * Adds a new Doctor to the database.
     *
     * @param doctor The Doctor object to be added.
     * @return true if the doctor was added successfully, false otherwise.
     */
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

    /**
     * Fetches a Doctor object from the database using the provided user ID.
     *
     * @param userId The user ID to search for.
     * @return A Doctor object if found, null otherwise.
     */
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
