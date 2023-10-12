package au.edu.cqu.se.hms.models;

/**
 *
 * @author sangharshachaulagain
 */
public class Specialization {

    private int id;
    private String name;

    public Specialization() {
    }

    public Specialization(String name) {
        this.name = name;
    }

    public Specialization(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Specialization{" + "id=" + id + ", name=" + name + '}';
    }

}
