package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Transaction {
    
    private int transactionId;
    private int accountId;
    private String transactionType; // DEPOSIT, WITHDRAW, TRANSFER, BILL_PAYMENT, etc.
    private double amount;
    private Timestamp transactionDate;
    private String description;
    private double balanceAfterTransaction; // for mini statement display

    // Constructors
    public Transaction() {
        super();
    }

    public Transaction(int transactionId, int accountId, String transactionType, double amount,
                       Timestamp transactionDate, String description, double balanceAfterTransaction) {
        super();
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        this.description = description;
        this.balanceAfterTransaction = balanceAfterTransaction;
    }

	public int getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}

	public int getAccountId() {
		return accountId;
	}

	public void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getBalanceAfterTransaction() {
		return balanceAfterTransaction;
	}

	public void setBalanceAfterTransaction(double balanceAfterTransaction) {
		this.balanceAfterTransaction = balanceAfterTransaction;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + ", accountId=" + accountId + ", transactionType="
				+ transactionType + ", amount=" + amount + ", transactionDate=" + transactionDate + ", description="
				+ description + ", balanceAfterTransaction=" + balanceAfterTransaction + "]";
	}

} // Getters and Setters
    