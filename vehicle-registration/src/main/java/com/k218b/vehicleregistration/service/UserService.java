package com.k218b.vehicleregistration.service;

import com.k218b.vehicleregistration.exception.DuplicatedAccountException;
import com.k218b.vehicleregistration.model.User;

public interface UserService {

	/**
	 * Opens a new account for the specified account ID.
	 * <p>
	 * If an account with the given ID already exists, a {@link DuplicatedAccountException}
	 * is thrown.
	 *
	 * @param accountId the unique identifier for the account to open
	 * @return the newly created {@link User}
	 * @throws DuplicatedAccountException if an account with that ID already exists
	 */
	User openAccount(String accountId) throws DuplicatedAccountException;
}
