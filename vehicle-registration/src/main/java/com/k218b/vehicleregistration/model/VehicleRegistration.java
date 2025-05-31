package com.k218b.vehicleregistration.model;

import java.util.Date;

public record VehicleRegistration(String registrationCode, Date validUntil, String accountId) {}
