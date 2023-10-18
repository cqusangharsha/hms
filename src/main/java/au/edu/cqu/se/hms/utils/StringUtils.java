package au.edu.cqu.se.hms.utils;

/**
**
 * This class provides utility methods for performing common
 * string operations. 
 * This class is designed to centralize and simplify string
 * handling operations throughout the application.
 * 
 */
public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
