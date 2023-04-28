package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.DaoException;
import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.aop.support.DelegatePerTargetObjectIntroductionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@WebServlet("/rents/create")
public class ReservationCreateServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    VehicleService vehicleService;
    @Autowired
    ClientService clientService;
    @Autowired
    ReservationService reservationService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

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
        LocalDate debutDate = LocalDate.parse(request.getParameter("begin"));
        LocalDate finDate = LocalDate.parse(request.getParameter("end"));

        Reservation reseToAdd = new Reservation(Long.parseLong(idClient), Long.parseLong(idCar), debutDate, finDate);

        try {
            if(!reservationService.validDates(reseToAdd)) {
                request.setAttribute("message_erreur", "Une voiture ne peut pas être réservé 2 fois le même jour");
                doGet(request, response);
                return;
            }
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        try {
            if (reservationService.countConsecutivDays(reseToAdd) >= 30) {
                request.setAttribute("message_erreur", "Une voiture ne peut pas être réservée 30 jours de suite sans pause");
                doGet(request, response);
                return;
            }
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        try {
            if(reservationService.countConsecutivDaysUser(reseToAdd) > 7) {
                request.setAttribute("message_erreur", "Une voiture ne peut pas être réservé plus de 7 jours de suite par le même utilisateur");
                doGet(request, response);
                return;
            }
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }

        try {
            this.reservationService.create(reseToAdd);
        } catch (ServiceException e) {
            throw new ServletException();
        }

        response.sendRedirect("/rentmanager/rents");
    }

}

