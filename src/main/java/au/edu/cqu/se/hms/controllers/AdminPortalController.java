package au.edu.cqu.se.hms.controllers;

import au.edu.cqu.se.hms.App;
import au.edu.cqu.se.hms.daos.SpecializationDao;
import au.edu.cqu.se.hms.daos.UserDao;
import au.edu.cqu.se.hms.enums.Role;
import au.edu.cqu.se.hms.models.Specialization;
import au.edu.cqu.se.hms.models.User;
import au.edu.cqu.se.hms.services.AuthenticationService;
import au.edu.cqu.se.hms.utils.StringUtils;
import au.edu.cqu.se.hms.utils.UIUtils;
import java.io.IOException;
import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
    private UserDao userDao;

    private final ObservableList<Specialization> observableSpecializationList = FXCollections.observableArrayList();
    private int specializationListPage = 0;
    private final int speciailzationListPageSize = 5;

    private final ObservableList<User> observableAdminList = FXCollections.observableArrayList();
    private int adminListPage = 0;
    private final int adminListPageSize = 5;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthenticationService();
        specializationDao = SpecializationDao.getInstance();
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

        displayAdminTable();
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

    private void handleDoctorBackBtn(ActionEvent event) {
        showDoctorListContainer();
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
    }

    @FXML
    private void handleAssistantMenu(ActionEvent event) {
        showAssistantContainer();
    }

    @FXML
    private void handleAddAssistant(ActionEvent event) {
        showAddAssistantContainer();
    }

    private void handleAssistantBackBtn(ActionEvent event) {
        showAssistantListContainer();
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

    private void displayAdminTable() {
        adminIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        adminNameCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName())
        );
        adminGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        adminPhoneCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
        adminAddressCol.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));
    }
    
    private void refreshAdminTable() {
        observableAdminList.clear();
        List<User> userList;
        userList = userDao.getUsersByRole(Role.ADMIN, adminListPage, adminListPageSize);
        observableAdminList.addAll(userList);
        adminTableView.setItems(observableAdminList);

        handleAdminPaginationView();
    }
    
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
