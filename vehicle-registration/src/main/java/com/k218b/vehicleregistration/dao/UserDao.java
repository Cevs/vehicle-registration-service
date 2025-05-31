package com.k218b.vehicleregistration.dao;

import com.k218b.vehicleregistration.dao.impl.DefaultUserDao;
import com.k218b.vehicleregistration.exception.UserCreationException;
import com.k218b.vehicleregistration.exception.UserNotFoundException;
import com.k218b.vehicleregistration.model.User;
import java.util.Optional;

/**
 * Data Access Object interface for <code>User</code> entities.
 * <p>
 * Defines methods to search for and persist user data in the underlying storage.
 * Implementations such as {@link DefaultUserDao} provide concrete behavior.
 * </p>
 */
public interface UserDao {

	/**
	 * Searches for a user by their account ID.
	 *
	 * @param accountId the unique identifier of the user account
	 * @return an {@link Optional} containing the {@link User} if found, or empty if no matching user exists
	 * @throws UserNotFoundException if an error occurs while searching for the user
	 */
	Optional<User> findByAccountId(String accountId) throws UserNotFoundException;

	/**
	 * Persists a new user with the given account ID, password hash, and salt.
	 *
	 * @param accountId    the unique identifier for the new user account
	 * @param passwordHash the encrypted password hash to store
	 * @param salt         the cryptographic salt used for hashing
	 * @throws UserCreationException if the user could not be created (e.g., duplicate account)
	 */
	void saveUser(String accountId, String passwordHash, String salt) throws UserCreationException;
}
