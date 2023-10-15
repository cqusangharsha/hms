package au.edu.cqu.se.hms.daos;

import au.edu.cqu.se.hms.models.Patient;
import au.edu.cqu.se.hms.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author sudeep_sharma
 */
public class PatientDao {

    private final DBConnection dbConnection;
    private static PatientDao instance;

    public PatientDao() {
        dbConnection = DBConnection.getInstance();
    }

    public static PatientDao getInstance() {
        if (instance == null) {
            instance = new PatientDao();
        }
        return instance;
    }

    public boolean addPatient(Patient patient) {
        Connection connection = dbConnection.getConnection();

        String patientQuery = "INSERT INTO patient (patientName, dateOfBirth, gender, contactNumber, email, address, doctor, visitReason)\n"
                + "VALUES(?,?,?,?,?,?,?,?)";

        try {

            PreparedStatement prepStatement = connection.prepareStatement(patientQuery);
            prepStatement.setString(1, patient.getPatientName());
            prepStatement.setDate(2, new java.sql.Date(patient.getDateOfBirth().getTime()));
            prepStatement.setString(3, patient.getGender());
            prepStatement.setString(4, patient.getContactNumber());
            prepStatement.setString(5, patient.getEmail());
            prepStatement.setString(6, patient.getAddress());
            prepStatement.setString(7, patient.getDoctor());
            prepStatement.setString(8, patient.getVisitReason());
            int rowsAffectedPatient = prepStatement.executeUpdate();

            return rowsAffectedPatient > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Patient> getPatientByDoctor(String doctorName) {
        List<Patient> patients = new ArrayList<>();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM patient WHERE doctor = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, doctorName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String patientName = resultSet.getString("patientName");
                Date dateOfBirth = resultSet.getDate("dateOfBirth");
                String gender = resultSet.getString("gender");
                String contactNumber = resultSet.getString("contactNumber");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String doctor = resultSet.getString("doctor");
                String visitReason = resultSet.getString("visitReason");

                Patient patient = new Patient();
                patient.setId(id);
                patient.setPatientName(patientName);
                patient.setDateOfBirth(dateOfBirth);
                patient.setGender(gender);
                patient.setContactNumber(contactNumber);
                patient.setEmail(email);
                patient.setAddress(address);
                patient.setDoctor(doctor);
                patient.setVisitReason(visitReason);

                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }
}