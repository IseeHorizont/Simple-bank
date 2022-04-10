package ru.geekbrains.bank;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class InnerTransferController {

    // TODO need or not ???
    private static UserAccount currentUserAccount = LogIntoAccountController.userAccount;

    @FXML
    private TextField beneficiaryIdField;

    @FXML
    private TextField amountForTransferField;

    @FXML
    private Button doTransferMoney;

    @FXML
    void initialize() {
        doTransferMoney.setOnAction(event -> {
            // TODO check data from user
            String beneficiaryId = beneficiaryIdField.getText().trim();
            if(beneficiaryId == null || !SQLHandler.isUserIdContainsInDB(beneficiaryId)) {
                // TODO wrong id
                printAlert(Alert.AlertType.ERROR, "Ошибка ввода", "Неверный номер счёта получателя");
                return;
            }
            int amountForTransfer = 0;
            try {
                amountForTransfer = Integer.parseInt(amountForTransferField.getText().trim());
            } catch (Exception ex) {
                printAlert(Alert.AlertType.ERROR, null, "Введите сумму цифрами");
                return;
            }

            // TODO transfer money in DB
            // TODO get String of 'currentDate'
            // DateFormat currentDate = new SimpleDateFormat("dd.MM.yyyy");
            // Date date = new Date();
            var currentDate = "09.04.2022";
            // current date -> senderId -> beneficiaryId -> money
            boolean isTransferComplete = SQLHandler.transferBetweenUsersAndWriteTransaction(currentDate, currentUserAccount.getUserId(), beneficiaryId, amountForTransfer);
            if (!isTransferComplete) {
                printAlert(Alert.AlertType.ERROR, "Ошибка перевода средств", "У Вас недостаточно средств на счету\nили что-то пошло не так...");
                return;
            }
            printAlert(Alert.AlertType.INFORMATION, "Перевод средств", "Перевод успешно выполнен");
            doTransferMoney.getScene().getWindow().hide();
        });

    }

    private static void printAlert(Alert.AlertType type, String title,  String messageToUser){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(messageToUser);
        alert.showAndWait();
    }
}
