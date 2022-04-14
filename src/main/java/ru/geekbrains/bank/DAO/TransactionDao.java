package ru.geekbrains.bank.DAO;

import ru.geekbrains.bank.models.Transaction;
import ru.geekbrains.bank.models.UserAccount;

import java.util.ArrayList;

public interface TransactionDao {
    public void connect();
    public ArrayList<Transaction> getUserTransactions(UserAccount userAccount);
    public boolean transferBetweenUsersAndWriteTransaction(String senderId, String beneficiaryId, int sum);
    public boolean transactionByMyself(String senderId, int sum);
}
