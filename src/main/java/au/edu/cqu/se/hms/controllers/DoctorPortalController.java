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
 *
 * @author Dell
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
        refreshPatientListTable();

        hideAllContainer();

        patientMenu.setStyle(selectedMenuStyle);
        patientListContainer.setVisible(true);
    }

    private void refreshPatientListTable() {
        observablePatientList.clear();
        observablePatientList.addAll(patientDao.getPatientByDoctor(currentLoggedInUser.getFirstName()));
        dcotorPatientTableView.setItems(observablePatientList);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        UIUtils.logout();
    }

    private void showPatientInfoContainer(Patient patient) {
        populatePatientInfo(patient);
        hideAllContainer();
        patientInfoContainer.setVisible(true);
    }

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

    private void showMedicalHistoryContainer(Patient patient) {
        refreshMedicalHistoryTable(patient);
        hideAllContainer();
        medicalHistoryContainer.setVisible(true);
    }

    private void refreshMedicalHistoryTable(Patient patient) {
        observableHistoryList.clear();
        observableHistoryList.addAll(patientMedicalHistoryDao.getMedicalHistoryByPatient(patient.getId()));
        diagnosisTV.setItems(observableHistoryList);
    }
    
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
