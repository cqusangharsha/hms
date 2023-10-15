package au.edu.cqu.se.hms.models;

/**
 *
 * @author sudeep_sharma
 */
public class Doctor extends User {
    private int Id;

    private int userId;

    private String specialization;

    public Doctor() {
    }

    public Doctor(int doctorId, String specialization) {
        this.Id = doctorId;
        this.specialization = specialization;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }
    
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

}
