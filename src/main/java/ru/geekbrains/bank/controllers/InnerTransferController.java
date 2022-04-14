package ru.geekbrains.bank.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.geekbrains.bank.DAO.TransactionDaoImpl;
import ru.geekbrains.bank.DAO.UserAccountDaoImpl;
import ru.geekbrains.bank.models.UserAccount;

public class InnerTransferController {

    private static UserAccount currentUserAccount = LogIntoAccountController.userAccount;
    private UserAccountDaoImpl userAccountDao;
    private TransactionDaoImpl transactionDao;

    @FXML
    private TextField beneficiaryIdField;
    @FXML
    private TextField amountForTransferField;
    @FXML
    private Button doTransferMoney;

    @FXML
    void initialize() {
        userAccountDao = new UserAccountDaoImpl();
        transactionDao = new TransactionDaoImpl();

        doTransferMoney.setOnAction(event -> {
            // check data from user
            String beneficiaryId = beneficiaryIdField.getText().trim();
            if (beneficiaryId == null || !userAccountDao.isUserIdContainsInDB(beneficiaryId)) {
                // wrong id
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
            // transfer money in DB
            // senderId -> beneficiaryId -> money
            boolean isTransferComplete = transactionDao.transferBetweenUsersAndWriteTransaction(currentUserAccount.getUserId(), beneficiaryId, amountForTransfer);
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
