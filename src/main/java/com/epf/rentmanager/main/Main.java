package com.epf.rentmanager.main;

import com.epf.rentmanager.exception.ServiceException;
import com.epf.rentmanager.model.Client;
import com.epf.rentmanager.model.Reservation;
import com.epf.rentmanager.model.Vehicle;
import com.epf.rentmanager.service.ClientService;
import com.epf.rentmanager.service.ReservationService;
import com.epf.rentmanager.service.VehicleService;
import java.time.LocalDate;



import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting main... \n");

        boolean addClient = false;
        boolean addVehicle = false;
        boolean removeClient = false;
        boolean removeVehicle = false;

        try {

            // ------- CLIENTS ------------

            // clients.stream().forEach((client) -> System.out.println(client));

            System.out.println("Tous les clients :");
            for(Client c : ClientService.getInstance().findAll()) {
                System.out.println(c);
            }
            System.out.println();

            System.out.println("Client id = 3 :");
            System.out.println(ClientService.getInstance().findById(3));
            System.out.println();

            // ----------- VEHICULES ------------

            System.out.println("Tous les véhicules :");
            for(Vehicle v : VehicleService.getInstance().findAll()) {
                System.out.println(v);
            }
            System.out.println();

            System.out.println("Véhicule id = 3 :");
            System.out.println(VehicleService.getInstance().findById(3));
            System.out.println();

            // ------------ AJOUT -----------

            /*

            if(addClient) {
                System.out.println("Ajout d'un client");
                ClientService.getInstance().create(new Client("DUPONT", "Didier", "didier.dupont@email.com",
                        LocalDate.of(2001, 6, 27)));
            }

            if(addVehicle) {
                System.out.println("AJout d'un véhicule");
                VehicleService.getInstance().create(new Vehicle("BMW", "", 4));
            }

            System.out.println();

            // ---------- SUPPRESSION ---------

            if(removeClient) {
                System.out.println("Suppression");
                ClientService.getInstance().delete(ClientService.getInstance().findById(9));
            }

            if(removeVehicle) {
                System.out.println("Suppression");
                VehicleService.getInstance().delete(VehicleService.getInstance().findById(6));
            }
            */

            // -------Count ---------

            /*
            System.out.println("Nombre de clients = " + ClientService.getInstance().count());

            System.out.println("Nombre de véhicules = " + VehicleService.getInstance().count());

            ReservationService.getInstance().create(new Reservation(1, 1,
                    LocalDate.of(2023, 1, 27), LocalDate.of(2023, 2, 12)));
            */




        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }
}
