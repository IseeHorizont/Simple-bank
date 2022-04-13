package ru.geekbrains.bank.models;

public class Transaction {
    private String transactionDate;
    private String transactionSender;
    private String transactionBeneficiary;
    private String transactionAmount;

    public Transaction(String transactionDate, String transactionSender, String transactionBeneficiary, String transactionAmount) {
        this.transactionDate = transactionDate;
        this.transactionSender = transactionSender;
        this.transactionBeneficiary = transactionBeneficiary;
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionSender() {
        return transactionSender;
    }

    public void setTransactionSender(String transactionSender) {
        this.transactionSender = transactionSender;
    }

    public String getTransactionBeneficiary() {
        return transactionBeneficiary;
    }

    public void setTransactionBeneficiary(String transactionBeneficiary) {
        this.transactionBeneficiary = transactionBeneficiary;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }
}
