package au.edu.cqu.se.hms.models;

import java.util.Date;

/**
 *
 * Model for medical history
 */
public class PatientMedicalHistory {

    //variable for patient medical history
    private int id;
    private Date date;
    private int patientId;
    private int doctorId;
    private String diagnosis;

    //constructors for patient medical history
    public PatientMedicalHistory() {
    }

    public PatientMedicalHistory(int id, Date date, int patientId, int doctorId, String diagnosis) {
        this.id = id;
        this.date = date;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
    }

    public PatientMedicalHistory(Date date, int patientId, int doctorId, String diagnosis) {
        this.date = date;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
    }

    
    //all getter and setter methods for patient medical history
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }
}
