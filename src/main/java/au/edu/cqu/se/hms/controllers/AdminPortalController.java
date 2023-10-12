package au.edu.cqu.se.hms.controllers;

import au.edu.cqu.se.hms.App;
import au.edu.cqu.se.hms.daos.DoctorDao;
import au.edu.cqu.se.hms.daos.SpecializationDao;
import au.edu.cqu.se.hms.daos.UserDao;
import au.edu.cqu.se.hms.enums.Role;
import au.edu.cqu.se.hms.models.Doctor;
import au.edu.cqu.se.hms.models.Specialization;
import au.edu.cqu.se.hms.models.User;
import au.edu.cqu.se.hms.services.AuthenticationService;
import au.edu.cqu.se.hms.utils.StringUtils;
import au.edu.cqu.se.hms.utils.UIUtils;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

/**
 *
 * @author sangharshachaulagain
 */
public class AdminPortalController implements Initializable {

    private final String selectedMenuStyle = "-fx-text-fill: #eee; -fx-font-size: 15px; -fx-background-color: #546e7a; -fx-border-color: transparent; -fx-underline: false;";
    private final String unSelectedMenuStyle = "-fx-text-fill: #546e7a; -fx-font-size: 15px; -fx-border-color: transparent; -fx-underline: false;";

    private AuthenticationService authService;
    private SpecializationDao specializationDao;

    private DoctorDao doctorDao;
    private UserDao userDao;

    private final ObservableList<Specialization> observableSpecializationList = FXCollections.observableArrayList();
    private int specializationListPage = 0;
    private final int speciailzationListPageSize = 5;

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
    private Pane specializationListContainer;
    @FXML
    private Pane addSpecializationContainer;
    @FXML
    private Pane specializationContainer;
    @FXML
    private TextField specializationFld;
    @FXML
    private TableView<Specialization> specialityTableView;
    @FXML
    private TableColumn<Specialization, Integer> specialityIDCol;
    @FXML
    private TableColumn<Specialization, String> specialityNameCol;
    @FXML
    private TableColumn<Specialization, String> specialityActionCol;
    @FXML
    private Button specialityPrevBtn;
    @FXML
    private Button specialityNextBtn;
    @FXML
    private Label specialityPaginationLbl;

    @FXML
    private TextField fName;

    @FXML
    private TextField lName;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

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
    private MenuButton reportsTo;

    @FXML
    private MenuButton specialization;

    private char gender;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthenticationService();
        specializationDao = SpecializationDao.getInstance();
        doctorDao = DoctorDao.getInstance();

        userDao = UserDao.getInstance();

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

        displaySpecializationTable();

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

    @FXML
    private void handleSaveDoctorBtn(ActionEvent event) {

        if (!isAddDoctorFormValid()) {
            UIUtils.alert("Validation Error.", "Please enter the required field.", Alert.AlertType.ERROR);
            return;
        } else {
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
                doctor.setDoctorId(userDao.getUserByEmail(doctor.getEmail()).getId());

                doctorDao.addDoctor(doctor);
            }

            clearNewForm();
            showDoctorContainer();
        }
    }

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

    private void showDoctorContainer() {
        hideAllContainer();

        doctorMenu.setStyle(selectedMenuStyle);
        doctorContainer.setVisible(true);

        showDoctorListContainer();
    }

    private void showDoctorListContainer() {
        saveDoctorCotaniner.setVisible(false);
        doctorListContainer.setVisible(true);
    }

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
        // Save the Assistant information to database
    }

    private void showAssistantContainer() {
        hideAllContainer();

        assistantMenu.setStyle(selectedMenuStyle);
        assistantContainer.setVisible(true);

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
    private void handleAddAdmin(ActionEvent event) {
        showAddAdminContainer();
    }

    @FXML
    private void handleAdminBackBtn(ActionEvent event) {
        showAdminListContainer();
    }

    @FXML
    private void handleSaveAdmin(ActionEvent event) {
        // save admin information to database
    }

    private void showAdminContainer() {
        hideAllContainer();

        adminMenu.setStyle(selectedMenuStyle);
        adminContainer.setVisible(true);

        showAdminListContainer();
    }

    private void showAdminListContainer() {
        addAdminContainer.setVisible(false);
        adminListContainer.setVisible(true);
    }

    private void showAddAdminContainer() {
        addAdminContainer.setVisible(true);
        adminListContainer.setVisible(false);
    }

    @FXML
    private void handleSpecializationMenu(ActionEvent event) {
        showSpecializationContainer();
    }

    @FXML
    private void handleAddSpecialization(ActionEvent event) {
        showAddSpecializationContainer();
    }

    @FXML
    private void handleSpecializationBackBtn(ActionEvent event) {
        showSpecializationListContainer();
    }

    @FXML
    private void handleSaveSpecialization(ActionEvent event) {
        if (!isAddSpecializaitonFormValid()) {
            UIUtils.alert("Validation Error.", "Please enter the required field.", Alert.AlertType.ERROR);
            return;
        }

        String specialization = specializationFld.getText();
        specializationDao.addSpecialization(new Specialization(specialization));

        clearAddSpecializationForm();
        showSpecializationListContainer();
    }

    private void showSpecializationContainer() {
        hideAllContainer();

        specializationMenu.setStyle(selectedMenuStyle);
        specializationContainer.setVisible(true);

        showSpecializationListContainer();
    }

    private void showSpecializationListContainer() {
        refreshSpeciailzationTable();
        addSpecializationContainer.setVisible(false);
        specializationListContainer.setVisible(true);
    }

    private void showAddSpecializationContainer() {
        clearAddSpecializationForm();
        addSpecializationContainer.setVisible(true);
        specializationListContainer.setVisible(false);
    }

    private void displaySpecializationTable() {
        specialityIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        specialityNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        Callback<TableColumn<Specialization, String>, TableCell<Specialization, String>> cellFactory = (TableColumn<Specialization, String> p) -> {
            final TableCell<Specialization, String> cell = new TableCell<Specialization, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Button btn = new Button("Edit");
                        btn.setOnAction((event) -> {
//                            showEditSpecializationContainer(getTableRow().getItem());
                            System.out.println("Show Edit Container");
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        specialityActionCol.setCellFactory(cellFactory);
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

    private boolean isAddSpecializaitonFormValid() {
        return !StringUtils.isEmpty(specializationFld.getText().trim());
    }

    private void clearAddSpecializationForm() {
        specializationFld.setText("");
    }

    private void hideAllContainer() {
        doctorMenu.setStyle(unSelectedMenuStyle);
        assistantMenu.setStyle(unSelectedMenuStyle);
        adminMenu.setStyle(unSelectedMenuStyle);
        specializationMenu.setStyle(unSelectedMenuStyle);

        doctorContainer.setVisible(false);
        assistantContainer.setVisible(false);
        adminContainer.setVisible(false);
        specializationContainer.setVisible(false);
    }

}
