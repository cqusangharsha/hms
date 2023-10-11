package au.edu.cqu.se.hms.utils;

import au.edu.cqu.se.hms.App;
import au.edu.cqu.se.hms.Session;
import java.io.IOException;
import javafx.scene.control.Alert;

/**
 *
 * @author sangharshachaulagain
 */
public class UIUtils {

    public static void alert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void logout() {
        Session.getInstance().clear();
        try {
            App.setRoot("signin");
        } catch (IOException ex) {
            alert("Application Error", "Error: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

}
