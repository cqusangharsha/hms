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
import javafx.scene.control.Hyperlink;
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

    private final String selectedMenuStyle = "-fx-text-fill: #eee; -fx-font-size: 15px; -fx-background-color: #546e7a; -fx-border-color: transparent; -fx-underline: false;";
    private final String unSelectedMenuStyle = "-fx-text-fill: #546e7a; -fx-font-size: 15px; -fx-border-color: transparent; -fx-underline: false;";

    private AuthenticationService authService;
    @FXML
    private Label nameLbl;
    @FXML
    private Pane sidebarPane;
    @FXML
    private Hyperlink doctorMenu;
    @FXML
    private Hyperlink assistantMenu;
    @FXML
    private Hyperlink adminMenu;
    @FXML
    private Pane doctorContainer;
    @FXML
    private Pane assistantContainer;
    @FXML
    private Pane adminContainer;
    @FXML
    private Pane doctorListContainer;
    @FXML
    private Pane saveDoctorCotaniner;

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
        showDoctorContainer();
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UIUtils.logout();
    }

    @FXML
    private void handleDoctorMenu(ActionEvent event) {
        showDoctorContainer();
    }
    
    @FXML
    private void handleAddDoctor(ActionEvent event) {
        showAddDoctorContainer();
    }
    
    @FXML
    private void handleDoctorBackBtn(ActionEvent event) {
        showDoctorListContainer();
    }

    private void showDoctorContainer() {
        doctorMenu.setStyle(selectedMenuStyle);
        assistantMenu.setStyle(unSelectedMenuStyle);
        adminMenu.setStyle(unSelectedMenuStyle);

        doctorContainer.setVisible(true);
        assistantContainer.setVisible(false);
        adminContainer.setVisible(false);
        
        showDoctorListContainer();
    }
    
    private void showDoctorListContainer() {
        saveDoctorCotaniner.setVisible(false);
        doctorListContainer.setVisible(true);
    }
    
    private void showAddDoctorContainer() {
        saveDoctorCotaniner.setVisible(true);
        doctorListContainer.setVisible(false);
    }

    @FXML
    private void handleAssistantMenu(ActionEvent event) {
        showAssistantContainer();
    }

    private void showAssistantContainer() {
        doctorMenu.setStyle(unSelectedMenuStyle);
        assistantMenu.setStyle(selectedMenuStyle);
        adminMenu.setStyle(unSelectedMenuStyle);

        doctorContainer.setVisible(false);
        assistantContainer.setVisible(true);
        adminContainer.setVisible(false);
    }

    @FXML
    private void handleAdminMenu(ActionEvent event) {
        showAdminContainer();
    }

    private void showAdminContainer() {
        doctorMenu.setStyle(unSelectedMenuStyle);
        assistantMenu.setStyle(unSelectedMenuStyle);
        adminMenu.setStyle(selectedMenuStyle);

        doctorContainer.setVisible(false);
        assistantContainer.setVisible(false);
        adminContainer.setVisible(true);
    }

    @FXML
    private void handleAddAssistant(ActionEvent event) {
    }

    @FXML
    private void handleAddAdmin(ActionEvent event) {
    }
}
