package au.edu.cqu.se.hms.enums;

/**
 *
 * @author sangharshachaulagain
 */
public enum Role {
    ADMIN("ADMIN"), DOCTOR("ADMIN"), ASSISTANT("ASSISTANCE");

    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid role value: " + value);
    }
}
