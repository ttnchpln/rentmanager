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
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/users/details")
public class UserDetailServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // private ClientService clientService = ClientService.getInstance();

    @Autowired
    ReservationService reservationService;
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

            long idClient = Long.parseLong(request.getParameter("id"));

            List<Reservation> rents = this.reservationService.findResaByClientId(idClient, false);
            List<Vehicle> cars = this.reservationService.findVehiclesByClientId(idClient);

            request.setAttribute("client", this.clientService.findById(idClient));
            request.setAttribute("rents", rents);
            request.setAttribute("cars", cars);
            request.setAttribute("nb_rents", rents.size());
            request.setAttribute("nb_cars", cars.size());

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/details.jsp").forward(request, response);
    }

}

