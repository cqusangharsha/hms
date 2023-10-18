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
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

/**
 * The AdminPortalController class provides functionality for the admin portal,
 * allowing admins to manage and view various resources such as doctors,
 * assistants, specializations, and other admin users.
 *
 */
public class AdminPortalController implements Initializable {

    // Define styles for menu selections
    private final String selectedMenuStyle = "-fx-text-fill: #eee; -fx-font-size: 15px; -fx-background-color: #546e7a; -fx-border-color: transparent; -fx-underline: false;";
    private final String unSelectedMenuStyle = "-fx-text-fill: #546e7a; -fx-font-size: 15px; -fx-border-color: transparent; -fx-underline: false;";

    // Service and DAO instances
    private AuthenticationService authService;
    private SpecializationDao specializationDao;
    private UserDao userDao;
    private DoctorDao doctorDao;
    private AssistantDao assistantDao;
    private PatientDao patientDao;

    // ObservableLists for managing list data
    private final ObservableList<Doctor> observableDoctorList = FXCollections.observableArrayList();

    private final ObservableList<Assistant> observableAssistantList = FXCollections.observableArrayList();

    private final ObservableList<Specialization> observableSpecializationList = FXCollections.observableArrayList();
    private int specializationListPage = 0;
    private final int speciailzationListPageSize = 5;

    private final ObservableList<User> observableAdminList = FXCollections.observableArrayList();
    private int adminListPage = 0;
    private final int adminListPageSize = 5;

    // JavaFX annotations for UI components
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
    @FXML
    private Pane assistantListContainer;
    @FXML
    private Pane addAssistantContainer;
    @FXML
    private Pane addAdminContainer;
    @FXML
    private Pane adminListContainer;
    @FXML
    private Hyperlink specializationMenu;

    @FXML
    private Hyperlink analyticsMenu;
    @FXML
    private Pane specializationListContainer;
    @FXML
    private Pane addSpecializationContainer;
    @FXML
    private Pane specializationContainer;
    @FXML
    private TextField specializationFld;
    @FXML
    private TextField specializationCost;
    @FXML
    private TableView<Specialization> specialityTableView;
    @FXML
    private TableColumn<Specialization, Integer> specialityIDCol;
    @FXML
    private TableColumn<Specialization, String> specialityNameCol;
    @FXML
    private Button specialityPrevBtn;
    @FXML
    private Button specialityNextBtn;
    @FXML
    private Label specialityPaginationLbl;
    @FXML
    private ToggleGroup adminGenderToggle;
    @FXML
    private TextField adminFirstNameTF;
    @FXML
    private TextField adminLastNameTF;
    @FXML
    private TextField adminEmailTF;
    @FXML
    private TextField adminPhoneTF;
    @FXML
    private TextField adminAddressTF;
    @FXML
    private RadioButton adminMaleRB;
    @FXML
    private RadioButton adminFemaleRB;
    @FXML
    private RadioButton adminOtherRB;
    @FXML
    private PasswordField adminPasswordFld;
    @FXML
    private DatePicker adminDoBFld;
    @FXML
    private TableView<User> adminTableView;
    @FXML
    private TableColumn<User, Integer> adminIDCol;
    @FXML
    private TableColumn<User, String> adminNameCol;
    @FXML
    private TableColumn<User, String> adminGenderCol;
    @FXML
    private TableColumn<User, String> adminPhoneCol;
    @FXML
    private TableColumn<User, String> adminAddressCol;
    @FXML
    private Button adminPrevBtn;
    @FXML
    private Button adminNextBtn;
    @FXML
    private Label adminPaginationLbl;

    @FXML
    private Pane analyticsListContainer;
    @FXML
    private Pane addAnalyticsContainer;
    @FXML
    private Pane analyticsContainer;

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
    private MenuButton reportsTo;

    @FXML
    private MenuButton specialization;
    @FXML
    private TableView<Doctor> adminDoctorTV;
    @FXML
    private TableColumn<Doctor, Integer> adminDoctorIdCol;
    @FXML
    private TableColumn<Doctor, String> adminDoctorNameCol;
    @FXML
    private TableColumn<Doctor, String> adminDoctorSpecilizationCol;
    @FXML
    private TableColumn<Doctor, String> adminDoctorContactCol;
    @FXML
    private TableView<Assistant> adminAssistantTV;
    @FXML
    private TableColumn<Assistant, Integer> adminAssistantIdCol;
    @FXML
    private TableColumn<Assistant, String> adminAssistantNameCol;
    @FXML
    private TableColumn<Assistant, String> adminAssistantReportsToCol;
    @FXML
    private TableColumn<Assistant, String> adminAssistantContactCol;

    @FXML
    private PieChart doctorPieChart;

    @FXML
    private PieChart specializationPieChart;

    /**
     * This method is called by the FXMLLoader when the initialization is
     * complete. It initializes the services, DAO instances, and sets up the
     * initial view for the admin portal.
     *
     * @param url The location used to resolve relative paths for the root
     * object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the
     * root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthenticationService();
        specializationDao = SpecializationDao.getInstance();
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
        displayDoctorTable();
        displayAssistantTable();
        displaySpecializationTable();
        displayAdminTable();

        showDoctorContainer();

    }

    /**
     * Handle the logout action. It logs the user out and redirects them to the
     * login page.
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        UIUtils.logout();
    }

    /**
     * Display the doctor management container.
     */
    @FXML
    private void handleDoctorMenu(ActionEvent event) {
        showDoctorContainer();
    }

    /**
     * Handles add doctor event
     */
    @FXML
    private void handleAddDoctor(ActionEvent event) {
        showAddDoctorContainer();
    }

    //method for taking user back to the dashboard
    @FXML
    private void handleDoctorBackBtn(ActionEvent event) {
        showDoctorListContainer();
    }

    /**
     * method to add new doctor
     */
    @FXML
    private void handleSaveDoctorBtn(ActionEvent event) {

        if (!isAddDoctorFormValid()) {
            UIUtils.alert("Validation Error.", "Please enter the required field.", Alert.AlertType.ERROR);
            return;
        }

        Doctor doctor = new Doctor();
        doctor.setFirstName(fName.getText());

        doctor.setLastName(lName.getText());
        doctor.setAddress(address.getText());
        doctor.setContactNumber(contactNumber.getText());
        doctor.setEmail(email.getText());
        doctor.setPassword(password.getText());

        java.sql.Date date = java.sql.Date.valueOf(dateOfBirth.getValue());
        doctor.setDateOfBirth(date);

        if (male.isSelected()) {
            doctor.setGender(male.getText());
        } else if (female.isSelected()) {
            doctor.setGender(female.getText());
        } else if (other.isSelected()) {
            doctor.setGender(other.getText());
        }
        doctor.setSpecialization(specialization.getText());
        doctor.setRole(Role.DOCTOR);
        if (userDao.signup(doctor)) {
            userDao.getUserByEmail(doctor.getEmail());
            doctor.setId(userDao.getUserByEmail(doctor.getEmail()).getId());

            doctorDao.addDoctor(doctor);
        }

        clearNewForm();
        showDoctorContainer();
    }

    /**
     * method to clear the values of the form
     */
    private void clearNewForm() {
        fName.setText("");
        lName.setText("");
        address.setText("");
        contactNumber.setText("");
        email.setText("");
        dateOfBirth.setValue(null);
        password.setText("");
        specialization.setText("");

    }

    /**
     * method to clear the assistant form values
     */
    private void clearNewAssistForm() {
        fName.setText("");
        lName.setText("");
        address.setText("");
        contactNumber.setText("");
        email.setText("");
        dateOfBirth.setValue(null);
        password.setText("");
        specialization.setText("");

    }

    /**
     * Display the doctor container
     */
    private void showDoctorContainer() {
        hideAllContainer();

        doctorMenu.setStyle(selectedMenuStyle);
        doctorContainer.setVisible(true);

        showDoctorListContainer();
    }

    /**
     * Display the list of doctors
     */
    private void showDoctorListContainer() {
        refreshDoctorTable();
        saveDoctorCotaniner.setVisible(false);
        doctorListContainer.setVisible(true);
    }

    /**
     * Display the available doctors in a table
     */
    private void displayDoctorTable() {
        adminDoctorIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        adminDoctorNameCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getUser().getFirstName() + " " + cellData.getValue().getUser().getLastName())
        );
        adminDoctorSpecilizationCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        adminDoctorContactCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getUser().getContactNumber())
        );
    }

    /**
     * method to refresh the doctor table
     */
    private void refreshDoctorTable() {
        observableDoctorList.clear();
        observableDoctorList.addAll(doctorDao.findAll());
        adminDoctorTV.setItems(observableDoctorList);
    }

    /**
     * method to show add doctor form
     */
    private void showAddDoctorContainer() {
        saveDoctorCotaniner.setVisible(true);
        doctorListContainer.setVisible(false);
        for (Specialization specialist : specializationDao.getAllSpecializations()) {
            MenuItem menuItem = new MenuItem(specialist.getName());
            menuItem.setOnAction(e -> {
                specialization.setText(menuItem.getText());
            });
            specialization.getItems().add(menuItem);
        }
    }

    /**
     * method to validate add doctor
     */
    private boolean isAddDoctorFormValid() {
        return !StringUtils.isEmpty(fName.getText().trim())
                && !StringUtils.isEmpty(lName.getText().trim())
                && !StringUtils.isEmpty(address.getText().trim())
                && !StringUtils.isEmpty(contactNumber.getText().trim())
                && !StringUtils.isEmpty(password.getText().trim())
                && (!StringUtils.isEmpty(male.getText().trim()) || !StringUtils.isEmpty(female.getText().trim()) || !StringUtils.isEmpty(other.getText().trim()))
                && !StringUtils.isEmpty(email.getText().trim())
                && !StringUtils.isEmpty(specialization.getText().trim())
                && !StringUtils.isEmpty(((TextField) dateOfBirth.getEditor()).getText());
    }

    /**
     * method to validate assistant form
     */
    private boolean isAddAssistantFormValid() {
        return !StringUtils.isEmpty(assitFName.getText().trim())
                && !StringUtils.isEmpty(assitLName.getText().trim())
                && !StringUtils.isEmpty(assistAddress.getText().trim())
                && !StringUtils.isEmpty(assistContact.getText().trim())
                && !StringUtils.isEmpty(assistPass.getText().trim())
                && (!StringUtils.isEmpty(assistMale.getText().trim()) || !StringUtils.isEmpty(assistFemale.getText().trim()) || !StringUtils.isEmpty(assistOther.getText().trim()))
                && !StringUtils.isEmpty(assistEmail.getText().trim())
                && !StringUtils.isEmpty(reportsTo.getText().trim())
                && !StringUtils.isEmpty(((TextField) assistDateOfBirth.getEditor()).getText());
    }

    // methods to handle assitant containers
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

    /**
     * method to add new assistant
     */
    @FXML
    private void handleSaveAssistant(ActionEvent event) {

        if (!isAddAssistantFormValid()) {
            UIUtils.alert("Validation Error.", "Please enter the required field.", Alert.AlertType.ERROR);
            return;
        }
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
        assist.setReportsTo(reportsTo.getText());
        assist.setRole(Role.ASSISTANT);
        if (userDao.signup(assist)) {
            userDao.getUserByEmail(assist.getEmail());
            assist.setAssistantId(userDao.getUserByEmail(assist.getEmail()).getId());

            assistantDao.addAssistant(assist);
        }

        clearNewAssistForm();
        showAssistantContainer();
    }

    /**
     * Display the assistant container
     */
    private void showAssistantContainer() {
        hideAllContainer();

        assistantMenu.setStyle(selectedMenuStyle);
        assistantContainer.setVisible(true);
        reportsTo.getItems().clear();
        for (User doctor : userDao.getUsersByRole(Role.DOCTOR)) {
            MenuItem menuItem = new MenuItem(doctor.getFirstName());
            menuItem.setOnAction(e -> {
                reportsTo.setText(menuItem.getText());
            });
            reportsTo.getItems().add(menuItem);
        }

        showAssistantListContainer();
    }

    private void showAssistantListContainer() {
        refreshAssistantTable();
        addAssistantContainer.setVisible(false);
        assistantListContainer.setVisible(true);
    }

    /**
     * Display the available assistants
     */
    private void displayAssistantTable() {
        adminAssistantIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        adminAssistantNameCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getUser().getFirstName() + " " + cellData.getValue().getUser().getLastName())
        );
        adminAssistantReportsToCol.setCellValueFactory(new PropertyValueFactory<>("reportsTo"));
        adminAssistantContactCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getUser().getContactNumber())
        );
    }

    private void refreshAssistantTable() {
        observableAssistantList.clear();
        observableAssistantList.addAll(assistantDao.findAll());
        adminAssistantTV.setItems(observableAssistantList);
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
    private void handleAddAdmin(ActionEvent event) {
        showAddAdminContainer();
    }

    @FXML
    private void handleAdminBackBtn(ActionEvent event) {
        showAdminListContainer();
    }

    //method to add new admin 
    @FXML
    private void handleSaveAdmin(ActionEvent event) {
        if (!isAddAdminFormValid()) {
            UIUtils.alert("Validation Error.", "Please enter the required field.", Alert.AlertType.ERROR);
            return;
        }
        userDao.signup(
                new User(adminFirstNameTF.getText(),
                        adminLastNameTF.getText(),
                        Date.from(
                                adminDoBFld.getValue()
                                        .atStartOfDay(ZoneId.systemDefault())
                                        .toInstant()
                        ), ((RadioButton) adminGenderToggle.getSelectedToggle()).getText(),
                        adminPhoneTF.getText(),
                        adminEmailTF.getText(),
                        adminPasswordFld.getText(),
                        adminAddressTF.getText(),
                        Role.ADMIN)
        );
        clearAdminForm();
        showAdminListContainer();
    }

    // method to check the validation of admin
    private boolean isAddAdminFormValid() {
        return !StringUtils.isEmpty(adminFirstNameTF.getText().trim())
                && !StringUtils.isEmpty(adminLastNameTF.getText().trim())
                && !StringUtils.isEmpty(adminEmailTF.getText().trim())
                && !StringUtils.isEmpty(adminPasswordFld.getText().trim())
                && !StringUtils.isEmpty(adminPhoneTF.getText().trim())
                && !StringUtils.isEmpty(adminAddressTF.getText().trim())
                && adminDoBFld.getValue() != null
                && adminGenderToggle.getSelectedToggle() != null;
    }

    private void showAdminContainer() {
        hideAllContainer();

        adminMenu.setStyle(selectedMenuStyle);
        adminContainer.setVisible(true);

        showAdminListContainer();
    }

    private void showAdminListContainer() {
        refreshAdminTable();
        addAdminContainer.setVisible(false);
        adminListContainer.setVisible(true);
    }

    private void showAddAdminContainer() {
        clearAdminForm();
        addAdminContainer.setVisible(true);
        adminListContainer.setVisible(false);
    }

    //method to display available admin
    private void displayAdminTable() {
        adminIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        adminNameCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName())
        );
        adminGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        adminPhoneCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        adminAddressCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
    }

    //method to refresh admin table
    private void refreshAdminTable() {
        observableAdminList.clear();
        List<User> userList;
        userList = userDao.getUsersByRole(Role.ADMIN, adminListPage, adminListPageSize);
        observableAdminList.addAll(userList);
        adminTableView.setItems(observableAdminList);

        handleAdminPaginationView();
    }

    //method to handle the pagination of admin table
    private void handleAdminPaginationView() {
        List<User> totalAdmin;
        totalAdmin = userDao.getUsersByRole(Role.ADMIN);

        if (totalAdmin.isEmpty()) {
            adminPaginationLbl.setVisible(false);
            adminPrevBtn.setVisible(false);
            adminNextBtn.setVisible(false);
        } else {
            adminPaginationLbl.setVisible(true);
            adminPrevBtn.setVisible(true);
            adminNextBtn.setVisible(true);
        }
        adminPaginationLbl.setText((adminListPage + 1) + " of " + (((totalAdmin.size() - 1) / adminListPageSize) + 1) + " pages");
        if (adminListPage <= 0) {
            adminPrevBtn.setDisable(true);
        } else {
            adminPrevBtn.setDisable(false);
        }

        if ((adminListPage + 1) * adminListPageSize >= totalAdmin.size()) {
            adminNextBtn.setDisable(true);
        } else {
            adminNextBtn.setDisable(false);
        }
    }

    @FXML
    private void handleAdminPrevBtn(ActionEvent event) {
        adminListPage -= 1;
        refreshAdminTable();
    }

    @FXML
    private void handleAdminNextBtn(ActionEvent event) {
        adminListPage += 1;
        refreshAdminTable();
    }

    //method to clear the value of admin form
    private void clearAdminForm() {
        adminFirstNameTF.setText("");
        adminLastNameTF.setText("");
        adminEmailTF.setText("");
        adminPasswordFld.setText("");
        adminPhoneTF.setText("");
        adminAddressTF.setText("");
        adminDoBFld.setValue(null);
        if (adminGenderToggle.getSelectedToggle() != null) {
            ((RadioButton) adminGenderToggle.getSelectedToggle()).setSelected(false);
        }
    }

    @FXML
    private void handleSpecializationMenu(ActionEvent event) {
        showSpecializationContainer();
    }

    @FXML
    private void handleAnalyticsMenu(ActionEvent event) {
        showAnalyticsContainer();
    }

    @FXML
    private void handleAddSpecialization(ActionEvent event) {
        showAddSpecializationContainer();
    }

    @FXML
    private void handleSpecializationBackBtn(ActionEvent event) {
        showSpecializationListContainer();
    }

    //method to save new specialization
    @FXML
    private void handleSaveSpecialization(ActionEvent event) {
        if (!isAddSpecializaitonFormValid()) {
            UIUtils.alert("Validation Error.", "Please enter the required field.", Alert.AlertType.ERROR);
            return;
        }

        String specialization = specializationFld.getText();
        String cost = specializationCost.getText();
        specializationDao.addSpecialization(new Specialization(specialization, cost));

        clearAddSpecializationForm();
        showSpecializationListContainer();
    }

    //method to list the specialization
    private void showSpecializationContainer() {
        hideAllContainer();

        specializationMenu.setStyle(selectedMenuStyle);
        specializationContainer.setVisible(true);

        showSpecializationListContainer();
    }

    //method to show the analytics container
    private void showAnalyticsContainer() {
        hideAllContainer();

        analyticsMenu.setStyle(selectedMenuStyle);
        analyticsContainer.setVisible(true);

        showAnalyticsListContainer();
    }

    private void showSpecializationListContainer() {
        refreshSpeciailzationTable();
        addSpecializationContainer.setVisible(false);
        specializationListContainer.setVisible(true);
    }

    private void showAnalyticsListContainer() {

        addAnalyticsContainer.setVisible(false);
        analyticsListContainer.setVisible(true);
    }

    private void showAddSpecializationContainer() {
        clearAddSpecializationForm();
        addSpecializationContainer.setVisible(true);
        specializationListContainer.setVisible(false);
    }

    private void displaySpecializationTable() {
        specialityIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        specialityNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void refreshSpeciailzationTable() {
        observableSpecializationList.clear();
        List<Specialization> specializationList;
        specializationList = specializationDao.getAllSpecializations(specializationListPage, speciailzationListPageSize);
        observableSpecializationList.addAll(specializationList);
        specialityTableView.setItems(observableSpecializationList);

        handleSpecializationPaginationView();
    }

    @FXML
    private void handleSpecialityPrevBtn(ActionEvent event) {
        specializationListPage -= 1;
        refreshSpeciailzationTable();
    }

    @FXML
    private void handleSpecialityNextBtn(ActionEvent event) {
        specializationListPage += 1;
        refreshSpeciailzationTable();
    }

    private void handleSpecializationPaginationView() {
        List<Specialization> totalSpecialization;
        totalSpecialization = specializationDao.getAllSpecializations();

        if (totalSpecialization.isEmpty()) {
            specialityPaginationLbl.setVisible(false);
            specialityPrevBtn.setVisible(false);
            specialityNextBtn.setVisible(false);
        } else {
            specialityPaginationLbl.setVisible(true);
            specialityPrevBtn.setVisible(true);
            specialityNextBtn.setVisible(true);
        }
        specialityPaginationLbl.setText((specializationListPage + 1) + " of " + (((totalSpecialization.size() - 1) / speciailzationListPageSize) + 1) + " pages");
        if (specializationListPage <= 0) {
            specialityPrevBtn.setDisable(true);
        } else {
            specialityPrevBtn.setDisable(false);
        }

        if ((specializationListPage + 1) * speciailzationListPageSize >= totalSpecialization.size()) {
            specialityNextBtn.setDisable(true);
        } else {
            specialityNextBtn.setDisable(false);
        }
    }

    @FXML
    private TableView<Patient> appointmentTable;

    @FXML
    private TableColumn<Patient, String> patientNameColumn;

    @FXML
    private TableColumn<Patient, String> doctorColumn;

    //method to load the analytics report
    @FXML
    public void loadAppointmentsToPieChart() {
        List<Patient> appointments = patientDao.getAppointments();
        Map<String, Integer> doctorCount = new HashMap<>();

        for (Patient appointment : appointments) {
            doctorCount.put(appointment.getDoctor(), doctorCount.getOrDefault(appointment.getDoctor(), 0) + 1);

        }

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<String, Integer> entry : doctorCount.entrySet()) {
            pieChartData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        loadSpecializationToPieChart();
        
        doctorPieChart.setData(pieChartData);
        doctorPieChart.setTitle("Doctor appoinments");
        doctorPieChart.layout();
    }

    //method to load the specialization for analytics report
    @FXML
    public void loadSpecializationToPieChart() {
        List<Specialization> specializations = specializationDao.getAllSpecializations();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Specialization special : specializations) {

            pieChartData.add(new PieChart.Data(special.getName(), Double.parseDouble(special.getCostCheckup())));
        }
        

        specializationPieChart.setData(pieChartData);
        specializationPieChart.setLabelLineLength(20);
        specializationPieChart.setLegendVisible(true);
        specializationPieChart.setLabelsVisible(true);

        specializationPieChart.setTitle("Specialization Costs");
        specializationPieChart.layout();

    }

    private boolean isAddSpecializaitonFormValid() {
        return !StringUtils.isEmpty(specializationFld.getText().trim())
                && !StringUtils.isEmpty(specializationCost.getText().trim());
    }

    private void clearAddSpecializationForm() {
        specializationFld.setText("");
        specializationCost.setText("");
    }

    //method to hide all containers
    private void hideAllContainer() {
        doctorMenu.setStyle(unSelectedMenuStyle);
        assistantMenu.setStyle(unSelectedMenuStyle);
        adminMenu.setStyle(unSelectedMenuStyle);
        specializationMenu.setStyle(unSelectedMenuStyle);
        analyticsMenu.setStyle(unSelectedMenuStyle);

        doctorContainer.setVisible(false);
        assistantContainer.setVisible(false);
        adminContainer.setVisible(false);
        specializationContainer.setVisible(false);
        analyticsContainer.setVisible(false);
    }

}
