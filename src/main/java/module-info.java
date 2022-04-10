module ru.geekbrains.bank {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires org.json;

    opens ru.geekbrains.bank to javafx.fxml;
    exports ru.geekbrains.bank;
}