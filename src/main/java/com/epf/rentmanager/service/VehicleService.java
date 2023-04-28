package com.epf.rentmanager.service;

import java.util.List;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.dao.ClientDao;
import com.epf.rentmanager.dao.VehicleDao;
import org.springframework.stereotype.Service;

@Service

public class VehicleService {

	private VehicleDao vehicleDao;
	
	public VehicleService(VehicleDao vehicleDao) {
		this.vehicleDao = vehicleDao;
	}
	
	/*
	public static VehicleService getInstance() {
		if (instance == null) {
			instance = new VehicleService();
		}
		
		return instance;
	}
	 */
	
	
	public long create(Vehicle vehicle) throws ServiceException {

		try {
			return vehicleDao.create(vehicle);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public long delete(long idVehicle) throws ServiceException {
		try {
			return vehicleDao.delete(idVehicle);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public Vehicle findById(long id) throws ServiceException {
		try {
			return vehicleDao.findById(id);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Vehicle> findAll() throws ServiceException {

		try {
			return vehicleDao.findAll();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public long update(Vehicle vehicle) throws ServiceException {

		try {
			return vehicleDao.update(vehicle);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public int count() throws ServiceException {
		try {
			return vehicleDao.count();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}
}
