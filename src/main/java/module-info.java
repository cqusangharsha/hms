module au.edu.cqu.se.hms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens au.edu.cqu.se.hms to javafx.fxml;
    exports au.edu.cqu.se.hms;
}
