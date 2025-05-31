package com.k218b.vehicleregistration.dao.impl;

import com.k218b.vehicleregistration.dao.UserDao;
import com.k218b.vehicleregistration.exception.UserCreationException;
import com.k218b.vehicleregistration.exception.UserSearchException;
import com.k218b.vehicleregistration.model.User;
import com.k218b.vehicleregistration.util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Data Access Object for User entities.
 * <p>
 * Provides methods to retrieve User records from the database
 * by account ID.
 * </p>
 */
public class DefaultUserDao implements UserDao {

	private static final String SELECT_BY_ID =
			"SELECT account_id, password_hash, salt FROM accounts WHERE account_id = ?";
	private static final String INSERT_ACCOUNT =
			"INSERT INTO accounts(account_id, password_hash, salt) VALUES(?,?,?)";

	@Override
	public Optional<User> findByAccountId(String accountId) throws UserSearchException {
		try (Connection conn = JDBCUtil.getConnection();
			 PreparedStatement ps = conn.prepareStatement(SELECT_BY_ID)) {

			ps.setString(1, accountId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					User user = new User(
							rs.getString(1),
							rs.getString(2),
							rs.getString(3)
					);

					return Optional.of(user);
				}
				return Optional.empty();
			}
		} catch (SQLException e) {
			throw new UserSearchException("Failed to retrieve user for accountId=%s".formatted(accountId), e);
		}
	}

	/**
	 * Persists a new account with its hashed password and salt.
	 *
	 * @param accountId    the unique identifier of the account
	 * @param passwordHash the Base64-encoded password hash
	 * @param salt         the Base64-encoded salt
	 * @throws UserCreationException if an error when error persisting user occurs
	 */
	@Override
	public void saveUser(String accountId, String passwordHash, String salt) throws UserCreationException{
		try (Connection conn = JDBCUtil.getConnection();
			 PreparedStatement ps = conn.prepareStatement(INSERT_ACCOUNT)) {

			ps.setString(1, accountId);
			ps.setString(2, passwordHash);
			ps.setString(3, salt);
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new UserCreationException("Failed to save credentials for accountId=%s".formatted(accountId), e);
		}
	}
}
