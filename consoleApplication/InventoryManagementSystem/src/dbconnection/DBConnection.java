package dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {
	static Connection dbConnection = null;

	static {
		try {
			dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventorymanagement", "root",
					"Sound!!123.");
		} catch (SQLException e) {
			System.out.print(e);
		}
	}

	public static Connection getDBConnection() throws SQLException {
		return dbConnection;
	}
}
