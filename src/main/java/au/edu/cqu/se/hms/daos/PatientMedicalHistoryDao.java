package au.edu.cqu.se.hms.daos;

import au.edu.cqu.se.hms.models.PatientMedicalHistory;
import au.edu.cqu.se.hms.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class provides the Data Access Object (DAO) functionalities for managing
 * the medical history of patients. It enables database operations like adding
 * new medical histories and fetching historical medical records of a specific
 * patient.
 *
 */
public class PatientMedicalHistoryDao {

    private final DBConnection dbConnection;
    private static PatientMedicalHistoryDao instance;

    /**
     * Constructor initializing the DBConnection instance.
     */
    public PatientMedicalHistoryDao() {
        dbConnection = DBConnection.getInstance();
    }

    /**
     * Returns a singleton instance of PatientMedicalHistoryDao. If an instance
     * doesn't exist, it creates a new one.
     *
     * @return An instance of PatientMedicalHistoryDao.
     */
    public static PatientMedicalHistoryDao getInstance() {
        if (instance == null) {
            instance = new PatientMedicalHistoryDao();
        }
        return instance;
    }

    /**
     * Inserts a new medical history record into the database.
     *
     * @param history The PatientMedicalHistory object to be inserted.
     * @return true if the record was added successfully, false otherwise.
     */
    public boolean addPatientMedicalHistory(PatientMedicalHistory history) {
        Connection connection = dbConnection.getConnection();

        String historyQuery = "INSERT INTO patient_medical_histories (date, patient_id, doctor_id, diagnosis) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(historyQuery);
            preparedStatement.setDate(1, new java.sql.Date(history.getDate().getTime()));
            preparedStatement.setInt(2, history.getPatientId());
            preparedStatement.setInt(3, history.getDoctorId());
            preparedStatement.setString(4, history.getDiagnosis());

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches all the medical history records associated with a specific
     * patient from the database.
     *
     * @param patientId The ID of the patient whose records are to be fetched.
     * @return A list of PatientMedicalHistory objects.
     */
    public List<PatientMedicalHistory> getMedicalHistoryByPatient(int patientId) {
        List<PatientMedicalHistory> historyList = new ArrayList<>();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM patient_medical_histories WHERE patient_id = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, patientId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                Date date = resultSet.getDate("date");
                int doctorId = resultSet.getInt("doctor_id");
                String diagnosis = resultSet.getString("diagnosis");

                PatientMedicalHistory history = new PatientMedicalHistory(id, date, patientId, doctorId, diagnosis);
                historyList.add(history);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historyList;
    }
}
