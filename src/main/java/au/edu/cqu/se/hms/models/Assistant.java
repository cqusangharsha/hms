package au.edu.cqu.se.hms.models;

/**
 *
 * @author sudeep_sharma
 */
public class Assistant extends User {

    private int assistantId;
    private String reportsTo;

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
}
