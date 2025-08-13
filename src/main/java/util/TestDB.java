package util;

import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            System.out.println("✅ DB Connected Successfully");
        } catch (Exception e) {
            System.out.println("❌ DB Connection Failed");
            e.printStackTrace();
        }
    }
}

