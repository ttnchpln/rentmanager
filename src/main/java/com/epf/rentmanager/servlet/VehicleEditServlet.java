package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet("/cars/edit")
public class VehicleEditServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    VehicleService vehicleService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            long idClient = Long.parseLong(request.getParameter("id"));

            request.setAttribute("vehicle", this.vehicleService.findById(idClient));

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/vehicles/edit.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        long idVehicle = Long.parseLong(request.getParameter("id"));

        String manufacturer = request.getParameter("manufacturer");
        String modele = request.getParameter("modele");
        int seats = Integer.parseInt(request.getParameter("seats"));

        try {
            this.vehicleService.update(new Vehicle(idVehicle, manufacturer, modele, seats));
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("/rentmanager/cars");
    }

}

