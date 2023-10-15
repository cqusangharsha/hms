package au.edu.cqu.se.hms.models;

import java.util.Date;

/**
 *
 * @author sangharshachaulagain
 */
public class PatientMedicalHistory {

    private int id;
    private Date date;
    private int patientId;
    private int doctorId;
    private String diagnosis;

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
