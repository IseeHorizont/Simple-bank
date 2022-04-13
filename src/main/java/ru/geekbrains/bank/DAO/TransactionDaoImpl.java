package ru.geekbrains.bank.DAO;

import ru.geekbrains.bank.DAO.TransactionDao;
import ru.geekbrains.bank.models.Transaction;
import ru.geekbrains.bank.models.UserAccount;

import java.sql.*;
import java.util.ArrayList;

public class TransactionDaoImpl implements TransactionDao {

    private static Connection connection;

    @Override
    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:src/bank.db");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public ArrayList<Transaction> getUserTransactions(UserAccount userAccount) {

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

    @Override
    public boolean transferBetweenUsersAndWriteTransaction(String date, String senderId, String beneficiaryId, int sum) {
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
