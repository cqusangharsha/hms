package au.edu.cqu.se.hms.models;

import au.edu.cqu.se.hms.daos.UserDao;

/**
 *
 * @author sudeep_sharma
 */
public class Doctor extends User {
    private int Id;

    private int userId;
    
    private User user;

    private String specialization;
    
    private UserDao userDao = UserDao.getInstance();

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
        this.user = userDao.findUserByID(userId);
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public User getUser() {
        return this.user == null ? new User() : this.user;
    }
}
