package au.edu.cqu.se.hms.controllers;

import au.edu.cqu.se.hms.App;
import au.edu.cqu.se.hms.models.User;
import au.edu.cqu.se.hms.services.AuthenticationService;
import au.edu.cqu.se.hms.utils.UIUtils;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * This class serves as the controller for the Sign-In UI of the application.*
 */
public class SignInController implements Initializable {

    @FXML
    private TextField emailFld;
    @FXML
    private PasswordField passwordFld;
    @FXML
    private Button exitBtn;
    @FXML
    private Button loginBtn;

    AuthenticationService authService = null;

   /**
     * This method is called by the FXMLLoader when the initialization is
     * complete. It initializes the services, DAO instances, and sets up the
     * initial view for the signin portal.
     *
     * @param url The location used to resolve relative paths for the root
     * object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the
     * root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthenticationService();
    }

    /**
     * Handles the click event for the exit button. Exits the application when
     * the exit button is clicked.
     */
    @FXML
    private void onExitBtnClick(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Handles the click event for the login button. 1. Validates the provided
     * email and password. 2. If valid, the user is authenticated using the
     * AuthenticationService. 3. Redirects the user to the appropriate portal
     * based on their role (Admin, Doctor, or Assistant). 4. Displays error
     * messages if any issue arises during the login process.
     *
     * @param event The action event representing the button click.
     */
    @FXML
    private void onLoginBtnClick(ActionEvent event) {
        String email = emailFld.getText();
        String password = passwordFld.getText();

        if (email.isEmpty() || password.isEmpty()) {
            UIUtils.alert("Error", "Please Fill out all the field.", Alert.AlertType.ERROR);
            return;
        }

        User user = authService.authenticate(email, password);
        if (user == null) {
            UIUtils.alert("Login Failed.", "Invalid Credentials.", Alert.AlertType.ERROR);
            return;
        }

        String redirectPortal = null;

        switch (user.getRole()) {
            case ADMIN ->
                redirectPortal = "admin_portal";
            case DOCTOR ->
                redirectPortal = "doctor_portal";
            case ASSISTANT ->
                redirectPortal = "assistant_portal";
            default -> {
                UIUtils.alert("Contact Administrator", "Your Role is not set. Please contact administrator.", Alert.AlertType.ERROR);
                return;
            }
        }

        try {
            App.setRoot(redirectPortal);
        } catch (IOException e) {
            e.printStackTrace();
            UIUtils.alert("Application Error", "Error: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
