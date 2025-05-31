package com.k218b.vehicleregistration.service.impl;

import com.k218b.vehicleregistration.context.SessionContext;
import com.k218b.vehicleregistration.dao.VehicleRegistrationDao;
import com.k218b.vehicleregistration.model.User;
import com.k218b.vehicleregistration.model.VehicleRegistration;
import com.k218b.vehicleregistration.service.VehicleRegistrationService;
import java.time.LocalDate;
import java.util.Optional;

public class DefaultVehicleRegistrationService implements VehicleRegistrationService {

	private final VehicleRegistrationDao vehicleDao;

	public DefaultVehicleRegistrationService(final VehicleRegistrationDao vehicleDao) {
		this.vehicleDao = vehicleDao;
	}

	@Override
	public boolean addVehicleRegistration(final String registrationCode, final LocalDate validUntil) {
		final User user = SessionContext.getCurrentUser();
		return vehicleDao.saveVehicleRegistration(registrationCode, validUntil, user);
	}

	@Override
	public Optional<VehicleRegistration> getVehicleRegistration(final String registrationCode) {
		return vehicleDao.findByRegistrationCode(registrationCode);
	}

}
