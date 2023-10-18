package au.edu.cqu.se.hms;

import au.edu.cqu.se.hms.models.Migration;
import au.edu.cqu.se.hms.utils.Queries;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The main JavaFX application class.
 *
 * This application initializes a list of migrations that define database table
 * structures and data insertions and executes them. Additionally, it manages
 * the primary JavaFX scene and views.
 *
 */
public class App extends Application {

    /**
     * The main scene of the application.
     */
    private static Scene scene;

    /**
     * A list of migrations to be executed on application start.
     */
    private static final List<Migration> migrations = new ArrayList<>();

    // Static block to initialize the migrations.
    static {
        migrations.add(new Migration(1, Queries.CREATE_USER));
        migrations.add(new Migration(2, Queries.INSERT_ADMIN));
        migrations.add(new Migration(3, Queries.CREATE_SPECIALIZATION));
        migrations.add(new Migration(12, Queries.INSERT_SPECIALIZATION));
        migrations.add(new Migration(4, Queries.CREATE_DOCTOR));
        migrations.add(new Migration(5, Queries.CREATE_ASSISTANT));
        migrations.add(new Migration(6, Queries.CREATE_PATIENT));

        migrations.add(new Migration(41, Queries.CREATE_PATIENT_MEDICAL_HISTORY));
        migrations.add(new Migration(11, Queries.CREATE_PATIENT_APPOINTMENT));
    }

    /**
     * Starts the JavaFX application.
     *
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("signin"), 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Sets the root view for the main scene.
     *
     * @param fxml The name of the FXML file to load (without extension).
     * @throws IOException If an error occurs while loading the FXML file.
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Loads the specified FXML file.
     *
     * @param fxml The name of the FXML file to load (without extension).
     * @return A Parent object representing the root view.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * The main method for the application.
     * <p>
     * Initializes and executes the database migrations and then launches the
     * JavaFX application.
     * </p>
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        Migration.createTable();
        for (Migration record : migrations) {
            record.executeMigration();
        }
        launch();
    }
}
