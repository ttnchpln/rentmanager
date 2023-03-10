package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ReservationService reservationService = ReservationService.getInstance();
    private ClientService clientService = ClientService.getInstance();
    private VehicleService vehicleService = VehicleService.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        try {
            request.setAttribute("clients", this.clientService.findAll());
            request.setAttribute("vehicles", this.vehicleService.findAll());
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/rents/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String idCar = request.getParameter("car");
        String idClient = request.getParameter("client");
        String[] debut = request.getParameter("begin").split("/");
        String[] fin = request.getParameter("end").split("/");

        // Parametre formulaire = jour/mois/année
        // LocalDate = année-mois-jour

        LocalDate debutDate = LocalDate.parse(debut[2]+"-"+debut[1]+"-"+debut[0]);
        LocalDate finDate = LocalDate.parse(fin[2]+"-"+fin[1]+"-"+fin[0]);

        Reservation reseToAdd = new Reservation(Long.parseLong(idCar), Long.parseLong(idClient), debutDate, finDate);

        try {
            this.reservationService.create(reseToAdd);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

}

