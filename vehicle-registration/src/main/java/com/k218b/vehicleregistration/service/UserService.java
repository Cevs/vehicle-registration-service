package com.k218b.vehicleregistration.service;

import com.k218b.vehicleregistration.exception.DuplicatedAccountException;
import com.k218b.vehicleregistration.model.User;
import java.util.Optional;

public interface UserService {

	/**
	 * Opens a new account for the specified account ID.
	 * <p>
	 * If an account with the given ID already exists, a {@link DuplicatedAccountException}
	 * is thrown.
	 *
	 * @param accountId the unique identifier for the account to open
	 * @return the newly create password for a user
	 * @throws DuplicatedAccountException if an account with that ID already exists
	 */
	String openAccount(String accountId) throws DuplicatedAccountException;

	Optional<User> getUser(String accountId);
}
