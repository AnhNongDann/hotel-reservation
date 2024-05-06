package service;

import models.Customer;
import models.IRoom;
import models.Reservation;

import java.util.*;
public class ReservationService {

    private static ReservationService reservationService =new ReservationService();
    private List<IRoom> rooms = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();

    public static ReservationService getInstance(){
        return reservationService;
    }

    public void addRooms(List<IRoom> room) {
        rooms.addAll(room);
    }
    public void addRoom(IRoom room) {
        rooms.add(room);
    }

    public List<IRoom> getAllRoom(){
        return rooms;
    }

    public IRoom getARoom(String roomId) {
        Optional<IRoom> room = rooms.stream().filter(r -> r.getRoomNumber().equals(roomId)).findFirst();

        return room.orElse(null);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        return getAllAvailableRoom(checkInDate, checkOutDate);
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {

        return reservations.stream().filter(r -> r.getCustomerEmail().equals(customer.getEmail())).toList();
    }

    public void printAllReservation() {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("There is no reservations.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation + "\n");
            }
        }
    }

    private List<IRoom> getAllAvailableRoom(Date checkInDate, Date checkOutDate) {
        List<Reservation> overlapReservations = getAllReservationsOverlap(checkInDate, checkOutDate);
        List<String> overlapRooms = overlapReservations.stream().map(Reservation::getRoomNumber).toList();
        return rooms.stream().filter(r -> !overlapRooms.contains(r.getRoomNumber())).toList();
    }

    private List<Reservation> getAllReservationsOverlap(final Date checkInDate, final Date checkOutDate) {
        List<Reservation> result = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (isOverlap(reservation, checkInDate, checkOutDate)) {
                result.add(reservation);
            }
        }

        return result;
    }

    private boolean isOverlap(Reservation reservation, Date checkInDate, Date checkOutDate) {
        if ((!checkInDate.after(reservation.getCheckOutDate()) && !checkInDate.before(reservation.getCheckInDate())) ||
                (!checkOutDate.after(reservation.getCheckOutDate()) && !checkOutDate.before(reservation.getCheckInDate()))) {
            return true;
        }

        return false;
    }
}
