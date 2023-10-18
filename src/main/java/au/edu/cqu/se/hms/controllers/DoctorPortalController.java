package au.edu.cqu.se.hms.controllers;

import au.edu.cqu.se.hms.daos.DoctorDao;
import au.edu.cqu.se.hms.daos.PatientDao;
import au.edu.cqu.se.hms.daos.PatientMedicalHistoryDao;
import au.edu.cqu.se.hms.models.Doctor;
import au.edu.cqu.se.hms.models.Patient;
import au.edu.cqu.se.hms.models.PatientMedicalHistory;
import au.edu.cqu.se.hms.models.User;
import au.edu.cqu.se.hms.services.AuthenticationService;
import au.edu.cqu.se.hms.utils.UIUtils;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * /**
 * This class represents the controller for the Doctor Portal in the
 * application. It handles all the UI interactions and logic pertaining to the
 * portal for doctors.
 */
public class DoctorPortalController implements Initializable {

    private final String selectedMenuStyle = "-fx-text-fill: #eee; -fx-font-size: 15px; -fx-background-color: #546e7a; -fx-border-color: transparent; -fx-underline: false;";
    private final String unSelectedMenuStyle = "-fx-text-fill: #546e7a; -fx-font-size: 15px; -fx-border-color: transparent; -fx-underline: false;";

    private final ObservableList<Patient> observablePatientList = FXCollections.observableArrayList();
    private final ObservableList<PatientMedicalHistory> observableHistoryList = FXCollections.observableArrayList();

    private AuthenticationService authService;

    private PatientDao patientDao;
    private DoctorDao doctorDao;
    private PatientMedicalHistoryDao patientMedicalHistoryDao;

    private Doctor currentLoggedInDoctor;
    private User currentLoggedInUser;

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
    @FXML
    private TableView<Patient> dcotorPatientTableView;
    @FXML
    private TableColumn<Patient, Integer> doctorPatientIdCol;
    @FXML
    private TableColumn<Patient, String> dcotorPatientNameCol;
    @FXML
    private TableColumn<Patient, String> dcotorPatientDobCol;
    @FXML
    private TableColumn<Patient, String> dcotorPatientGenderCol;
    @FXML
    private TableColumn<Patient, String> dcotorPatientActionCol;
    @FXML
    private TextField paitentInfoGenderTF;
    @FXML
    private TextField patientInfoDobTF;
    @FXML
    private TextField patientInfoContactNumberTF;
    @FXML
    private TextField patientInfoEmailTF;
    @FXML
    private TextField patientInfoAddressTF;
    @FXML
    private TextArea reasonVisitTA;
    @FXML
    private Button viewMedicalHistoryBtn;
    @FXML
    private Button makeDiagonsisBtn;
    @FXML
    private Pane makeDiagonsisContainer;
    @FXML
    private TextArea doctorDiagonsisTA;
    @FXML
    private Button doctorDiagonsisBtn;
    @FXML
    private TableView<PatientMedicalHistory> diagnosisTV;
    @FXML
    private TableColumn<PatientMedicalHistory, Integer> diagnosisDateCol;
    @FXML
    private TableColumn<PatientMedicalHistory, String> diagonsisDiagnosisCol;
    @FXML
    private TableColumn<PatientMedicalHistory, String> diagnosisActionCol;

    /**
     * This method is called by the FXMLLoader when the initialization is
     * complete. It initializes the services, DAO instances, and sets up the
     * initial view for the doctor portal.
     *
     * @param url The location used to resolve relative paths for the root
     * object, or null if the location is not known.
     * @param rb The resources used to localize the root object, or null if the
     * root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        authService = new AuthenticationService();
        patientDao = PatientDao.getInstance();
        doctorDao = DoctorDao.getInstance();
        patientMedicalHistoryDao = PatientMedicalHistoryDao.getInstance();

        currentLoggedInUser = authService.getCurrentUser();

        if (currentLoggedInUser == null) {
            UIUtils.logout();
            return;
        }

        currentLoggedInDoctor = doctorDao.findDoctorByUserId(currentLoggedInUser.getId());
        if (currentLoggedInDoctor == null) {
            UIUtils.logout();
            return;
        }
        nameLbl.setText("Dr. " + currentLoggedInUser.getLastName());
        nameLbl.layoutXProperty().bind(sidebarPane.widthProperty().subtract(nameLbl.widthProperty()).divide(2));

        displayDoctorPatientTable();
        displayPatientHistoryTable();
        showAppointmentContainer();
    }

    /**
     * Sets up the table for displaying the list of patients associated with the
     * logged-in doctor.
     */
    private void displayDoctorPatientTable() {
        doctorPatientIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dcotorPatientNameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        dcotorPatientDobCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        dcotorPatientGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));

        Callback<TableColumn<Patient, String>, TableCell<Patient, String>> cellFactory = (TableColumn<Patient, String> p) -> {
            final TableCell<Patient, String> cell = new TableCell<Patient, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button btn = new Button("View details");
                        btn.setOnAction((event) -> {
                            DoctorPortalController.this.showPatientInfoContainer(getTableRow().getItem());
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        dcotorPatientActionCol.setCellFactory(cellFactory);
    }

    /**
     * Sets up the table for displaying the medical history of a specific
     * patient.
     */
    private void displayPatientHistoryTable() {
        diagnosisDateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        diagonsisDiagnosisCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDiagnosis().length() > 25
                ? cellData.getValue().getDiagnosis().substring(0, 25).replace("\n", "") + "..."
                : cellData.getValue().getDiagnosis().replace("\n", "")
        ));

        Callback<TableColumn<PatientMedicalHistory, String>, TableCell<PatientMedicalHistory, String>> cellFactory = (TableColumn<PatientMedicalHistory, String> p) -> {
            final TableCell<PatientMedicalHistory, String> cell = new TableCell<PatientMedicalHistory, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        Button btn = new Button("View details");
                        btn.setOnAction((event) -> {
                            UIUtils.alert(
                                    new SimpleDateFormat("dd MMM, yyyy").format(getTableRow().getItem().getDate()),
                                    getTableRow().getItem().getDiagnosis(),
                                    Alert.AlertType.INFORMATION);
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
            return cell;
        };
        diagnosisActionCol.setCellFactory(cellFactory);
    }

    /**
     * Handles the click event of the Appointment Menu. Redirects the user to
     * the appointments container.
     *
     * @param event The action event representing the menu item click.
     */
    @FXML
    private void handleAppointmentMenu(ActionEvent event) {
        showAppointmentContainer();
    }

    /**
     * Displays the container related to appointments.
     */
    private void showAppointmentContainer() {
        hideAllContainer();
        appointmentMenu.setStyle(selectedMenuStyle);
        appointmentContainer.setVisible(true);
    }

    /**
     * Handles the click event of the Patient Menu. Redirects the user to the
     * patient list container.
     *
     * @param event The action event representing the menu item click.
     */
    @FXML
    private void handlePatientMenu(ActionEvent event) {
        hideAllContainer();
        showPatientListContainer();
    }

    /**
     * Displays the container related to the list of patients.
     */
    private void showPatientListContainer() {
        refreshPatientListTable();

        hideAllContainer();

        patientMenu.setStyle(selectedMenuStyle);
        patientListContainer.setVisible(true);
    }

    /**
     * Updates the table to display the latest list of patients.
     */
    private void refreshPatientListTable() {
        observablePatientList.clear();
        observablePatientList.addAll(patientDao.getPatientByDoctor(currentLoggedInUser.getFirstName()));
        dcotorPatientTableView.setItems(observablePatientList);
    }

    /**
     * Handles the logout action. Redirects the user back to the login page.
     *
     * @param event The action event representing the logout button click.
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        UIUtils.logout();
    }

    /**
     * Displays detailed information of a selected patient.
     *
     * @param patient The patient whose details are to be displayed.
     */
    private void showPatientInfoContainer(Patient patient) {
        populatePatientInfo(patient);
        hideAllContainer();
        patientInfoContainer.setVisible(true);
    }

    /**
     * Populates the UI with the information of the provided patient.
     *
     * @param patient The patient whose information is to be displayed.
     */
    private void populatePatientInfo(Patient patient) {
        patientNameLbl.setText(patient.getPatientName());
        patientInfoAddressTF.setText(patient.getAddress());
        paitentInfoGenderTF.setText(patient.getGender());
        patientInfoDobTF.setText(new SimpleDateFormat("MMM, dd YYYY").format(patient.getDateOfBirth()));
        patientInfoEmailTF.setText(patient.getEmail());
        patientInfoContactNumberTF.setText(patient.getContactNumber());
        reasonVisitTA.setText(patient.getVisitReason());

        viewMedicalHistoryBtn.setOnAction((ActionEvent t) -> {
            showMedicalHistoryContainer(patient);
        });

        makeDiagonsisBtn.setOnAction((ActionEvent t) -> {
            showMakeDiagonsisContainer(patient);
        });
    }

    /**
     * Displays the container that shows the medical history of a patient.
     *
     * @param patient The patient whose medical history is to be displayed.
     */
    private void showMedicalHistoryContainer(Patient patient) {
        refreshMedicalHistoryTable(patient);
        hideAllContainer();
        medicalHistoryContainer.setVisible(true);
    }

    /**
     * Updates the table to display the latest medical history of the provided
     * patient.
     *
     * @param patient The patient whose medical history is to be displayed.
     */
    private void refreshMedicalHistoryTable(Patient patient) {
        observableHistoryList.clear();
        observableHistoryList.addAll(patientMedicalHistoryDao.getMedicalHistoryByPatient(patient.getId()));
        diagnosisTV.setItems(observableHistoryList);
    }

    /**
     * Displays the container that allows the doctor to make a diagnosis for a
     * patient.
     *
     * @param patient The patient for whom the diagnosis is to be made.
     */
    private void showMakeDiagonsisContainer(Patient patient) {
        hideAllContainer();
        makeDiagonsisContainer.setVisible(true);

        doctorDiagonsisBtn.setOnAction((ActionEvent t) -> {
            patientMedicalHistoryDao.addPatientMedicalHistory(
                    new PatientMedicalHistory(
                            new Date(),
                            patient.getId(),
                            currentLoggedInDoctor.getId(),
                            doctorDiagonsisTA.getText()
                    )
            );
            showPatientInfoContainer(patient);
        });
    }

    /**
     * Hides all the UI containers to allow for transitioning between different
     * views.
     */
    private void hideAllContainer() {
        patientMenu.setStyle(unSelectedMenuStyle);
        appointmentMenu.setStyle(unSelectedMenuStyle);

        patientListContainer.setVisible(false);
        appointmentContainer.setVisible(false);
        patientInfoContainer.setVisible(false);
        medicalHistoryContainer.setVisible(false);
        makeDiagonsisContainer.setVisible(false);
    }

}
