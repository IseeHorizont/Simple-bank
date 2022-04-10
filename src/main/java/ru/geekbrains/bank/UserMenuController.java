package ru.geekbrains.bank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class UserMenuController {

    private static UserAccount currentUserAccount = LogIntoAccountController.userAccount;

    @FXML
    private Text balance;

    @FXML
    private Text accountNumber;

    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();

    @FXML
    private TableView<Transaction> tableOfTransactions;

    @FXML
    private TableColumn<Transaction, String> columnTransactionDate;

    @FXML
    private TableColumn<Transaction, String> columnTransactionSender;

    @FXML
    private TableColumn<Transaction, String> columnTransactionBeneficiary;

    @FXML
    private TableColumn<Transaction, String> columnTransactionAmount;

    @FXML
    private Button addMoneyButton;

    @FXML
    private Button doTransferButton;

    @FXML
    private Button showAccountInfoButton;

    @FXML
    private Button closeAccountButton;

    @FXML
    private Button logOutFromUserMenuButton;

    @FXML
    private Button giveDonationButton;

    @FXML
    private Text usdTextField;

    @FXML
    private Text euroTextField;

    @FXML
    private Text gbpTextField;

    @FXML
    private Text cadTextField;

//    @FXML
//    private Text applePriceField;
//
//    @FXML
//    private Text microsoftPriceField;
//
//    @FXML
//    private Text jpmorganPriceField;
//
//    @FXML
//    private Text cocaColaPriceField;
//
//    @FXML
//    private Text mcDonaldsPriceField;

    @FXML
    public void initialize() {
        // set up balance & account's number
        balance.setText(currentUserAccount.getUserBalance() + " USD");
        accountNumber.setText(currentUserAccount.getUserId());

        // show exchange rate
        ExchangeRates exchangeRates = new ExchangeRates();
        if (exchangeRates != null) {
            usdTextField.setText(exchangeRates.getUsdRate() + " руб");
            euroTextField.setText(exchangeRates.getEurRate() + " руб");
            gbpTextField.setText(exchangeRates.getGbpRate() + " руб");
            cadTextField.setText(exchangeRates.getCadRate() + " руб");
        }

        // TODO maybe I need to create stocks classes in Main before user came into account?
        // TODO time out of requests to API - 5 sec
        // show Dow Jones stocks
//        DowJonesHelper dowJones = new DowJonesHelper();
//        if (dowJones != null) {
//            applePriceField.setText(dowJones.getApplePrice() + "$");
//            microsoftPriceField.setText(dowJones.getMicrosoftPrice() + "$");
//            jpmorganPriceField.setText(dowJones.getJpmorganPrice() + "$");
//            cocaColaPriceField.setText(dowJones.getCocaColaPrice() + "$");
//            mcDonaldsPriceField.setText(dowJones.getMcDonaldsPrice() + "$");
//        }

        // go to DB and get user's transactions
        ArrayList<Transaction> userTransactions = SQLHandler.getUserTransactions(currentUserAccount);
        if (userTransactions != null) {
            columnTransactionDate.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));
            columnTransactionSender.setCellValueFactory(new PropertyValueFactory<>("transactionSender"));
            columnTransactionBeneficiary.setCellValueFactory(new PropertyValueFactory<>("transactionBeneficiary"));
            columnTransactionAmount.setCellValueFactory(new PropertyValueFactory<>("transactionAmount"));
            transactions.addAll(userTransactions);
            tableOfTransactions.setItems(transactions);
        }

        // log out from account
        logOutFromUserMenuButton.setOnAction(event -> {
            logOutFromUserMenuButton.getScene().getWindow().hide();
        });

        // close account
        closeAccountButton.setOnAction(event -> {
            // dialog window with user
            TextInputDialog confirmDeleteDialog = new TextInputDialog();
            confirmDeleteDialog.setHeaderText("Подтвердите удаление аккаунта");
            confirmDeleteDialog.setContentText("Ваш пароль:");
            Optional<String> confirmText = confirmDeleteDialog.showAndWait();
            confirmText.ifPresent(password -> {
                if (password.equals(currentUserAccount.getUserPassword())) {
                    // go to DB & remove current user
                    boolean isUserDeleted = SQLHandler.removeUser(currentUserAccount);
                    if (isUserDeleted) {
                        printAlert(Alert.AlertType.INFORMATION, "Удаление аккаунта", "Ваш аккаунт удалён");
                        closeAccountButton.getScene().getWindow().hide();
                    } else {
                        printAlert(Alert.AlertType.ERROR, "Удаление аккаунта", "Неверный пароль");
                    }
                }
            });
        });

        // show all user's info
        showAccountInfoButton.setOnAction(event -> {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Сведения о счёте");
            info.setHeaderText("Реквизиты счёта");
            info.setResizable(true);
            info.setContentText(currentUserAccount.toString());
            info.showAndWait();
        });

        // add money
        addMoneyButton.setOnAction(event -> {
            TextInputDialog addMoneyDialog = new TextInputDialog();
            addMoneyDialog.setHeaderText("Пополнить счёт?");
            addMoneyDialog.setContentText("Сумма пополнения:");
            Optional<String> confirmText = addMoneyDialog.showAndWait();
            confirmText.ifPresent(sum -> {
                int sumForAdding = 0;
                try {
                    sumForAdding = Integer.parseInt(sum);
                } catch (Exception ex) {
                    printAlert(Alert.AlertType.ERROR, null, "Введите сумму цифрами");
                    return;
                }
                boolean isAddingMoneyComplete = SQLHandler.increaseUserBalance(sumForAdding, currentUserAccount);
                if (!isAddingMoneyComplete) {
                    printAlert(Alert.AlertType.ERROR, "Ошибка", "Что-то пошло не так...\nпопробуте снова");
                    return;
                }
                currentUserAccount.setUserBalance(SQLHandler.getBalanceByUser(currentUserAccount));
                balance.setText(currentUserAccount.getUserBalance() + " USD");
                printAlert(Alert.AlertType.INFORMATION, "Пополнение счёта", "Ваш счёт успешно пополнен на сумму " + sumForAdding + " USD");
            });
        });

        // transfer money
        doTransferButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/transfer-form.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Перевод средств");
            stage.setScene(new Scene(root, 500, 600));
            stage.showAndWait();
        });

        // give a donation
        giveDonationButton.setOnAction(event -> {
            TextInputDialog donationDialog = new TextInputDialog();
            donationDialog.setHeaderText("Вы хотите пожертвовать деньги?");
            donationDialog.setContentText("Сумма пожертвования:");
            Optional<String> confirmText = donationDialog.showAndWait();
            confirmText.ifPresent(amountDonat -> {
                // go to DB check balance & subtract donat's amount & update balance
                int donat = 0;
                try {
                    donat = Integer.parseInt(amountDonat);
                } catch (Exception ex) {
                    printAlert(Alert.AlertType.ERROR, null, "Введите сумму цифрами");
                    return;
                }
                boolean isDonationComplete = SQLHandler.decreaseUserBalance(donat, currentUserAccount);
                if (!isDonationComplete) {
                    printAlert(Alert.AlertType.ERROR, "Ошибка", "У Вас недостаточно средств на счету\nили что-то пошло не так...");
                    return;
                }
                // update balance in currentUserAccount
                currentUserAccount.setUserBalance(SQLHandler.getBalanceByUser(currentUserAccount));
                balance.setText(currentUserAccount.getUserBalance() + " USD");
                printAlert(Alert.AlertType.INFORMATION, "Пожертвование", "Спасибо\nВаш донат отправлен");
            });
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
