package api;

import models.Customer;
import models.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {
    private static AdminResource adminResource = new AdminResource();

    public static AdminResource getInstance(){
        return adminResource;
    }

    CustomerService customerService = CustomerService.getInstance();
    ReservationService reservationService = ReservationService.getInstance();


    public Customer getCustomer(String email) {
        return customerService.getCustomer(email);
    }

    public void addARoom(IRoom room){
        reservationService.addRoom(room);
    }

    public void addRoom(List<IRoom> rooms){
        reservationService.addRooms(rooms);
    }

    public List<IRoom> getAllRoom(){
        return reservationService.getAllRoom();
    }

    public Collection<Customer> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    public void DisplayAllReservations(){
        reservationService.printAllReservation();
    }
}
