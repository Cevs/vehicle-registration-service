package com.k218b.vehicleregistration.service.impl;

import com.k218b.vehicleregistration.dao.UserDao;
import com.k218b.vehicleregistration.exception.DuplicatedAccountException;
import com.k218b.vehicleregistration.model.User;
import com.k218b.vehicleregistration.service.UserService;
import com.k218b.vehicleregistration.util.CryptoUtil;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultUserService implements UserService {

	private static final Logger LOG = Logger.getLogger(DefaultUserService.class.getName());

	private final UserDao userDao;

	public DefaultUserService(final UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public String openAccount(final String accountId) throws DuplicatedAccountException {
		final Optional<User> userOptional = userDao.findByAccountId(accountId);
		if (userOptional.isPresent()) {
			final String errMsg = "Account for given uid: %s already exists!".formatted(accountId);
			LOG.log(Level.WARNING, errMsg);
			throw new DuplicatedAccountException(errMsg);
		}

		final String randomPassword = CryptoUtil.generateRandomPassword();
		final String salt = CryptoUtil.generateSalt();
		final String hash = CryptoUtil.hashPassword(randomPassword, salt);
		userDao.saveUser(accountId, hash, salt);
		return randomPassword;
	}

	@Override
	public Optional<User> getUser(final String accountId) {
		return userDao.findByAccountId(accountId);
	}

}
