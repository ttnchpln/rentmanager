package com.epf.rentmanager.service;

import com.epf.rentmanager.dao.ReservationDao;
import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
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

	public long update(Reservation reservation) throws ServiceException {

		try {
			return reservationDao.update(reservation);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public long delete(long id) throws ServiceException {
		try {
			return reservationDao.delete(id);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public Reservation findById(long id) throws ServiceException {
		try {
			return reservationDao.findById(id);
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

	public List<Client> findClientsByVehicleId(long idVehicle) throws ServiceException {
		try {
			return reservationDao.findClientsByVehicleId(idVehicle);
		} catch (DaoException e) {
			e.printStackTrace();
			throw new ServiceException();
		}
	}

	public List<Reservation> findResaByClientId(long clientId) throws ServiceException {
		try {
			return reservationDao.findResaByClientId(clientId);
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

	public boolean validDates(Reservation newRes) throws DaoException {
		return reservationDao.validDates(newRes);
	}

	public int countConsecutivDays(Reservation res) throws DaoException {
		return reservationDao.countConsecutivDays(res);
	}

	public int countConsecutivDaysUser(Reservation res) throws DaoException {
		return reservationDao.countConsecutivDaysUser(res);
	}


}
