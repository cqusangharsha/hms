module au.edu.cqu.se.hms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.base;

    opens au.edu.cqu.se.hms to javafx.fxml;
    opens au.edu.cqu.se.hms.controllers to javafx.fxml;
    opens au.edu.cqu.se.hms.models to javafx.base;
    exports au.edu.cqu.se.hms.controllers;
    exports au.edu.cqu.se.hms;
}
