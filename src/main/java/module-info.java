module com.mycompany.todoapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.todoapp to javafx.fxml;
    exports com.mycompany.todoapp;
}
