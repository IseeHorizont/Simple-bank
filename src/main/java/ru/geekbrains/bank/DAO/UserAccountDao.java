package ru.geekbrains.bank.DAO;

import ru.geekbrains.bank.models.UserAccount;

public interface UserAccountDao {
    public void connect();
    public boolean insertNewUserInDB(UserAccount newUser);
    public UserAccount isAuthorize(String currentUserName, String currentUserPassword);
    public boolean removeUser(UserAccount userAccount);
    public boolean decreaseUserBalance(int donat, UserAccount userAccount);
    public int getBalanceByUser(UserAccount currentUserAccount);
    public boolean increaseUserBalance(int sumForAdding, UserAccount currentUserAccount);
    public boolean isUserIdContainsInDB(String beneficiaryId);
}
