package api;

import models.Customer;
import models.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {

    public static Customer getCustomer(String email) {
        return CustomerService.getCustomer(email);
    }

    public static void addARoom(IRoom room){
        ReservationService.addRoom(room);
    }

    public static void addRoom(List<IRoom> rooms){
        ReservationService.addRooms(rooms);
    }

    public static List<IRoom> getAllRoom(){
        return ReservationService.getAllRoom();
    }

    public static Collection<Customer> getAllCustomers(){
        return CustomerService.getAllCustomers();
    }

    public static void DisplayAllReservations(){
        ReservationService.printAllReservation();
    }
}
