package au.edu.cqu.se.hms.daos;

import au.edu.cqu.se.hms.enums.Role;
import au.edu.cqu.se.hms.models.Patient;
import au.edu.cqu.se.hms.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
 * This class provides the Data Access Object (DAO) functionalities specific to the 
 * Patient model. It facilitates database operations like adding patients, fetching patient
 * details, managing patient appointments, generating patient bills, and much more.
 * 
 */
public class PatientDao {

    private final DBConnection dbConnection;
    private static PatientDao instance;

    /**
     * Constructor initializing the DBConnection instance.
     */
    public PatientDao() {
        dbConnection = DBConnection.getInstance();
    }

    /**
     * Returns a singleton instance of PatientDao. If an instance doesn't exist,
     * it creates a new one.
     *
     * @return An instance of PatientDao.
     */
    public static PatientDao getInstance() {
        if (instance == null) {
            instance = new PatientDao();
        }
        return instance;
    }

    /**
     * Inserts a new patient into the database.
     *
     * @param patient The Patient object to be inserted.
     * @return true if the patient was added successfully, false otherwise.
     */
    public boolean addPatient(Patient patient) {
        Connection connection = dbConnection.getConnection();

        String patientQuery = "INSERT INTO patient (patientName, dateOfBirth, gender, contactNumber, email, address, visitReason)\n"
                + "VALUES(?,?,?,?,?,?,?)";

        try {

            PreparedStatement prepStatement = connection.prepareStatement(patientQuery);
            prepStatement.setString(1, patient.getPatientName());
            prepStatement.setDate(2, new java.sql.Date(patient.getDateOfBirth().getTime()));
            prepStatement.setString(3, patient.getGender());
            prepStatement.setString(4, patient.getContactNumber());
            prepStatement.setString(5, patient.getEmail());
            prepStatement.setString(6, patient.getAddress());
            prepStatement.setString(7, patient.getVisitReason());
            int rowsAffectedPatient = prepStatement.executeUpdate();

            return rowsAffectedPatient > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Inserts a new appointment for a patient into the database.
     *
     * @param patient The Patient object holding the appointment details.
     * @return true if the appointment was added successfully, false otherwise.
     */
    public boolean addAppointment(Patient patient) {
        Connection connection = dbConnection.getConnection();

        String appointmentQuery = "INSERT INTO appointment (patientName, selectedDate, selectedTime, doctor,  visitReason)\n"
                + "VALUES(?,?,?,?,?)";

        try {

            PreparedStatement prepStatement = connection.prepareStatement(appointmentQuery);
            prepStatement.setString(1, patient.getPatientName());
            prepStatement.setDate(2, new java.sql.Date(patient.getSelectedDate().getTime()));
            prepStatement.setString(3, patient.getSelectedTime());
            prepStatement.setString(4, patient.getDoctor());
            prepStatement.setString(5, patient.getVisitReason());
            int rowsAffectedAppointment = prepStatement.executeUpdate();

            return rowsAffectedAppointment > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches a list of patients associated with a specific doctor.
     *
     * @param doctorName The name of the doctor.
     * @return A list of Patient objects.
     */
    public List<Patient> getPatientByDoctor(String doctorName) {
        List<Patient> patients = new ArrayList<>();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM patient WHERE patientName IN (Select patientName from appointment where doctor = ? )";

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

    /**
     * Generates and retrieves a billing statement for a specific patient.
     *
     * @param patientName The name of the patient.
     * @return A list of Patient objects, each holding a specific billing
     * detail.
     */
    public List<Patient> getPatientBill(String patientName) {
        List<Patient> patients = new ArrayList<>();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT s.name,s.checkupCost,a.doctor,a.visitReason\n"
                + "FROM specializations s\n"
                + "JOIN doctor d ON s.name = d.specialization\n"
                + "JOIN users u ON d.id = u.id AND u.role = ? \n"
                + "JOIN appointment a ON u.firstName = a.doctor\n"
                + "WHERE a.patientName = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, Role.DOCTOR.getValue());
            statement.setString(2, patientName);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                System.out.println("Inside while");
                String ptName = resultSet.getString("name");
                String cost = resultSet.getString("checkupCost");
                String doctor = resultSet.getString("doctor");
                String visitReason = resultSet.getString("visitReason");

                Patient patient = new Patient();

                patient.setPatientName(ptName);

                patient.setTotalCost(cost);
                patient.setDoctor(doctor);
                patient.setVisitReason(visitReason);
                System.out.println("Cost" + cost);
                patients.add(patient);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

    /**
     * Fetches all the patients from the database.
     *
     * @return A list of all Patient objects.
     */
    public List<Patient> getAllPatient() {
        List<Patient> patients = new ArrayList<>();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT * FROM patient";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
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

    /**
     * Fetches all appointments from the database.
     *
     * @return A list of Patient objects, each holding appointment details.
     */
    public List<Patient> getAppointments() {
        List<Patient> patients = new ArrayList<>();
        Connection connection = dbConnection.getConnection();
        String query = "SELECT patientName,doctor FROM appointment";

        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String patientName = resultSet.getString("patientName");

                String doctor = resultSet.getString("doctor");

                Patient patient = new Patient();

                patient.setPatientName(patientName);

                patient.setDoctor(doctor);

                patients.add(patient);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return patients;
    }

}
