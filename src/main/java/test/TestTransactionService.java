package test;

import service.AccountService;
import service.TransactionService;

import java.util.List;

import model.Transaction;
import model.User;  // optional if needed for userId

public class TestTransactionService {

    public static void main(String[] args) {
        AccountService accountService = new AccountService();
        TransactionService service = new TransactionService();

        try {
            int userId1 = 2;  // existing user/account in DB
            int userId2 = 7;

            // Test deposit
//            boolean depositResult = accountService.deposit(userId1, 500.0, "Test deposit");
//            System.out.println("Deposit result: " + depositResult);

            // Test withdraw
//            boolean withdrawResult = accountService.withdraw(userId1, 200.0, "Test withdraw");
//            System.out.println("Withdraw result: " + withdrawResult);

//            // Test transfer (you may need to add transfer method to AccountService)
//            boolean transferResult = accountService.transfer(userId1, userId2, 100.0, "Test transfer");
//            System.out.println("Transfer result: " + transferResult);
            
            List<Transaction> transactionResult= service.getTransactionsByAccountId(userId1);
             for (Transaction transaction : transactionResult) {
				System.out.println(transaction);
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
