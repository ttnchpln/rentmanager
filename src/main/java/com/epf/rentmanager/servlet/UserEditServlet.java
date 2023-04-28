package com.epf.rentmanager.servlet;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.YEARS;

@WebServlet("/users/edit")
public class UserEditServlet extends HttpServlet {

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

        try {

            long idClient = Long.parseLong(request.getParameter("id"));

            request.setAttribute("client", this.clientService.findById(idClient));

        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        this.getServletContext().getRequestDispatcher("/WEB-INF/views/users/edit.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        long idClient = Long.parseLong(request.getParameter("id"));

        String nom = request.getParameter("last_name");
        String prenom = request.getParameter("first_name");
        String email = request.getParameter("email");
        LocalDate birthday = LocalDate.parse(request.getParameter("birthday"));

        if(YEARS.between(birthday, LocalDate.now()) < 18) {
            request.setAttribute("message_erreur", "Un client n'ayant pas 18 ans ne peut pas être créé");
            doGet(request, response);
            return;
        }

        try {
            for(Client c : clientService.findAll()) {
                if(email.equals(c.getEmail()) && c.getId() != idClient) {
                    request.setAttribute("message_erreur", "Ce mail est déja attribué");
                    doGet(request, response);
                    return;
                }
            }
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        Client clientToAdd = new Client(idClient, nom, prenom, email, birthday);

        try {
            this.clientService.update(clientToAdd);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }

        response.sendRedirect("/rentmanager/users");
    }

}

