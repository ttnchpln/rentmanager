package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

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

import static java.time.temporal.ChronoUnit.DAYS;

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
	private static final String FIND_RESERVATION_QUERY = "SELECT id, client_id, vehicle_id, debut, fin FROM Reservation WHERE id=?;";
	private static final String COUNT_RESERVATIONS = "SELECT COUNT(*) AS total FROM Reservation";
	private static final String UPDATE_RESERVATION_QUERY = "UPDATE Reservation SET client_id = ?, vehicle_id = ?, debut = ?, fin = ? WHERE id = ?";

	public long create(Reservation reservation) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(CREATE_RESERVATION_QUERY);

			statement.setString(1, String.valueOf(reservation.getClient_id()));
			statement.setString(2, String.valueOf(reservation.getVehicle_id()));
			statement.setString(3, reservation.getDebut().toString());
			statement.setString(4, reservation.getFin().toString());

			return statement.executeUpdate();

		} catch (SQLException e) {
			throw new DaoException();
		}
	}
	
	public long delete(long id) throws DaoException {
		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(DELETE_RESERVATION_QUERY);
			statement.setLong(1, id);

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}

	public long update(Reservation reservation) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(UPDATE_RESERVATION_QUERY);

			statement.setString(1, String.valueOf(reservation.getClient_id()));
			statement.setString(2, String.valueOf(reservation.getVehicle_id()));
			statement.setString(3, reservation.getDebut().toString());
			statement.setString(4, reservation.getFin().toString());
			statement.setLong(5, reservation.getId());

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}

	public Reservation findById(long id) throws DaoException {

		Reservation reservation = new Reservation();

		try {
			Connection connexion = ConnectionManager.getConnection();
			PreparedStatement statement = connexion.prepareStatement(FIND_RESERVATION_QUERY);
			statement.setLong(1, id);

			ResultSet rs = statement.executeQuery();

			while (rs.next()) {
				reservation.setClient_id(rs.getInt("client_id"));
				reservation.setVehicle_id(rs.getInt("vehicle_id"));
				reservation.setDebut(rs.getDate("debut").toLocalDate());
				reservation.setFin(rs.getDate("fin").toLocalDate());
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
		return reservation;
	}

	public List<Vehicle> findVehiclesByClientId(long idClient) throws DaoException {
		List<Reservation> rents = this.findResaByClientId(idClient);

		List<Vehicle> cars = new ArrayList<>();

		for(Reservation rent : rents) {
			cars.add(rent.getVehicle());
		}

		Set<Vehicle> set = new HashSet<>(cars);
		cars.clear();
		cars.addAll(set);

		return cars;
	}

	public List<Client> findClientsByVehicleId(long idVehicle) throws DaoException {
		List<Reservation> rents = this.findResaByVehicleId(idVehicle);

		List<Client> clients = new ArrayList<>();

		for(Reservation rent : rents) {
			clients.add(rent.getClient());
		}

		Set<Client> set = new HashSet<>(clients);
		clients.clear();
		clients.addAll(set);

		return clients;
	}

	public List<Reservation> findResaByClientId(long clientId) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(FIND_RESERVATIONS_BY_CLIENT_QUERY);

			statement.setLong(1, clientId);

			ResultSet rs = statement.executeQuery();

			List<Reservation> reservations = new ArrayList<>();


			while (rs.next()) {
				Client client = clientService.findById(clientId);
				Vehicle vehicle = vehicleService.findById(rs.getLong("vehicle_id"));

				reservations.add(new Reservation(rs.getLong("id"),
						client, vehicle,
						rs.getDate("debut").toLocalDate(),
						rs.getDate("fin").toLocalDate()));
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
				Vehicle vehicle = vehicleService.findById(vehicleId);
				Client client = clientService.findById(rs.getLong("client_id"));

				reservations.add(new Reservation(rs.getLong("id"),
						client, vehicle,
						rs.getDate("debut").toLocalDate(),
						rs.getDate("fin").toLocalDate()));
			}
			return reservations;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
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

	public boolean validDates(Reservation newRes) throws DaoException {
		for(Reservation res : this.findAll(true)) {
			if(newRes.getId() != res.getId()) {
				if (newRes.getDebut().isAfter(res.getDebut()) && newRes.getDebut().isBefore(res.getFin())
						|| newRes.getFin().isAfter(res.getDebut()) && newRes.getFin().isBefore(res.getFin())) {
					return false;
				}
			}
		}
		return true;
	}

	public int countConsecutivDays(Reservation reseToAdd) throws DaoException {

		List<Reservation> allResas = this.findAll(true);
		allResas.add(reseToAdd);
		Collections.sort(allResas, new Comparator<Reservation>() {
			@Override
			public int compare(Reservation o1, Reservation o2) {
				if (o1.getDebut().isBefore(o2.getDebut())) {
					return -1;
				} else {
					return 1;
				}
			}
		});

		LocalDate vraiDebut = allResas.get(0).getDebut();
		LocalDate lastFin = allResas.get(0).getFin();

		int nbJourConsecutif = 0;

		if(allResas.size() > 1) {
			if(reseToAdd.getDebut().equals(allResas.get(allResas.size()-2).getFin().plusDays(1))) {
				for (Reservation res : allResas) {
					if (!lastFin.plusDays(1).equals(res.getDebut())) {
						vraiDebut = res.getDebut();
					}
					lastFin = res.getFin();
					nbJourConsecutif = (int) DAYS.between(vraiDebut, lastFin);
				}
			}
		}

		return nbJourConsecutif;
	}

	public int countConsecutivDaysUser(Reservation reseToAdd) throws DaoException {

		int nbJourConsecutif = (int) DAYS.between(reseToAdd.getDebut(), reseToAdd.getFin());
		if(nbJourConsecutif > 7) {
			return nbJourConsecutif;
		}

		List<Reservation> allResas = this.findAll(true);
		allResas.add(reseToAdd);
		Collections.sort(allResas, new Comparator<Reservation>() {
			@Override
			public int compare(Reservation o1, Reservation o2) {
				if (o1.getDebut().isBefore(o2.getDebut())) {
					return -1;
				} else {
					return 1;
				}
			}
		});

		LocalDate vraiDebut = allResas.get(0).getDebut();
		LocalDate lastFin = allResas.get(0).getFin();

		if(allResas.size() > 1) {
			if(reseToAdd.getDebut().equals(allResas.get(allResas.size()-2).getFin().plusDays(1))) {
				// si le debut de na nouvelle resa tombe juste apres la fin de la derniere resa
				for (Reservation res : allResas) {
					if (!lastFin.plusDays(1).equals(res.getDebut())) {
						vraiDebut = res.getDebut();
					}
					if(reseToAdd.getClient_id() == res.getClient_id()) {
						vraiDebut = res.getDebut();
					}
					lastFin = res.getFin();
					nbJourConsecutif = (int) DAYS.between(vraiDebut, lastFin);
				}
			}
		}
		return nbJourConsecutif;
	}
}
