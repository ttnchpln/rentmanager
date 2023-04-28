package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Reservation;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cars/delete")
public class VehicleDeleteServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // private ClientService clientService = ClientService.getInstance();

    @Autowired
    VehicleService vehicleService;
    @Autowired
    ReservationService reservationService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            long idVehicle = Long.parseLong(request.getParameter("id"));

//            List<Reservation> reservations = reservationService.findResaByVehicleId(idVehicle);
//
//            if(reservations.size() > 0) {
//                for(Reservation resa : reservations) {
//                    reservationService.delete(resa);
//                }
//            }

            vehicleService.delete(idVehicle);

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("/rentmanager/cars");
    }

}

