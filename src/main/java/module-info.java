module com.mycompany.helloworldtodo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.mycompany.helloworldtodo to javafx.fxml;
    exports com.mycompany.helloworldtodo;
}
