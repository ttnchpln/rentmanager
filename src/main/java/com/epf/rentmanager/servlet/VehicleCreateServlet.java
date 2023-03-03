package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.VehicleService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/cars/create")
public class VehicleCreateServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private VehicleService vehicleService = VehicleService.getInstance();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String constructeur = request.getParameter("manufacturer");
        String modele = request.getParameter("modele");
        String nb_places = request.getParameter("seats");

        Vehicle vehicleToAdd = new Vehicle(constructeur, modele, Integer.parseInt(nb_places));
        System.out.println(vehicleToAdd.toString());
        try {
            this.vehicleService.create(vehicleToAdd);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }

}

