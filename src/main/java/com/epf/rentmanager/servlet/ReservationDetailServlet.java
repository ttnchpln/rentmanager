package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/rents/details")
public class ReservationDetailServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    ReservationService reservationService;
    @Autowired
    VehicleService vehicleService;
    @Autowired
    ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            long id = Long.parseLong(request.getParameter("id"));

            Reservation reservation = this.reservationService.findById(id);

            request.setAttribute("rent", reservation);
            request.setAttribute("vehicle", this.vehicleService.findById(reservation.getVehicle_id()));
            request.setAttribute("client", this.clientService.findById(reservation.getClient_id()));

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/details.jsp").forward(request, response);
    }
}

