package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;

import java.util.List;

public class ReservationService {

	private ReservationDao reservationDao;
	public static ReservationService instance;

	private ReservationService() {
		this.reservationDao = ReservationDao.getInstance();
	}
	
	public static ReservationService getInstance() {
		if (instance == null) {
			instance = new ReservationService();
		}
		
		return instance;
	}
	
	
	public long create(Reservation reservation) throws ServiceException {

		try {
			return ReservationDao.getInstance().create(reservation);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public long delete(Reservation reservation) throws ServiceException {
		try {
			return ReservationDao.getInstance().delete(reservation);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Reservation> findResaByClientId(long clientId) throws ServiceException {
		try {
			return ReservationDao.getInstance().findResaByClientId(clientId);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Reservation> findResaByVehicleId(long vehicleId) throws ServiceException {
		try {
			return ReservationDao.getInstance().findResaByVehicleId(vehicleId);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Reservation> findAll(boolean byId) throws ServiceException {

		try {
			return ReservationDao.getInstance().findAll(byId);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public int count() throws ServiceException {
		try {
			return ReservationDao.getInstance().count();
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}
}
