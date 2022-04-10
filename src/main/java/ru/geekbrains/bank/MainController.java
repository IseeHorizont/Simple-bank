package ru.geekbrains.bank;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    public static Stage stage;

    @FXML
    public void openWindowForCreateAccount() throws IOException {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("/create-form.fxml"));
        Scene scene = new Scene(newLoader.load(), 500, 530);
        stage.setScene(scene);
        stage.setTitle("Create your bank account");
        stage.showAndWait();
    }

    @FXML
    public void openWindowForLoginAccount() throws IOException {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader newLoader = new FXMLLoader(getClass().getResource("/login-form.fxml"));
        Scene scene = new Scene(newLoader.load(), 420, 450);
        stage.setScene(scene);
        stage.setTitle("Log into your bank account");
        stage.showAndWait();
    }

    @FXML
    public void exitMain(ActionEvent event) {
        Platform.runLater(() -> {
            System.exit(0);
        });
    }

    @FXML
    public static void exitStage() {
        Platform.runLater(() -> {
            stage.close();
        });
    }
}
