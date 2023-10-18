package au.edu.cqu.se.hms.enums;

/**
 * Represents distinct roles within the system. Each role is associated with a specific string value.
 * This enum provides methods to retrieve the string value of a role and to convert a string value back to its corresponding role.
 *
 * */
public enum Role {

    /**
     * Represents respective roles.
     */
    ADMIN("ADMIN"), DOCTOR("DOCTOR"), ASSISTANT("ASSISTANCE");

    private final String value;

    /**
     * Private constructor used to associate a string value with each role.
     *
     * @param value The string representation of the role.
     */
    private Role(String value) {
        this.value = value;
    }

     /**
     * Retrieves the string value associated with the role.
     *
     * @return The string representation of the role.
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Converts a string value to its corresponding role.
     *
     * @param value The string representation of the role.
     * @return The role corresponding to the provided string value.
     * @throws IllegalArgumentException If the provided string value does not match any role.
     */
    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}
