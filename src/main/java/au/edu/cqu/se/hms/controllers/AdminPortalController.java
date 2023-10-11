package au.edu.cqu.se.hms.controllers;

import au.edu.cqu.se.hms.App;
import au.edu.cqu.se.hms.models.User;
import au.edu.cqu.se.hms.services.AuthenticationService;
import au.edu.cqu.se.hms.utils.UIUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

/**
 *
 * @author sangharshachaulagain
 */
public class AdminPortalController implements Initializable {

    private MenuButton profileButton;

    private AuthenticationService authService;
    @FXML
    private Label nameLbl;
    @FXML
    private Pane sidebarPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthenticationService();

        User user = authService.getCurrentUser();
        if (user == null) {
            try {
                App.setRoot("signin");
            } catch (IOException ex) {
                UIUtils.alert("Application Error", "Error: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
            return;
        }
        nameLbl.setText("Male".equals(user.getGender()) ? "Mr. " + user.getLastName() : "Mrs. " + user.getLastName());
        nameLbl.layoutXProperty().bind(sidebarPane.widthProperty().subtract(nameLbl.widthProperty()).divide(2));
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UIUtils.logout();
    }
}
