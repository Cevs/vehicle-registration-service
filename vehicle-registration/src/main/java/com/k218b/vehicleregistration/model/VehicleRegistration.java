package com.k218b.vehicleregistration.model;

import java.time.LocalDate;

public record VehicleRegistration(String registrationCode, LocalDate validUntil, String accountId) {}
