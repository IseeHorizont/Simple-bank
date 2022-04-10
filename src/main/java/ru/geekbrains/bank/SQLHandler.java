package ru.geekbrains.bank;

import java.sql.*;
import java.util.ArrayList;

public class SQLHandler {
    private static Connection connection;


    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:src/bank.db");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }


    // add new user account in DB
    public static boolean insertNewUserInDB(UserAccount newUser) {
        String insertUserQuery = "INSERT INTO users (userId, userName, userPassword, userDateOfBirth, " +
                                                    "userPlaceOfBirth, userEmail, userBalance) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        connect();
        try (PreparedStatement statement = connection.prepareStatement(insertUserQuery)) {
            statement.setString(1, newUser.getUserId());
            statement.setString(2, newUser.getUserName());
            statement.setString(3, newUser.getUserPassword());
            statement.setString(4, newUser.getUserDateOfBirth());
            statement.setString(5, newUser.getUserPlaceOfBirth());
            statement.setString(6, newUser.getUserEmail());
            statement.setInt(7, newUser.getUserBalance());
            statement.executeUpdate();
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    // to check that are there name & password in DB
    public static UserAccount isAuthorize(String currentUserName, String currentUserPassword) {
        String selectByNameAndPassword = "SELECT * FROM users WHERE userName=? AND userPassword=?";
        connect();
        UserAccount userAccount = null;
        try (PreparedStatement statement = connection.prepareStatement(selectByNameAndPassword)) {
            statement.setString(1, currentUserName);
            statement.setString(2, currentUserPassword);
            ResultSet resultSet = statement.executeQuery();
            userAccount = new UserAccount(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6),
                    resultSet.getInt(7)
            );
            resultSet.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userAccount;
    }

    // find user's transactions and safe them in ArrayList
    public static ArrayList<Transaction> getUserTransactions(UserAccount userAccount) {
        connect();
        ArrayList<Transaction> userTransactions = new ArrayList<>();
        // get user's transactions from DB
        String selectUserTransactions = "SELECT * FROM transactions WHERE transactionSender=? "
                                                                    + "OR transactionBeneficiary=?";
        connect();
        try (PreparedStatement statement = connection.prepareStatement(selectUserTransactions)) {
            statement.setString(1, userAccount.getUserId());
            statement.setString(2, userAccount.getUserId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                userTransactions.add(new Transaction(
                        resultSet.getString("transactionDate"),
                        resultSet.getString("transactionSender"),
                        resultSet.getString("transactionBeneficiary"),
                        resultSet.getString("transactionAmount")
                ));
            }
            resultSet.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return userTransactions;
    }

    // delete user
    public static boolean removeUser(UserAccount userAccount) {
        String deleteQuery = "DELETE FROM users WHERE userId=?;";
        connect();
        try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
            statement.setString(1, userAccount.getUserId());
            statement.executeUpdate();
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                return false;
            }
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    // decrease user's balance by donat
    public static boolean decreaseUserBalance(int donat, UserAccount userAccount) {
        String selectUserBalanceQuery = "SELECT userBalance FROM users WHERE userId=?;";
        String updateUserBalanceQuery = "UPDATE users SET userBalance=? WHERE userId=?;";

        connect();
        Integer newUserBalance = null;
        try (PreparedStatement statement = connection.prepareStatement(selectUserBalanceQuery)) {
            statement.setString(1, userAccount.getUserId());
            ResultSet resultSet = statement.executeQuery();
            newUserBalance = resultSet.getInt(1) - donat;
            if (newUserBalance < 0) {
                resultSet.close();
                connection.close();
                return false;
            }
            resultSet.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        connect();
        try (PreparedStatement statement = connection.prepareStatement(updateUserBalanceQuery)) {
            // update user balance
            statement.setInt(1, newUserBalance);
            statement.setString(2, userAccount.getUserId());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static int getBalanceByUser(UserAccount currentUserAccount) {
        String selectBalanceByUserQuery = "SELECT userBalance FROM users WHERE userId=?;";
        connect();
        int userBalance = 0;
        try (PreparedStatement statement = connection.prepareStatement(selectBalanceByUserQuery)) {
            statement.setString(1, currentUserAccount.getUserId());
            ResultSet resultSet = statement.executeQuery();
            userBalance = resultSet.getInt(1);
            resultSet.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
        return userBalance;
    }

    public static boolean increaseUserBalance(int sumForAdding, UserAccount currentUserAccount) {
        String increaseUserBalanceQuery = "UPDATE users SET userBalance=userBalance+? WHERE userId=?;";
        connect();
        try (PreparedStatement statement = connection.prepareStatement(increaseUserBalanceQuery)) {
            statement.setInt(1, sumForAdding);
            statement.setString(2, currentUserAccount.getUserId());
            statement.executeUpdate();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isUserIdContainsInDB(String beneficiaryId) {
        String selectUserById = "SELECT userId FROM users WHERE userId=?;";
        connect();
        try (PreparedStatement statement = connection.prepareStatement(selectUserById)) {
            statement.setString(1, beneficiaryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet == null) {
                resultSet.close();
                connection.close();
                return false;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    // TODO do transfer and write transaction in DB
    public static boolean transferBetweenUsersAndWriteTransaction(String date, String senderId, String beneficiaryId, int sum) {
        String decreaseSenderBalanceQuery = "UPDATE users SET userBalance=userBalance-'sum'" + " WHERE userId='" + senderId + "'";
        String increaseBeneficiaryBalanceQuery = "UPDATE users SET userBalance=userBalance+'sum'" + " WHERE userId='" + beneficiaryId + "'";
        String writeTransactionQuery = "INSERT INTO transactions (transactionDate, transactionSender, transactionBeneficiary, transactionAmount) VALUES ('" + date + "', '" + senderId + "', '" + beneficiaryId + "', '" + sum + "')";
        connect();
        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            statement.addBatch(decreaseSenderBalanceQuery);
            statement.addBatch(increaseBeneficiaryBalanceQuery);
            statement.addBatch(writeTransactionQuery);
            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
