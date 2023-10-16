package au.edu.cqu.se.hms.controllers;

import au.edu.cqu.se.hms.App;
import au.edu.cqu.se.hms.daos.AssistantDao;
import au.edu.cqu.se.hms.daos.DoctorDao;
import au.edu.cqu.se.hms.daos.PatientDao;
import au.edu.cqu.se.hms.daos.UserDao;
import au.edu.cqu.se.hms.enums.Role;
import au.edu.cqu.se.hms.models.Assistant;
import au.edu.cqu.se.hms.models.Patient;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 *
 * @author sudeep_sharma
 */
public class AssistantPortalController implements Initializable {

    private final String selectedMenuStyle = "-fx-text-fill: #eee; -fx-font-size: 15px; -fx-background-color: #546e7a; -fx-border-color: transparent; -fx-underline: false;";
    private final String unSelectedMenuStyle = "-fx-text-fill: #546e7a; -fx-font-size: 15px; -fx-border-color: transparent; -fx-underline: false;";

    private AuthenticationService authService;

    private UserDao userDao;
    private PatientDao patientDao;

    @FXML
    private Label nameLbl;
    @FXML
    private Pane sidebarPane;
    @FXML
    private Hyperlink patientMenu;

    @FXML
    private Hyperlink billingMenu;

    @FXML
    private Hyperlink appointmentMenu;
    @FXML
    private Pane patientContainer;
    @FXML
    private Pane appointmentContainer;
    @FXML
    private Pane billingContainer;
    @FXML
    private Pane patientListContainer;
    @FXML
    private Pane savePatientCotaniner;
    @FXML
    private Pane appointmentListContainer;

    @FXML
    private Pane addAppointmentContainer;
    @FXML
    private Pane billingListContainer;

    @FXML
    private TextField fName;

    @FXML
    private TextField email;

    @FXML
    private TextField address;

    @FXML
    private TextField contactNumber;

    @FXML
    private RadioButton male;

    @FXML
    private RadioButton female;

    @FXML
    private RadioButton other;

    @FXML
    private DatePicker dateOfBirth;

    @FXML
    private MenuButton availableDoctors;

    @FXML
    private MenuButton selectedTime;

    @FXML
    private TextArea reasonApointment;

    @FXML
    private TextArea reasonVisit;

    @FXML
    private MenuButton patientName;

    @FXML
    private DatePicker selectedate;

    @FXML
    private Text billingText;

    @FXML
    private TextField checkPatient;
    @FXML
    private TableView<Patient> table;

    @FXML
    private TableColumn<Patient, String> doc;
    
    @FXML
    private TableColumn<Patient, String> visit;

    @FXML
    private TableColumn<Patient, String> cost;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthenticationService();

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
    private void handleAppointmentMenu(ActionEvent event) {
        showAppointmentContainer();
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
        }
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
        // userDao.getUserByEmail(availableDoctors.getText());

        //   patient.setDoctor(availableDoctors.getText());
        patientDao.addPatient(patient);

        clearNewForm();
        showPatientListContainer();
    }

    private void clearNewForm() {
        fName.setText("");

        address.setText("");
        contactNumber.setText("");
        email.setText("");
        dateOfBirth.setValue(null);

    }

    private void clearNewAssistForm() {
        patientName.setText("");
        selectedate.setValue(null);
        selectedTime.setText(null);
        availableDoctors.setText(null);
        reasonApointment.setText("");

    }

    private void showPatientContainer() {
        hideAllContainer();

        patientMenu.setStyle(selectedMenuStyle);
        patientContainer.setVisible(true);

        showPatientListContainer();

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

    private boolean isAddAppointmentFormValid() {
        return !StringUtils.isEmpty(patientName.getText().trim())
                && !StringUtils.isEmpty(((TextField) selectedate.getEditor()).getText());
    }

    @FXML
    private void handleAssistantMenu(ActionEvent event) {
        showAppointmentContainer();
    }

    @FXML
    private void handleSaveAppointment(ActionEvent event) {

        if (!isAddAppointmentFormValid()) {
            UIUtils.alert("Validation Error.", "Please enter the required field.", Alert.AlertType.ERROR);
            return;
        }
        Patient patient = new Patient();
        patient.setPatientName(patientName.getText());
        java.sql.Date date = java.sql.Date.valueOf(selectedate.getValue());
        patient.setSelectedDate(date);
        patient.setSelectedTime(selectedTime.getText());
        patient.setDoctor(availableDoctors.getText());
        patient.setVisitReason(reasonApointment.getText());

        patientDao.addAppointment(patient);

        clearNewAssistForm();
        showAppointmentContainer();
    }

    private void showAppointmentContainer() {
        hideAllContainer();

        appointmentMenu.setStyle(selectedMenuStyle);
        appointmentContainer.setVisible(true);

        availableDoctors.getItems().clear();
        for (User doctor : userDao.getUsersByRole(Role.DOCTOR)) {
            MenuItem menuItem = new MenuItem(doctor.getFirstName());
            menuItem.setOnAction(e -> {
                availableDoctors.setText(menuItem.getText());
            });
            availableDoctors.getItems().add(menuItem);
        }

        patientName.getItems().clear();
        for (Patient patient : patientDao.getAllPatient()) {
            MenuItem menuItem = new MenuItem(patient.getPatientName());
            menuItem.setOnAction(e -> {
                patientName.setText(menuItem.getText());
            });
            patientName.getItems().add(menuItem);
        }

        for (int i = 9; i <= 17; i++) {
            String timeLabel = i < 12 ? i + ":00 AM" : (i == 12 ? "12:00 PM" : (i - 12) + ":00 PM");
            MenuItem menuItem = new MenuItem(timeLabel);

            menuItem.setOnAction(e -> {
                selectedTime.setText(menuItem.getText());
            });

            selectedTime.getItems().add(menuItem);
        }

        showAppointmentListContainer();
    }

    private void showAppointmentListContainer() {
        addAppointmentContainer.setVisible(false);
        appointmentListContainer.setVisible(true);
    }

    private void showAddAssistantContainer() {
        addAppointmentContainer.setVisible(true);
        appointmentListContainer.setVisible(false);
    }

    @FXML
    private void handleBillingMenu(ActionEvent event) {
        showBillingContainer();
    }

    @FXML
    private void handleAddAppointment(ActionEvent event) {
        showAddAdminContainer();
    }

    @FXML
    private void handleBillingBackBtn(ActionEvent event) {
        showBillingListContainer();
    }

    /**
     * @FXML private void handleSaveBilling(ActionEvent event) {
     *
     * TableView<Patient> table = new TableView<>();
     *
     * List<Patient> dataList =
     * patientDao.getPatientBill(checkPatient.getText());
     * ObservableList<Patient> data =
     * FXCollections.observableArrayList(dataList);
     *
     * TableColumn<Patient, String> ptName = new TableColumn<>("Patient Name");
     * ptName.setCellValueFactory(new PropertyValueFactory<>("name"));
     *
     * TableColumn<Patient, String> cost = new TableColumn<>("Cost");
     * cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
     *
     * table.getColumns().addAll(ptName, cost); table.setItems(data);
     *
     * System.out.println("Cost" + cost);
     *
     * }*
     */
    @FXML
    private void handleSaveBilling(ActionEvent event) {

        
        if (doc.getCellValueFactory() == null) {
            doc.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        }
        
        if (visit.getCellValueFactory() == null) {
            visit.setCellValueFactory(new PropertyValueFactory<>("visitReason"));
        }

        if (cost.getCellValueFactory() == null) {
            cost.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        }

        List<Patient> dataList = patientDao.getPatientBill(checkPatient.getText());
        ObservableList<Patient> data = FXCollections.observableArrayList(dataList);

        table.setItems(data);

        // If you want to print the data for debugging:
        for (Patient patient : data) {
            System.out.println("Name: " + patient.getPatientName() + ", Cost: " + patient.getTotalCost());
        }
    }

    private void showBillingContainer() {
        hideAllContainer();

        billingMenu.setStyle(selectedMenuStyle);
        billingContainer.setVisible(true);

        showBillingListContainer();
    }

    private void showBillingListContainer() {
        addAppointmentContainer.setVisible(false);
        billingListContainer.setVisible(true);
    }

    private void showAddAdminContainer() {
        addAppointmentContainer.setVisible(true);
        billingListContainer.setVisible(false);
    }

    private void hideAllContainer() {
        patientMenu.setStyle(unSelectedMenuStyle);

        billingMenu.setStyle(unSelectedMenuStyle);

        patientContainer.setVisible(false);
        appointmentContainer.setVisible(false);
        billingContainer.setVisible(false);

    }

}
