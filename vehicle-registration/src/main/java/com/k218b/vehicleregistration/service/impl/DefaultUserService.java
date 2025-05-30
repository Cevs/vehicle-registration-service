package com.k218b.vehicleregistration.service.impl;

import com.k218b.vehicleregistration.exception.DuplicatedAccountException;
import com.k218b.vehicleregistration.model.User;
import com.k218b.vehicleregistration.service.UserService;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultUserService implements UserService {

	private static final Map<String, User> STORE = new ConcurrentHashMap<>();
	private static final Logger LOG = Logger.getLogger(DefaultUserService.class.getName());

	@Override
	public User openAccount(final String accountId) throws DuplicatedAccountException{
		if (STORE.containsKey(accountId)) {
			final String errMsg = "Account for given uid: %s already exists!".formatted(accountId);
			LOG.log(Level.WARNING, errMsg);
			throw new DuplicatedAccountException(errMsg);
		}

		User user = createUser(accountId);
		STORE.put(accountId, user);

		return user;
	}

	private User createUser(String accountId) {
		return new User(accountId, createPassword());
	}

	private String createPassword() {
		return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
	}

}
