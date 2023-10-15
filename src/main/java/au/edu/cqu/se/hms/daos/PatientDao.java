/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package au.edu.cqu.se.hms.daos;


import au.edu.cqu.se.hms.models.Patient;
import au.edu.cqu.se.hms.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

        String patientQuery = "INSERT INTO patient (patientName, dateOfBirth, gender, contactNumber, email, address, doctor, visitReason)\n" +
"VALUES(?,?,?,?,?,?,?,?)";

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

}
