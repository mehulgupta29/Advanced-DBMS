package edu.stevens.cs562.queryprocessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {

	private static Connection db;
	private static final String user = "postgres";
	private static final String password = "p@ssw0rd";
	private static final String url = "jdbc:postgresql://localhost:5432/cs562";

	/**
	 * function to load database drivers
	 */
	public static void loadDBDriver() {
		try {
			Class.forName("org.postgresql.Driver");
			System.out.println("Success loading Driver!");
		} catch (Exception exception) {
			System.out.println("Fail loading Driver!");
			exception.printStackTrace();
		}
	}

	/**
	 * function to connect to the database
	 * @return
	 */
	public static Connection getDBInstance() {
		if (db == null)
			try {
				loadDBDriver();
				db = DriverManager.getConnection(url, user, password);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return db;
	}

}
