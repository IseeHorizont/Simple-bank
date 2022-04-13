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
    exports ru.geekbrains.bank.controllers;
    opens ru.geekbrains.bank.controllers to javafx.fxml;
    exports ru.geekbrains.bank.models;
    opens ru.geekbrains.bank.models to javafx.fxml;
    exports ru.geekbrains.bank.DAO;
    opens ru.geekbrains.bank.DAO to javafx.fxml;
    exports ru.geekbrains.bank.utils;
    opens ru.geekbrains.bank.utils to javafx.fxml;
}