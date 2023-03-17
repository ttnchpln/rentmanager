package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.stereotype.Repository;

@Repository

public class ReservationDao {

	private ClientService clientService;
	private VehicleService vehicleService;
	private ReservationDao(ClientService clientService, VehicleService vehicleService) {
		this.clientService = clientService;
		this.vehicleService = vehicleService;
	}
	private static final String CREATE_RESERVATION_QUERY = "INSERT INTO Reservation(client_id, vehicle_id, debut, fin) VALUES(?, ?, ?, ?);";
	private static final String DELETE_RESERVATION_QUERY = "DELETE FROM Reservation WHERE id=?;";
	private static final String FIND_RESERVATIONS_BY_CLIENT_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation WHERE client_id=?;";
	private static final String FIND_RESERVATIONS_BY_VEHICLE_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation WHERE vehicle_id=?;";
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

	public List<Vehicle> findVehiclesByClientId(long idClient) throws DaoException {
		List<Reservation> rents = this.findResaByClientId(idClient, false);

		List<Vehicle> cars = new ArrayList<>();

		for(Reservation rent : rents) {
			cars.add(rent.getVehicle());
		}

		Set<Vehicle> set = new HashSet<>(cars);
		cars.clear();
		cars.addAll(set);

		return cars;
	}

	public List<Reservation> findResaByClientId(long clientId, boolean getId) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);

			statement.setLong(1, clientId);

			ResultSet rs = statement.executeQuery();

			List<Reservation> reservations = new ArrayList<>();

			if(getId) {
				while (rs.next()) {
					reservations.add(new Reservation(rs.getLong("id"),
							rs.getLong("client_id"),
							rs.getLong("vehicle_id"),
							rs.getDate("debut").toLocalDate(),
							rs.getDate("fin").toLocalDate()));
				}
			} else {
				while (rs.next()) {
					Client client = clientService.findById(clientId);
					Vehicle vehicle = vehicleService.findById(rs.getLong("vehicle_id"));

					reservations.add(new Reservation(rs.getLong("id"),
							client, vehicle,
							rs.getDate("debut").toLocalDate(),
							rs.getDate("fin").toLocalDate()));
				}
			}
			return reservations;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
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

	public List<Reservation> findAll(boolean getId) throws DaoException {
		try {
			Connection connection = ConnectionManager.getConnection();

			Statement statement = connection.createStatement();

			ResultSet rs = statement.executeQuery(FIND_RESERVATIONS_QUERY);

			List<Reservation> reservations = new ArrayList<>();

			if(getId) {
				while (rs.next()) {
					reservations.add(new Reservation(rs.getLong("id"),
							rs.getLong("client_id"),
							rs.getLong("vehicle_id"),
							rs.getDate("debut").toLocalDate(),
							rs.getDate("fin").toLocalDate()));
				}
			} else {
				while (rs.next()) {
					Client client = clientService.findById(rs.getLong("client_id"));
					Vehicle vehicle = vehicleService.findById(rs.getLong("vehicle_id"));

					reservations.add(new Reservation(rs.getLong("id"),
							client, vehicle,
							rs.getDate("debut").toLocalDate(),
							rs.getDate("fin").toLocalDate()));
				}
			}
			return reservations;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			throw new RuntimeException(e);
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
