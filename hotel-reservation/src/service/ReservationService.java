package service;

import models.Customer;
import models.IRoom;
import models.Reservation;

import java.util.*;

import static provider.data.reservations;
import static provider.data.rooms;

public class ReservationService {

    public static void addRooms(List<IRoom> room) {
        rooms.addAll(room);
    }
    public static void addRoom(IRoom room) {
        rooms.add(room);
    }

    public static List<IRoom> getAllRoom(){
        return rooms;
    }

    public static IRoom getARoom(String roomId) {
        Optional<IRoom> room = rooms.stream().filter(r -> r.getRoomNumber().equals(roomId)).findFirst();

        return room.orElse(null);
    }

    public static Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public static Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {
        return getAllAvailableRoom(checkInDate, checkOutDate);
    }

    public static Collection<Reservation> getCustomersReservation(Customer customer) {

        return reservations.stream().filter(r -> r.getCustomerEmail().equals(customer.getEmail())).toList();
    }

    public static void printAllReservation() {
        if (reservations == null || reservations.isEmpty()) {
            System.out.println("There is no reservations.");
        } else {
            for (Reservation reservation : reservations) {
                System.out.println(reservation + "\n");
            }
        }
    }

    private static List<IRoom> getAllAvailableRoom(Date checkInDate, Date checkOutDate) {
        List<Reservation> overlapReservations = getAllReservationsOverlap(checkInDate, checkOutDate);
        List<String> overlapRooms = overlapReservations.stream().map(Reservation::getRoomNumber).toList();
        return rooms.stream().filter(r -> !overlapRooms.contains(r.getRoomNumber())).toList();
    }

    private static List<Reservation> getAllReservationsOverlap(final Date checkInDate, final Date checkOutDate) {
        List<Reservation> result = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (isOverlap(reservation, checkInDate, checkOutDate)) {
                result.add(reservation);
            }
        }

        return result;
    }

    private static boolean isOverlap(Reservation reservation, Date checkInDate, Date checkOutDate) {
        if ((!checkInDate.after(reservation.getCheckOutDate()) && !checkInDate.before(reservation.getCheckInDate())) ||
                (!checkOutDate.after(reservation.getCheckOutDate()) && !checkOutDate.before(reservation.getCheckInDate()))) {
            return true;
        }

        return false;
    }
}
