/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package au.edu.cqu.se.hms.controllers;

import au.edu.cqu.se.hms.App;
import au.edu.cqu.se.hms.daos.AssistantDao;
import au.edu.cqu.se.hms.daos.DoctorDao;
import au.edu.cqu.se.hms.daos.PatientDao;
import au.edu.cqu.se.hms.daos.SpecializationDao;
import au.edu.cqu.se.hms.daos.UserDao;
import au.edu.cqu.se.hms.enums.Role;
import au.edu.cqu.se.hms.models.Assistant;
import au.edu.cqu.se.hms.models.Doctor;
import au.edu.cqu.se.hms.models.Patient;
import au.edu.cqu.se.hms.models.Specialization;
import au.edu.cqu.se.hms.models.User;
import au.edu.cqu.se.hms.services.AuthenticationService;
import au.edu.cqu.se.hms.utils.StringUtils;
import au.edu.cqu.se.hms.utils.UIUtils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 *
 * @author sudeep_sharma
 */
public class AssistantPortalController implements Initializable {

    private final String selectedMenuStyle = "-fx-text-fill: #eee; -fx-font-size: 15px; -fx-background-color: #546e7a; -fx-border-color: transparent; -fx-underline: false;";
    private final String unSelectedMenuStyle = "-fx-text-fill: #546e7a; -fx-font-size: 15px; -fx-border-color: transparent; -fx-underline: false;";

    private AuthenticationService authService;

    private DoctorDao doctorDao;
    private UserDao userDao;
    private PatientDao patientDao;
    private AssistantDao assistantDao;

    @FXML
    private Label nameLbl;
    @FXML
    private Pane sidebarPane;
    @FXML
    private Hyperlink patientMenu;
    @FXML
    private Hyperlink assistantMenu;
    @FXML
    private Hyperlink adminMenu;
    @FXML
    private Pane patientContainer;
    @FXML
    private Pane assistantContainer;
    @FXML
    private Pane adminContainer;
    @FXML
    private Pane patientListContainer;
    @FXML
    private Pane savePatientCotaniner;
    @FXML
    private Pane assistantListContainer;
    @FXML
    private Pane addAssistantContainer;
    @FXML
    private Pane addAppointmentContainer;
    @FXML
    private Pane appointmentListContainer;

    @FXML
    private TextField fName;

    @FXML
    private TextField assitFName;

    @FXML
    private TextField lName;

    @FXML
    private TextField assitLName;

    @FXML
    private TextField email;

    @FXML
    private TextField assistEmail;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField assistPass;

    @FXML
    private TextField address;

    @FXML
    private TextField assistAddress;

    @FXML
    private TextField contactNumber;

    @FXML
    private TextField assistContact;

    @FXML
    private RadioButton male;

    @FXML
    private RadioButton assistMale;
    @FXML
    private RadioButton female;

    @FXML
    private RadioButton assistFemale;

    @FXML
    private RadioButton other;

    @FXML
    private RadioButton assistOther;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private DatePicker assistDateOfBirth;

    @FXML
    private MenuButton availableDoctors;

    @FXML
    private TextArea reasonVisit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthenticationService();

        doctorDao = DoctorDao.getInstance();
        assistantDao = AssistantDao.getInstance();

        userDao = UserDao.getInstance();
        patientDao = PatientDao.getInstance();

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

        showPatientContainer();

    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UIUtils.logout();
    }

    @FXML
    private void handlePatientMenu(ActionEvent event) {
        showPatientContainer();
    }

    @FXML
    private void handleAddPatient(ActionEvent event) {
        showAddPatientContainer();
    }

    @FXML
    private void handleDoctorBackBtn(ActionEvent event) {
        showPatientListContainer();
    }

    @FXML
    private void handleSavePatientBtn(ActionEvent event) {

        if (!isAddPatientFormValid()) {
            UIUtils.alert("Validation Error.", "Please enter the required field.", Alert.AlertType.ERROR);
            return;
        } else {
            Patient patient = new Patient();
            patient.setPatientName(fName.getText());

            patient.setAddress(address.getText());
            patient.setContactNumber(contactNumber.getText());
            patient.setEmail(email.getText());

            java.sql.Date date = java.sql.Date.valueOf(dateOfBirth.getValue());
            patient.setDateOfBirth(date);

            if (male.isSelected()) {
                patient.setGender(male.getText());
            } else if (female.isSelected()) {
                patient.setGender(female.getText());
            } else if (other.isSelected()) {
                patient.setGender(other.getText());
            }

            patient.setVisitReason(reasonVisit.getText());

            userDao.getUserByEmail(availableDoctors.getText());
            
         
          
            patient.setDoctor(availableDoctors.getText());

            
           
            patientDao.addPatient(patient);

            clearNewForm();
            showPatientListContainer();
        }
    }

    private void clearNewForm() {
        fName.setText("");

        address.setText("");
        contactNumber.setText("");
        email.setText("");
        dateOfBirth.setValue(null);


    }

    private void clearNewAssistForm() {
        fName.setText("");
        lName.setText("");
        address.setText("");
        contactNumber.setText("");
        email.setText("");
        dateOfBirth.setValue(null);
        password.setText("");

    }

    private void showPatientContainer() {
        hideAllContainer();

        patientMenu.setStyle(selectedMenuStyle);
        patientContainer.setVisible(true);

        for (User doctor : userDao.getUsersByRole(Role.DOCTOR.getValue())) {
            MenuItem menuItem = new MenuItem(doctor.getFirstName());
            menuItem.setOnAction(e -> {
                availableDoctors.setText(menuItem.getText());
            });
            availableDoctors.getItems().add(menuItem);

            showPatientListContainer();
        }
    }

    private void showPatientListContainer() {
        savePatientCotaniner.setVisible(false);
        patientListContainer.setVisible(true);
    }

    private void showAddPatientContainer() {
        savePatientCotaniner.setVisible(true);
        patientListContainer.setVisible(false);

    }

    private boolean isAddPatientFormValid() {
        return !StringUtils.isEmpty(fName.getText().trim())
                && !StringUtils.isEmpty(address.getText().trim())
                && !StringUtils.isEmpty(contactNumber.getText().trim())
                && !StringUtils.isEmpty(reasonVisit.getText().trim())
                && (!StringUtils.isEmpty(male.getText().trim()) || !StringUtils.isEmpty(female.getText().trim()) || !StringUtils.isEmpty(other.getText().trim()))
                && !StringUtils.isEmpty(email.getText().trim())
                && !StringUtils.isEmpty(((TextField) dateOfBirth.getEditor()).getText());
    }

    private boolean isAddAssistantFormValid() {
        return !StringUtils.isEmpty(assitFName.getText().trim())
                && !StringUtils.isEmpty(assitLName.getText().trim())
                && !StringUtils.isEmpty(assistAddress.getText().trim())
                && !StringUtils.isEmpty(assistContact.getText().trim())
                && !StringUtils.isEmpty(assistPass.getText().trim())
                && (!StringUtils.isEmpty(assistMale.getText().trim()) || !StringUtils.isEmpty(assistFemale.getText().trim()) || !StringUtils.isEmpty(assistOther.getText().trim()))
                && !StringUtils.isEmpty(assistEmail.getText().trim())
                && !StringUtils.isEmpty(availableDoctors.getText().trim())
                && !StringUtils.isEmpty(((TextField) assistDateOfBirth.getEditor()).getText());
    }

    @FXML
    private void handleAssistantMenu(ActionEvent event) {
        showAssistantContainer();
    }

    @FXML
    private void handleAddAssistant(ActionEvent event) {
        showAddAssistantContainer();
    }

    @FXML
    private void handleAssistantBackBtn(ActionEvent event) {
        showAssistantListContainer();
    }

    @FXML
    private void handleSaveAssistant(ActionEvent event) {

        if (!isAddAssistantFormValid()) {
            UIUtils.alert("Validation Error.", "Please enter the required field.", Alert.AlertType.ERROR);
            return;
        } else {
            Assistant assist = new Assistant();
            assist.setFirstName(assitFName.getText());

            assist.setLastName(assitLName.getText());
            assist.setAddress(assistAddress.getText());
            assist.setContactNumber(assistContact.getText());
            assist.setEmail(assistEmail.getText());
            assist.setPassword(assistPass.getText());

            java.sql.Date date = java.sql.Date.valueOf(assistDateOfBirth.getValue());
            assist.setDateOfBirth(date);

            if (assistMale.isSelected()) {
                assist.setGender(assistMale.getText());
            } else if (assistFemale.isSelected()) {
                assist.setGender(assistFemale.getText());
            } else if (assistOther.isSelected()) {
                assist.setGender(assistOther.getText());
            }
            assist.setReportsTo(availableDoctors.getText());
            assist.setRole(Role.ASSISTANT);
            userDao.getUserByEmail(assist.getEmail());
            assist.setAssistantId(userDao.getUserByEmail(assist.getEmail()).getId());

            assistantDao.addAssistant(assist);

            clearNewAssistForm();
            showAssistantContainer();
        }
    }

    private void showAssistantContainer() {
        hideAllContainer();

        assistantMenu.setStyle(selectedMenuStyle);
        assistantContainer.setVisible(true);

        for (User doctor : userDao.getUsersByRole(Role.DOCTOR.getValue())) {
            MenuItem menuItem = new MenuItem(doctor.getFirstName());
            menuItem.setOnAction(e -> {
                availableDoctors.setText(menuItem.getText());
            });
            availableDoctors.getItems().add(menuItem);
        }

        showAssistantListContainer();
    }

    private void showAssistantListContainer() {
        addAssistantContainer.setVisible(false);
        assistantListContainer.setVisible(true);
    }

    private void showAddAssistantContainer() {
        addAssistantContainer.setVisible(true);
        assistantListContainer.setVisible(false);
    }

    @FXML
    private void handleAdminMenu(ActionEvent event) {
        showAdminContainer();
    }

    @FXML
    private void handleAddAppointment(ActionEvent event) {
        showAddAdminContainer();
    }

    @FXML
    private void handleAdminBackBtn(ActionEvent event) {
        showAdminListContainer();
    }

    @FXML
    private void handleSaveAppointment(ActionEvent event) {
        // save admin information to database
    }

    private void showAdminContainer() {
        hideAllContainer();

        adminMenu.setStyle(selectedMenuStyle);
        adminContainer.setVisible(true);

        showAdminListContainer();
    }

    private void showAdminListContainer() {
        addAppointmentContainer.setVisible(false);
        appointmentListContainer.setVisible(true);
    }

    private void showAddAdminContainer() {
        addAppointmentContainer.setVisible(true);
        appointmentListContainer.setVisible(false);
    }

    private void hideAllContainer() {
        patientMenu.setStyle(unSelectedMenuStyle);
        assistantMenu.setStyle(unSelectedMenuStyle);
        adminMenu.setStyle(unSelectedMenuStyle);

        patientContainer.setVisible(false);
        assistantContainer.setVisible(false);
        adminContainer.setVisible(false);

    }

}
