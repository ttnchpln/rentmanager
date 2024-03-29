package com.epf.rentmanager.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository

public class VehicleDao {

	public VehicleDao() {}
	private static final String CREATE_VEHICLE_QUERY = "INSERT INTO Vehicle(constructeur, modele, nb_places) VALUES(?, ?, ?);";
	private static final String DELETE_VEHICLE_QUERY = "DELETE FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLE_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle WHERE id=?;";
	private static final String FIND_VEHICLES_QUERY = "SELECT id, constructeur, modele, nb_places FROM Vehicle;";
	private static final String COUNT_VEHICLES = "SELECT COUNT(*) AS total FROM Vehicle";
	private static final String UPDATE_VEHICLE_QUERY = "UPDATE Vehicle SET constructeur = ?, modele = ?, nb_places = ? WHERE id = ?";
	
	public long create(Vehicle vehicle) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			//Statement statement = connexion.createStatement();

			PreparedStatement statement = connexion.prepareStatement(CREATE_VEHICLE_QUERY, Statement.RETURN_GENERATED_KEYS);

			statement.setString(1, vehicle.getConstructeur());
			statement.setString(2, vehicle.getModele());
			statement.setInt(3, vehicle.getNb_places());

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}

	public long delete(long idVehicle) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(DELETE_VEHICLE_QUERY);
			statement.setLong(1, idVehicle);

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}

	public Vehicle findById(long id) throws DaoException {

		Vehicle vehicle = new Vehicle();

		try  {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(FIND_VEHICLE_QUERY);

			statement.setLong(1, id);

			ResultSet rs = statement.executeQuery();

			while(rs.next()) {
				vehicle.setId(id);
				vehicle.setConstructeur(rs.getString("constructeur"));
				vehicle.setModele(rs.getString("modele"));
				vehicle.setNb_places(rs.getInt("nb_places"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}

		return vehicle;
	}

	public List<Vehicle> findAll() throws DaoException {

		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		try {
			Connection connexion = ConnectionManager.getConnection();

			Statement statement = connexion.createStatement();

			ResultSet rs = statement.executeQuery(FIND_VEHICLES_QUERY);

			while(rs.next()) {
				vehicles.add(new Vehicle(rs.getInt("id"),
						rs.getString("constructeur"),
						rs.getString("modele"),
						rs.getInt("nb_places")));
				//id, constructeur, nb_places
				//Vehicle(long id, String constructeur, String modele, int nb_places)
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}

		return vehicles;
	}

	public long update(Vehicle vehicle) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(UPDATE_VEHICLE_QUERY);

			statement.setString(1, vehicle.getConstructeur());
			statement.setString(2, vehicle.getModele());
			statement.setInt(3, vehicle.getNb_places());
			statement.setLong(4, vehicle.getId());

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}

	public int count() throws DaoException {
		int count = 0;

		try {
			Connection connexion = ConnectionManager.getConnection();

			Statement statement = connexion.createStatement();

			ResultSet rs = statement.executeQuery(COUNT_VEHICLES);

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
