package au.edu.cqu.se.hms.models;

/**
 *
 * @author sudeep_sharma
 */
public class Doctor extends User {

    private int doctorId;

    private String specialization;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

}
