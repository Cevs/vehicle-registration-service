package com.k218b.vehicleregistration.dao.impl;

import com.k218b.vehicleregistration.dao.VehicleRegistrationDao;
import com.k218b.vehicleregistration.model.User;
import com.k218b.vehicleregistration.model.VehicleRegistration;
import com.k218b.vehicleregistration.util.JDBCUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DefaultVehicleRegistrationDao implements VehicleRegistrationDao {

	private static final Logger LOG = Logger.getLogger(DefaultVehicleRegistrationDao.class.getName());

	private static final String SELECT_BY_REGISTRATION_CODE =
			"SELECT registration_code, valid_until, account_id FROM vehicle_registrations WHERE registration_code = ?";
	private static final String SELECT_BY_REGISTRATION_CODE_AND_USER =
			"SELECT registration_code, valid_until, account_id FROM vehicle_registrations WHERE registration_code = ? AND account_id = ?";
	private static final String INSERT_VEHICLE_REGISTRATION =
			"INSERT INTO vehicle_registrations(registration_code, valid_until, account_id) VALUES(?,?,?)";
	private static final String COUNT_PER_ACCOUNT_SQL =
			"SELECT account_id, COUNT(*) AS cnt FROM vehicle_registrations GROUP BY account_id";

	@Override
	public Optional<VehicleRegistration> findByRegistrationCode(final String registrationCode) {
		try (final Connection conn = JDBCUtil.getConnection();
			 final PreparedStatement ps = conn.prepareStatement(SELECT_BY_REGISTRATION_CODE)) {

			ps.setString(1, registrationCode);
			try (final ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final VehicleRegistration vehicleRegistration = new VehicleRegistration(
							rs.getString(1),
							rs.getDate(2).toLocalDate(),
							rs.getString(3)
					);

					return Optional.of(vehicleRegistration);
				}
			}
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Issue occurred while retrieving Vehicle Registration for code: %s"
					.formatted(registrationCode), e);
		}
		return Optional.empty();
	}

	@Override
	public boolean saveVehicleRegistration(final String registrationCode, final LocalDate validUntil, final User user) {
		try (final Connection conn = JDBCUtil.getConnection();
			 final PreparedStatement ps = conn.prepareStatement(INSERT_VEHICLE_REGISTRATION)) {
			ps.setString(1, registrationCode);
			ps.setDate(2, Date.valueOf(validUntil));
			ps.setString(3, user.accountId());
			ps.executeUpdate();
			return true;
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Issue with persisting Vehicle Registration: %s ".formatted(registrationCode), e);
			return false;
		}
	}

	@Override
	public Map<String, Integer> countVehicleRegistrationsPerUser() {
		try (final Connection conn = JDBCUtil.getConnection();
			 final PreparedStatement ps = conn.prepareStatement(COUNT_PER_ACCOUNT_SQL);
			 final ResultSet rs = ps.executeQuery()) {

			final Map<String, Integer> result = new HashMap<>();
			while (rs.next()) {
				final String accountId = rs.getString("account_id");
				final int count = rs.getInt("cnt");
				result.put(accountId, count);
			}
			return result;

		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Issue with fetching vehicle registrations numbers per account!", e);
			return Map.of();
		}
	}

	@Override
	public Optional<VehicleRegistration> findByRegistrationCodeAndUser(final String registrationCode, final User user) {
		try (final Connection conn = JDBCUtil.getConnection();
			 final PreparedStatement ps = conn.prepareStatement(SELECT_BY_REGISTRATION_CODE_AND_USER)) {

			ps.setString(1, registrationCode);
			ps.setString(2, user.accountId());
			try (final ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					final VehicleRegistration vehicleRegistration = new VehicleRegistration(
							rs.getString(1),
							rs.getDate(2).toLocalDate(),
							rs.getString(3)
					);

					return Optional.of(vehicleRegistration);
				}
			}
		} catch (SQLException e) {
			LOG.log(Level.SEVERE, "Issue occurred while retrieving Vehicle Registration for code: %s and user: %s"
					.formatted(registrationCode, user.accountId()), e);
		}
		return Optional.empty();
	}

}
