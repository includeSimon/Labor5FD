package Controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RegistrationSystem {
    private final Connection connection;

    public RegistrationSystem() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lab5", "root", "");
    }
}
