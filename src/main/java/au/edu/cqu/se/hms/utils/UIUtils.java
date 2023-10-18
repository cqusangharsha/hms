package au.edu.cqu.se.hms.utils;

import au.edu.cqu.se.hms.App;
import au.edu.cqu.se.hms.Session;
import java.io.IOException;
import javafx.scene.control.Alert;

/**
 *
 * This class provides utility methods for common user interface
 * operations in the application. This class centralizes UI-related functionalities
 * like alerting users and managing application sessions.
 * 
 */
public class UIUtils {

    
    /**
     * Displays an alert dialog box with the given parameters.
     *
     * @param title the title of the alert box
     * @param msg the message content of the alert
     * @param type the type of the alert (e.g., INFO, ERROR)
     */
    public static void alert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    
     /**
     * Logs out the current user session and navigates to the sign-in page.
     * If any error occurs during the transition, an alert box is displayed.
     */
    public static void logout() {
        Session.getInstance().clear();
        try {
            App.setRoot("signin");
        } catch (IOException ex) {
            alert("Application Error", "Error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
