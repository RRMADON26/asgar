package connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {
	public static Connection toDb() {
		Connection connection = null;

		try {
			Class.forName("org.sqlite.JDBC");

			connection = DriverManager.getConnection("jdbc:sqlite:asgar.db");
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}


		return connection;
	}
}
