package com.k218b.vehicleregistration.util;

import com.k218b.vehicleregistration.config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility for initializing and connecting to an embedded HSQLDB database.
 * <p>
 * Uses the built-in HSQLDB engine (org.hsqldb) with an in-memory or file-based
 * mode. No external dependencies besides the HSQLDB JAR.
 * </p>
 */
public class JDBCUtil {

	private static final String URL = AppConfig.get("db.url");
	private static final String USER = AppConfig.getOrDefault("db.user", "SA");
	private static final String PASSWORD = AppConfig.getOrDefault("db.password", "");

	private JDBCUtil() {}

	/**
	 * Get a new Connection to the HSQLDB instance.
	 *
	 * @return a Connection object
	 * @throws SQLException if a database access error occurs
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	/**
	 * Initializes the database schema if not already present.
	 * Creates 'accounts' and 'registrations' tables.
	 * Ignores "already exists" errors.
	 */
	public static void initDatabase() {
		String ddl = "CREATE TABLE accounts ("
					 + " account_id VARCHAR(100) PRIMARY KEY,"
					 + " password_hash VARCHAR(512) NOT NULL,"
					 + " salt VARCHAR(64) NOT NULL"
					 + ")";
		try (Connection conn = getConnection();
			 Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(ddl);
		} catch (SQLException e) {
			// ignore "table already exists" errors: SQLState 42Y55 or error code -5501
			String state = e.getSQLState();
			int code = e.getErrorCode();
			if (!"42Y55".equals(state) && code != -5501) {
				throw new RuntimeException("DB init failed", e);
			}
		}
	}
}
