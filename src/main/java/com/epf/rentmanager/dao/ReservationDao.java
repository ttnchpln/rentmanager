package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.persistence.ConnectionManager;

public class ReservationDao {

	private static ReservationDao instance = null;
	private ReservationDao() {}
	public static ReservationDao getInstance() {
		if(instance == null) {
			instance = new ReservationDao();
		}
		return instance;
	}
	
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
	private static final String FIND_RESERVATIONS_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation;";
	private static final String COUNT_RESERVATIONS = "SELECT COUNT(*) AS total FROM Reservation";
		
	public long create(Reservation reservation) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(CREATE_RESERVATION_QUERY);
			// ong client_id, long vehicle_id, LocalDate debut, LocalDate fin

			statement.setString(1, String.valueOf(reservation.getClient_id()));
			statement.setString(2, String.valueOf(reservation.getVehicle_id()));
			statement.setString(3, reservation.getDebut().toString());
			statement.setString(4, reservation.getFin().toString());

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}
	
	public long delete(Reservation reservation) throws DaoException {
		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(DELETE_RESERVATION_QUERY);
			statement.setLong(1, reservation.getId());

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}

	public List<Reservation> findResaByClientId(long clientId) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);

			statement.setLong(1, clientId);

			ResultSet rs = statement.executeQuery();

			List<Reservation> reservations = new ArrayList<>();

			while (rs.next()) {

				reservations.add(new Reservation(rs.getLong("id"),
						rs.getLong("client_id"),
						rs.getLong("vehicle_id"),
						rs.getDate("debut").toLocalDate(),
						rs.getDate("fin").toLocalDate()));
			}
			return reservations;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Reservation> findResaByVehicleId(long vehicleId) throws DaoException {
		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(FIND_RESERVATIONS_BY_VEHICLE_QUERY);

			statement.setLong(1, vehicleId);

			ResultSet rs = statement.executeQuery();

			List<Reservation> reservations = new ArrayList<>();

			while (rs.next()) {

				reservations.add(new Reservation(rs.getLong("id"),
						rs.getLong("client_id"),
						rs.getLong("vehicle_id"),
						rs.getDate("debut").toLocalDate(),
						rs.getDate("fin").toLocalDate()));
			}
			return reservations;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Reservation> findAll() throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();

			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(FIND_RESERVATIONS_QUERY);

			List<Reservation> reservations = new ArrayList<>();

			while (rs.next()) {

				reservations.add(new Reservation(rs.getLong("id"),
						rs.getLong("client_id"),
						rs.getLong("vehicle_id"),
						rs.getDate("debut").toLocalDate(),
						rs.getDate("fin").toLocalDate()));
			}
			return reservations;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public int count() throws DaoException {

		int count = 0;

		try {
			Connection connexion = ConnectionManager.getConnection();

			Statement statement = connexion.createStatement();

			ResultSet rs = statement.executeQuery(COUNT_RESERVATIONS);

			while(rs.next()) {
				count = rs.getInt("total");
			}

			return count;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}
}
