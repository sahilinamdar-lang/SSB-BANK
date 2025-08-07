package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/BankDB";
	private static final String USER = "root";
	private static final String PASSWORD = "Sahil2410";

	public static Connection getConnection() throws ClassNotFoundException, SQLException {
	
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}
}