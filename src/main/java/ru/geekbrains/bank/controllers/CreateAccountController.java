package ru.geekbrains.bank.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import ru.geekbrains.bank.DAO.UserAccountDaoImpl;
import ru.geekbrains.bank.models.UserAccount;

import java.util.Calendar;

public class CreateAccountController {

    private UserAccountDaoImpl userAccountDao;

    @FXML
    private TextField fullNameNewUser;
    @FXML
    private TextField emailNewUser;
    @FXML
    private TextField dateOfBirthNewUser;
    @FXML
    private TextField placeOfBirthNewUser;


    // method which get data from UI, create a new account, go to DB & write new user
    @FXML
    public void createNewAccount() {
        userAccountDao = new UserAccountDaoImpl();
        // checking data which we got from user
        String userName = fullNameNewUser.getText().trim();
        String userEmail = emailNewUser.getText().trim();
        String userDateOfBirth = dateOfBirthNewUser.getText().trim();
        String userPlaceOfBirth = placeOfBirthNewUser.getText().trim();

        if (userName == null || userName.isEmpty()) {
            printAlert(Alert.AlertType.ERROR, "Ошибка ввода ФИО", "Введите свои фамилию, имя, отчество");
            return;
        }
        if (!isCorrectEmail(userEmail)) {
            printAlert(Alert.AlertType.ERROR,"Ошибка ввода адреса эл.почты", "Введите корректно адрес эл.почты в формате: info@example.com");
            return;
        }
        if (!isCorrectDateOfBirth(userDateOfBirth)) {
            printAlert(Alert.AlertType.ERROR,"Ошибка ввода даты рождения", "Введите дату рождения цифрами в формате: 01.01.2000");
            return;
        }
        if (userPlaceOfBirth == null || userPlaceOfBirth.isEmpty()) {
            printAlert(Alert.AlertType.ERROR,"Ошибка ввода места рождения", "Введите место рождения");
            return;
        }
        // create new object 'user'
        // name, dateOfBirth, placeOfBirth, email
        UserAccount newUser = new UserAccount(userName, userDateOfBirth, userPlaceOfBirth, userEmail);
        // go to DB and write new user
        if(userAccountDao.insertNewUserInDB(newUser)) {
            printAlert(Alert.AlertType.INFORMATION, "", "Успешная регистрация" + "\nВаш пароль для входа: " + newUser.getUserPassword());
            // after successfully registration need to close registration's window
            fullNameNewUser.getScene().getWindow().hide();
        } else {
            printAlert(Alert.AlertType.ERROR, "Ошибка регистрации", "При регистрации что-то пошло не так...");
        }
    }

    private boolean isCorrectEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String[] emailAfterSplit = email.split("@");
        if (emailAfterSplit.length != 2) {
            return false;
        }
        return true;
    }

    private boolean isCorrectDateOfBirth(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            return false;
        }
        // format like '01.01.2000'
        String dateOfBirthPattern = "^\\d{2}.\\d{2}.\\d{4}$";
        if (!dateOfBirth.matches(dateOfBirthPattern)) {
            return false;
        }
        // checking day && month && year
        String[] dateAfterSplit = dateOfBirth.split("\\.");
        if (dateAfterSplit.length != 3) {
            System.out.println(dateAfterSplit.length);
            return false;
        }
        return checkCorrectInputData(dateAfterSplit[0], dateAfterSplit[1], dateAfterSplit[2]);
    }

    private static void printAlert(Alert.AlertType type, String title, String messageToUser){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(messageToUser);
        alert.showAndWait();
    }

    public static boolean checkCorrectInputData(String day, String month, String year){
        int currentDay = 0;
        int currentMonth = 0;
        int checkYear = 0;
        try {
            currentDay = Integer.parseInt(day);
            currentMonth = Integer.parseInt(month);
            checkYear = Integer.parseInt(year);
        }catch(Exception ex){
            return false;
        }
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if(checkYear > currentYear){
            return false;
        }
        if(isYearLeap(currentMonth)){
            if(currentMonth == 2 && currentDay > 29){
                return false;
            }
        }
        if(currentMonth < 1 || currentMonth > 12){
            return false;
        }
        checkHowMuchDaysInMonth(currentDay, currentMonth);
        return true;
    }

    private static boolean isYearLeap(int year) {
        if (year % 100 == 0 && year % 400 == 0) {
            return true;
        } else if (year % 4 == 0 && year % 100 > 0){
            return true;
        } else if (year % 100 == 0) {
            return false;
        } else
            return false;
    }
    
    private static boolean checkHowMuchDaysInMonth(int day, int month){
        if(day < 1 || day > 31){
            return false;
        }
        if(month == 4 || month == 6 || month == 9 || month == 11){
            if(day > 30){
                return false;
            }
        }else{
            if(day > 31){
                return false;
            }
        }
        return true;
    }
}
