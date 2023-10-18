package au.edu.cqu.se.hms.models;

/**
 *
 * Model class for specialization
 */
public class Specialization {

    //variables for specialization
    private int id;
    private String name;
    private String costCheckup;

    //constructors for specialization
    public Specialization() {
    }

    public Specialization(String name, String costCheckup) {
        this.name = name;
        this.costCheckup = costCheckup;
    }

    public Specialization(int id, String name, String costCheckup) {
        this.id = id;
        this.name = name;
        this.costCheckup = costCheckup;
    }

    //all getter and setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCostCheckup() {
        return costCheckup;
    }

    public void setCostCheckup(String costCheckup) {
        this.costCheckup = costCheckup;
    }

    @Override
    public String toString() {
        return "Specialization{" + "id=" + id + ", name=" + name + '}';
    }

}
