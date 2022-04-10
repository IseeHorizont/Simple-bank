package ru.geekbrains.bank;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class LogIntoAccountController {

    public static UserAccount userAccount;

    @FXML
    private TextField userName;

    @FXML
    private TextField userPassword;

    @FXML
    private Button logIntoUserAccount;


    @FXML
    void initialize() {
        logIntoUserAccount.setOnAction(event -> {
            String currentUserName = userName.getText().trim();
            String currentUserPassword = userPassword.getText().trim();
            // check data from user
            if (currentUserName == null || currentUserName.isEmpty()) {
                printAlert(Alert.AlertType.ERROR, "Ошибка ввода ФИО", "Введите свои фамилию, имя, отчество");
                return;
            }
            if (currentUserPassword == null || currentUserPassword.isEmpty()) {
                printAlert(Alert.AlertType.ERROR, "Ошибка ввода пароля", "Введите правильный пароль из 4-х цифр");
                return;
            }
            // go to DB & verified user's name & user's password
            UserAccount currentUserAccount = SQLHandler.isAuthorize(currentUserName, currentUserPassword);
            userAccount = currentUserAccount;

            if (currentUserAccount == null) {
                printAlert(Alert.AlertType.ERROR, "Ошибка авторизации", "ФИО или пароль введены неверно");
                return;
            }

            logIntoUserAccount.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/usermenu.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Личный кабинет пользователя");
            stage.setScene(new Scene(root, 1000, 680));
            stage.showAndWait();
        });
    }


    private static void printAlert(Alert.AlertType type, String title, String messageToUser){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(messageToUser);
        alert.showAndWait();
    }
}
