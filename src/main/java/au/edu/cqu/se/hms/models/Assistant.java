package au.edu.cqu.se.hms.models;

import au.edu.cqu.se.hms.daos.UserDao;

/**
 *
 * @author sudeep_sharma
 */
public class Assistant extends User {

    private int id;
    private int assistantId;
    private String reportsTo;
    private int userId;
    private User user;
    
    private UserDao userDao = UserDao.getInstance();

    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getAssistantId() {
        return assistantId;
    }

    public void setAssistantId(int assistantId) {
        this.assistantId = assistantId;
    }

    public String getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(String reportsTo) {
        this.reportsTo = reportsTo;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public void setUserId(int userId) {
        this.user = userDao.findUserByID(userId);
        this.userId = userId;
    }
    
    public User getUser() {
        return this.user == null ? new User() : this.user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
