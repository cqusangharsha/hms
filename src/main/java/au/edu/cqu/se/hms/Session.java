package au.edu.cqu.se.hms;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author sangharshachaulagain
 */
public class Session {

    private final Map<String, Object> data;
    private static Session session = null;

    private Session() {
        data = new HashMap<>();
    }

    public static Session getInstance() {
        if (session == null) {
            session = new Session();
        }
        return session;
    }

    public void setData(String key, Object obj) {
        data.put(key, obj);
    }

    public Object getData(String key) {
        return data.get(key);
    }

    public boolean hasDataFor(String key) {
        return data.containsKey(key);
    }

    public void clear() {
        data.clear();
    }

}
