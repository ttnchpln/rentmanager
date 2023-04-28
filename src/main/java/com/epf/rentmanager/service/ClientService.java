package com.epf.rentmanager.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import org.springframework.stereotype.Service;

@Service

public class ClientService {

	private ClientDao clientDao;
	
	private ClientService(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	
	/*
	public static ClientService getInstance() {
		if (instance == null) {
			instance = new ClientService();
		}
		
		return instance;
	}
	 */

	public long delete(long idClient) throws ServiceException {
		try {
			return clientDao.delete(idClient);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public long create(Client client) throws ServiceException {

		try {
			return clientDao.create(client);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public Client findById(long id) throws ServiceException {

		try {
			return clientDao.findById(id);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Client> findAll() throws ServiceException {

		try {
			return clientDao.findAll();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public long update(Client client) throws ServiceException {

		try {
			return clientDao.update(client);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public int count() throws ServiceException {

		try {
			return clientDao.count();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}
}
