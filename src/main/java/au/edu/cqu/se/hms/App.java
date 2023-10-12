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
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static final List<Migration> migrations = new ArrayList<>();
    static {
        migrations.add(new Migration(1, Queries.CREATE_USER));
        migrations.add(new Migration(2, Queries.INSERT_ADMIN));
        migrations.add(new Migration(3, Queries.CREATE_SPECIALIZATION));
         migrations.add(new Migration(4, Queries.CREATE_DOCTOR));
         migrations.add(new Migration(5, Queries.CREATE_ASSISTANT));
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("signin"), 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        Migration.createTable();
        for (Migration record : migrations) {
            record.executeMigration();
        }
        launch();
    }

}
