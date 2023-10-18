package au.edu.cqu.se.hms;

import java.util.HashMap;
import java.util.Map;

/**
 * A singleton class representing a session with key-value data storage
 * capabilities.
 *
 * The session provides methods to set, retrieve, check, and clear data. Only
 * one instance of the session can be created during the runtime of the
 * application.
 *
 *
 */
public class Session {

    /**
     * The map to store session data.
     */
    private final Map<String, Object> data;

    /**
     * The single instance of the session.
     */
    private static Session session = null;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private Session() {
        data = new HashMap<>();
    }

    /**
     * Returns the single instance of the session.
     *
     * If the session instance hasn't been created yet, it initializes and then
     * returns it.
     *
     *
     * @return The single instance of the session.
     */
    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    /**
     * Sets a key-value pair in the session data.
     *
     * @param key The key for the data.
     * @param obj The value to be stored.
     */
    public void setData(String key, Object obj) {
        data.put(key, obj);
    }

    /**
     * Retrieves a value based on the given key from the session data.
     *
     * @param key The key to retrieve the data for.
     * @return The value associated with the given key, or null if the key is
     * not present.
     */
    public Object getData(String key) {
        return data.get(key);
    }

    /**
     * Checks if the session has data for the given key.
     *
     * @param key The key to check the presence for.
     * @return {@code true} if the session has data for the given key,
     * {@code false} otherwise.
     */
    public boolean hasDataFor(String key) {
        return data.containsKey(key);
    }

    /**
     * Clears all data stored in the session.
     */
    public void clear() {
        data.clear();
    }

}
