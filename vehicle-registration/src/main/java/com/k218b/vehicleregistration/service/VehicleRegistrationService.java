package com.k218b.vehicleregistration.service;

import com.k218b.vehicleregistration.model.VehicleRegistration;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

/**
 * Service interface for managing vehicle registrations.
 * <p>
 * Provides methods to add a new vehicle registration and to retrieve
 * an existing vehicle registration by its registration code.
 * </p>
 */
public interface VehicleRegistrationService {

	/**
	 * Adds a new vehicle registration for the currently authenticated user.
	 * <p>
	 * If the registration code is already in use or if there is no authenticated
	 * user context, this method returns {@code false} and does not persist
	 * any data. Otherwise, a new {@link VehicleRegistration} is created with
	 * the provided registration code and expiration date.
	 * </p>
	 *
	 * @param registrationCode the unique code identifying the vehicle (e.g., license plate)
	 * @param validUntil       the date until which the vehicle’s registration remains valid
	 * @return {@code true} if the registration was successfully created,
	 *         or {@code false} if it could not be created (e.g., duplicate code
	 *         or lack of authentication)
	 */
	boolean addVehicleRegistration(String registrationCode, LocalDate validUntil);

	/**
	 * Retrieves a vehicle registration by its unique registration code.
	 * <p>
	 * Looks up the {@link VehicleRegistration} instance corresponding to the given
	 * registration code. If a registration exists and the current user is authorized
	 * to access it, the registration is returned; otherwise, an empty {@link Optional}
	 * is returned.
	 * </p>
	 *
	 * @param registrationCode the unique vehicle registration code to search for
	 * @return an {@link Optional} containing the matching {@link VehicleRegistration}
	 *         if found and accessible by the user, or {@code Optional.empty()} otherwise
	 */
	Optional<VehicleRegistration> getVehicleRegistration(String registrationCode);

	/**
	 * Retrieves the count of vehicle registrations grouped by account ID.
	 * <p>
	 * Returns a map where each key is an account ID (String) and each value is
	 * the number of vehicle registrations associated with that account. Accounts
	 * with zero registrations will not appear in the returned map.
	 * </p>
	 *
	 * @return a {@code Map<String, Integer>} mapping each account ID to its total
	 *         number of vehicle registrations
	 */
	Map<String, Integer> getVehicleRegistrationPerUser();
}
