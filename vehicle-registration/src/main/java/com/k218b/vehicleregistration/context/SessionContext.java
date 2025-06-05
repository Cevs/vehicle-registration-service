package com.k218b.vehicleregistration.context;


import com.k218b.vehicleregistration.model.User;

/**
 * Holds the currently authenticated {@link User} in a ThreadLocal, so that
 * downstream code can call {@link #getCurrentUser()} without needing an
 * HttpExchange or explicit accountId parameter.
 *
 * <p>
 *   Usage pattern:
 *   <ol>
 *     <li>At the start of request‐processing (e.g. in a filter), call
 *         {@code SessionContext.setCurrentUser(user);}.</li>
 *     <li>Anywhere else on the same thread (handler, service, DAO), call
 *         {@link #getCurrentUser()} to retrieve that user.</li>
 *     <li>When the request is finished, call {@link #clear()} to remove the
 *         User from the ThreadLocal.</li>
 *   </ol>
 * </p>
 *
 * <p>
 *   Be sure to call {@link #clear()} in a final‐block (or filter finally)
 *   so that threads returned to the pool don’t retain old user data.
 * </p>
 */
public final class SessionContext {
	private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

	private SessionContext() {
		// Utility class; prevent instantiation
	}

	/**
	 * Stores the given User in the ThreadLocal for the current thread.
	 *
	 * @param user the authenticated User to be associated with this request
	 */
	public static void setCurrentUser(User user) {
		currentUser.set(user);
	}

	/**
	 * Retrieves the User stored for this thread, or null if none has been set.
	 *
	 * @return the currently authenticated User, or null if not authenticated
	 */
	public static User getCurrentUser() {
		return currentUser.get();
	}

	/**
	 * Clears the ThreadLocal so that no User remains bound to this thread.
	 * <p>
	 * Must be called after request processing completes (e.g. in a filter finally
	 * block) to avoid leaking references when threads are re‐pooled.
	 * </p>
	 */
	public static void clear() {
		currentUser.remove();
	}
}