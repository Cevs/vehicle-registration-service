package com.k218b.vehicleregistration.factory;


import com.k218b.vehicleregistration.model.User;
import com.k218b.vehicleregistration.response.OpenAccountResponse;
import com.k218b.vehicleregistration.util.I18nUtil;
import com.sun.net.httpserver.HttpExchange;

import java.util.Locale;

public abstract class OpenAccountResponseFactory {
	private static final String SUCCESS_KEY = "account.creation.success.message";
	private static final String DUPLICATE_KEY = "account.creation.duplicate.message";

	private OpenAccountResponseFactory() {}

	public static OpenAccountResponse success(HttpExchange ex, User user) {
		final Locale locale = I18nUtil.resolveLocale(ex);
		final String msg = I18nUtil.getMessage(locale, SUCCESS_KEY, user.accountId());
		return OpenAccountResponse.builder()
								  .success(true)
								  .message(msg)
								  .password(user.password())
								  .build();
	}

	public static OpenAccountResponse duplicate(HttpExchange ex, String accountId) {
		final Locale locale = I18nUtil.resolveLocale(ex);
		final String msg = I18nUtil.getMessage(locale, DUPLICATE_KEY, accountId);
		return OpenAccountResponse.builder()
								  .success(false)
								  .message(msg)
								  .build();
	}
}
