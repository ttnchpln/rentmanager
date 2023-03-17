package com.epf.rentmanager.dao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.persistence.ConnectionManager;
import org.springframework.stereotype.Repository;

@Repository
public class ClientDao {
	private ClientDao() {}
	private static final String CREATE_CLIENT_QUERY = "INSERT INTO Client(nom, prenom, email, naissance) VALUES(?, ?, ?, ?);";
	private static final String DELETE_CLIENT_QUERY = "DELETE FROM Client WHERE id=?;";
	private static final String FIND_CLIENT_QUERY = "SELECT nom, prenom, email, naissance FROM Client WHERE id=?;";
	private static final String FIND_CLIENTS_QUERY = "SELECT id, nom, prenom, email, naissance FROM Client;";
	private static final String COUNT_VEHICLES = "SELECT COUNT(*) AS total FROM Client";
	
	public long create(Client client) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(CREATE_CLIENT_QUERY);

			statement.setString(1, client.getNom());
			statement.setString(2, client.getPrenom());
			statement.setString(3, client.getEmail());
			statement.setString(4, client.getDateDeNaissance().toString());

			//Statement statement = connexion.createStatement();
			//String toReplace = String.format("'%s', '%s', '%s', '%s'", client.getNom(), client.getPrenom(), client.getEmail(), client.getDateDeNaissance().toString());
			//String query = CREATE_CLIENT_QUERY.replace("?, ?, ?, ?", toReplace);
			//System.out.println("Query = " + query);

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}
	
	public long delete(Client client) throws DaoException {

		try {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(DELETE_CLIENT_QUERY);
			statement.setLong(1, client.getId());

			return statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}
	}

	public Client findById(long id) throws DaoException {

		Client client = new Client();

		try  {
			Connection connexion = ConnectionManager.getConnection();

			PreparedStatement statement = connexion.prepareStatement(FIND_CLIENT_QUERY);

			statement.setLong(1, id);

			ResultSet rs = statement.executeQuery();

			while(rs.next()) {
				client.setId(id);
				client.setNom(rs.getString("nom"));
				client.setPrenom(rs.getString("prenom"));
				client.setEmail(rs.getString("email"));
				client.setDateDeNaissance(rs.getDate("naissance").toLocalDate());
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}

		return client;
	}

	public List<Client> findAll() throws DaoException {

		List<Client> clients = new ArrayList<Client>();
		try {
			Connection connexion = ConnectionManager.getConnection();

			Statement statement = connexion.createStatement();

			ResultSet rs = statement.executeQuery(FIND_CLIENTS_QUERY);

			while(rs.next()) {

				clients.add(new Client(rs.getInt("id"),
						rs.getString("nom"),
						rs.getString("prenom"),
						rs.getString("email"),
						rs.getDate("naissance").toLocalDate()));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DaoException();
		}

		return clients;
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
