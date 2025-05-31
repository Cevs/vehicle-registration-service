package com.k218b.vehicleregistration.dao;

import com.k218b.vehicleregistration.model.User;
import com.k218b.vehicleregistration.model.VehicleRegistration;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Data Access Object interface for Vehicle entities.
 * <p>
 * Defines methods to search for vehicles by their registration code
 * in the underlying data store. Implementations should handle
 * retrieval logic (e.g., querying an embedded HSQLDB table) and return
 * an {@link Optional} containing the {@link VehicleRegistration} if found.
 * </p>
 */
public interface VehicleRegistrationDao {

	/**
	 * Searches for a vehicle record by its unique registration code.
	 *
	 * @param registrationCode the unique vehicle registration identifier
	 * @return an {@link Optional} containing the {@link VehicleRegistration} if a matching
	 *         entry is found, or {@link Optional#empty()} if no such vehicle exists
	 * @throws RuntimeException if an unexpected data access error occurs
	 */
	Optional<VehicleRegistration> findByRegistrationCode(String registrationCode);

	/**
	 * Persists a new vehicle registration for the specified user.
	 * <p>
	 * Creates a record linking the given registration code and expiration date
	 * to the provided {@link User}. If a registration with the same code already
	 * exists or if saving fails for any other reason, this method returns {@code false}.
	 * Otherwise, it returns {@code true} to indicate that the registration was
	 * successfully saved.
	 * </p>
	 *
	 * @param registrationCode the unique code identifying the vehicle (e.g., license plate)
	 * @param validUntil       the date until which the vehicle’s registration remains valid
	 * @param user             the {@link User} who is registering this vehicle
	 * @return {@code true} if the vehicle registration was successfully saved;
	 *         {@code false} if a duplicate registration exists or persisting failed
	 */
	boolean saveVehicleRegistration(String registrationCode, LocalDate validUntil, User user);

}