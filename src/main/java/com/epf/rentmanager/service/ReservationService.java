package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ReservationService {

	private ReservationDao reservationDao;

	private ReservationService(ReservationDao reservationDao) {
		this.reservationDao = reservationDao;
	}
	
	/*
	public static ReservationService getInstance() {
		if (instance == null) {
			instance = new ReservationService();
		}
		
		return instance;
	}
	 */
	
	
	public long create(Reservation reservation) throws ServiceException {

		try {
			return reservationDao.create(reservation);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public long delete(Reservation reservation) throws ServiceException {
		try {
			return reservationDao.delete(reservation);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Vehicle> findVehiclesByClientId(long idClient) throws ServiceException {
		try {
			return reservationDao.findVehiclesByClientId(idClient);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Reservation> findResaByClientId(long clientId, boolean getId) throws ServiceException {
		try {
			return reservationDao.findResaByClientId(clientId, getId);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Reservation> findResaByVehicleId(long vehicleId) throws ServiceException {
		try {
			return reservationDao.findResaByVehicleId(vehicleId);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Reservation> findAll(boolean byId) throws ServiceException {

		try {
			return reservationDao.findAll(byId);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public int count() throws ServiceException {
		try {
			return reservationDao.count();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}
}
