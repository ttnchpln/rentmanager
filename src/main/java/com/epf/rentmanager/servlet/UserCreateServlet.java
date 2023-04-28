package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
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
import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;

@WebServlet("/users/create")
public class UserCreateServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // private ClientService clientService = ClientService.getInstance();

    @Autowired
    ClientService clientService;
    @Override
    public void init() throws ServletException {
        super.init();
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nom = request.getParameter("last_name");
        String prenom = request.getParameter("first_name");
        String email = request.getParameter("email");
        LocalDate birthday = LocalDate.parse(request.getParameter("birthday"));

        if(YEARS.between(birthday, LocalDate.now()) < 18) {
            request.setAttribute("message_erreur", "Un client n'ayant pas 18 ans ne peut pas être créé");
            this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
            return;
        }

        try {
            for(Client c : clientService.findAll()) {
                if(email.equals(c.getEmail())) {
                    request.setAttribute("message_erreur", "Ce mail est déja attribué");
                    this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/create.jsp").forward(request, response);
                    return;
                }
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        Client clientToAdd = new Client(nom, prenom, email, birthday);

        try {
            this.clientService.create(clientToAdd);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("/rentmanager/users");
    }

}

