package ru.geekbrains.bank.models;

import java.util.Objects;
import java.util.Random;

public class UserAccount {

    private final String userId;
    private final String userName;
    private final String userPassword;
    private final String userDateOfBirth;
    private final String userPlaceOfBirth;
    private String userEmail;
    private int userBalance;

    public UserAccount(String userName, String userDateOfBirth, String userPlaceOfBirth, String userEmail) {
        this.userId = generateIdNumber();
        this.userName = userName;
        this.userPassword = generatePassword();
        this.userDateOfBirth = userDateOfBirth;
        this.userPlaceOfBirth = userPlaceOfBirth;
        this.userEmail = userEmail;
        this.userBalance = 0;
    }

    public UserAccount(String userId, String userName, String userPassword, String userDateOfBirth, String userPlaceOfBirth, String userEmail, int userBalance) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userDateOfBirth = userDateOfBirth;
        this.userPlaceOfBirth = userPlaceOfBirth;
        this.userEmail = userEmail;
        this.userBalance = userBalance;
    }

    // generate ID like some bank generates a bank's card number
    private String generateIdNumber() {
        Random random = new Random();
        StringBuilder idNumberBuilder = new StringBuilder();
        String iin = "400000";
        idNumberBuilder.append(iin);
        for (int i = 0; i < 9; i++) {
            int next = random.nextInt(10);
            idNumberBuilder.append(next);
        }
        int checksum = findChecksum(idNumberBuilder.toString());
        idNumberBuilder.append(checksum);
        return idNumberBuilder.toString();
    }

    private int findChecksum(String cardNumber) {
        char[] chars = cardNumber.toCharArray();
        int sum = 0;
        for (int i = 1; i <= chars.length; i++) {
            int num = Character.getNumericValue(chars[i - 1]);
            if (i % 2 == 1) {
                num *= 2;
            }
            if (num > 9) {
                num -= 9;
            }
            sum += num;
        }
        return (10 - sum % 10) % 10;
    }

    // generate PASSWORD which has only four digit
    private String generatePassword() {
        Random random = new Random();
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            passwordBuilder.append(random.nextInt(10));
        }
        return passwordBuilder.toString();
    }

    public String getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserDateOfBirth() {
        return userDateOfBirth;
    }

    public String getUserPlaceOfBirth() {
        return userPlaceOfBirth;
    }

    public int getUserBalance() {
        return userBalance;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserBalance(int userBalance) {
        this.userBalance = userBalance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAccount that = (UserAccount) o;
        return userId.equals(that.userId) && userName.equals(that.userName)
                && userPassword.equals(that.userPassword) && userDateOfBirth.equals(that.userDateOfBirth)
                && userPlaceOfBirth.equals(that.userPlaceOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, userName, userPassword, userDateOfBirth, userPlaceOfBirth);
    }

    @Override
    public String toString() {
        return "Simple Bank \n" +
                "№ счёта: \t\t" + userId + "\n" +
                "ФИО: \t\t" + userName + "\n" +
                "Дата рождения: \t\t" + userDateOfBirth + "\n" +
                "Место рождения: \t\t" + userPlaceOfBirth + "\n" +
                "Эл. почта: \t\t" + userEmail + "\n" +
                "Остаток по счёту: \t\t" + userBalance + " USD" + "\n";
    }
}
