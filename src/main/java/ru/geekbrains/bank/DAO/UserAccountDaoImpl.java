package ru.geekbrains.bank.DAO;

import ru.geekbrains.bank.DAO.UserAccountDao;
import ru.geekbrains.bank.models.UserAccount;

import java.sql.*;

public class UserAccountDaoImpl implements UserAccountDao {

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
    public boolean insertNewUserInDB(UserAccount newUser) {
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

    @Override
    public UserAccount isAuthorize(String currentUserName, String currentUserPassword) {
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

    @Override
    public boolean removeUser(UserAccount userAccount) {
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

    @Override
    public boolean decreaseUserBalance(int donat, UserAccount userAccount) {
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

    @Override
    public int getBalanceByUser(UserAccount currentUserAccount) {
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

    @Override
    public boolean increaseUserBalance(int sumForAdding, UserAccount currentUserAccount) {
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

    @Override
    public boolean isUserIdContainsInDB(String beneficiaryId) {
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
}
