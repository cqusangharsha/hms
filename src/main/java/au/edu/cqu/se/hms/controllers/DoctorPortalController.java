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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 *
 * @author Dell
 */
public class DoctorPortalController implements Initializable {

    private final String selectedMenuStyle = "-fx-text-fill: #eee; -fx-font-size: 15px; -fx-background-color: #546e7a; -fx-border-color: transparent; -fx-underline: false;";
    private final String unSelectedMenuStyle = "-fx-text-fill: #546e7a; -fx-font-size: 15px; -fx-border-color: transparent; -fx-underline: false;";

    private AuthenticationService authService;
    @FXML
    private Pane sidebarPane;
    @FXML
    private Label nameLbl;
    @FXML
    private Hyperlink appointmentMenu;
    @FXML
    private Hyperlink patientMenu;
    @FXML
    private Pane appointmentContainer;
    @FXML
    private Pane patientListContainer;
    @FXML
    private Pane patientInfoContainer;
    @FXML
    private Text patientNameLbl;
    @FXML
    private Pane medicalHistoryContainer;
    @FXML
    private Label medicalHistoryLbl;

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
        nameLbl.setText("Dr. " + user.getLastName());
        nameLbl.layoutXProperty().bind(sidebarPane.widthProperty().subtract(nameLbl.widthProperty()).divide(2));
        showAppointmentContainer();

    }

    @FXML
    private void handleAppointmentMenu(ActionEvent event) {
        showAppointmentContainer();
    }

    private void showAppointmentContainer() {
        hideAllContainer();
        appointmentMenu.setStyle(selectedMenuStyle);
        appointmentContainer.setVisible(true);
    }

    @FXML
    private void handlePatientMenu(ActionEvent event) {
        hideAllContainer();
        showPatientListContainer();
    }

    private void showPatientListContainer() {
        hideAllContainer();
        patientMenu.setStyle(selectedMenuStyle);
        patientListContainer.setVisible(true);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UIUtils.logout();
    }

    private void showPatientInfoContainer() {
        hideAllContainer();
        patientInfoContainer.setVisible(true);
    }

    private void showMedicalHistoryContainer() {
        hideAllContainer();
        medicalHistoryContainer.setVisible(true);
    }

    private void hideAllContainer() {
        patientMenu.setStyle(unSelectedMenuStyle);
        appointmentMenu.setStyle(unSelectedMenuStyle);

        patientListContainer.setVisible(false);
        appointmentContainer.setVisible(false);
        patientInfoContainer.setVisible(false);
        medicalHistoryContainer.setVisible(false);
    }
}
