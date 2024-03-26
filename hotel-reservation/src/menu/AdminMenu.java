package menu;

import api.AdminResource;
import api.HotelResource;
import enumerations.RoomType;
import models.Customer;
import models.IRoom;
import models.impl.Room;

import java.util.Arrays;
import java.util.Collection;
import java.util.Scanner;

public class AdminMenu extends Menu {

    public AdminMenu() {
        super(Arrays.asList("1. See all customers",
                "2. See all rooms",
                "3. See all reservations",
                "4. Add a room",
                "5. Back to Main Menu"));
    }


    public void showMenu() {
        String line = "";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            clearConsole();
            super.showMenu();
            super.printYourOption();
            line = scanner.nextLine();
            switch (line) {
                case "1":
                    displayAllCustomers();
                    break;
                case "2":
                    displayAllRooms();
                    break;
                case "3":
                    displayAllReservations();
                    break;
                case "4":
                    addNewRoom();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Please enter your selected option in range (1-5).");
            }
        }
    }

    public void displayAllCustomers(){
        Collection<Customer> customers = AdminResource.getAllCustomers();

        if (customers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            for(Customer customer: customers){
                System.out.println(customer);
            }
        }
        waitExitStatement();
    }

    public void displayAllRooms(){
        Collection<IRoom> rooms = AdminResource.getAllRoom();

        if (rooms.isEmpty()) {
            System.out.println("No rooms found.");
        } else {
            for(IRoom room: rooms){
                System.out.println(room);
            }
        }
        waitExitStatement();
    }

    public void displayAllReservations(){
        AdminResource.DisplayAllReservations();
        waitExitStatement();
    }

    private static void addNewRoom() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter room number:");
        String roomNumber = "";
        while (roomNumber.isEmpty()){
            roomNumber = scanner.nextLine();
            IRoom room = HotelResource.getRoom(roomNumber);
            if (room != null){
                roomNumber = "";
                System.out.println("This room number is contains. Please enter another room number.");
            }
        }


        System.out.println("Enter price per night:");
        String roomPriceStr = "";
        Double roomPrice = null;
        while (roomPriceStr.isEmpty()){
            roomPriceStr = scanner.nextLine();
            try{
                roomPrice = Double.valueOf(roomPriceStr);
            }
            catch (Exception e){
                roomPriceStr = "";
                System.out.println("Something is wrong, please enter price is decimal value.");
            }
        }

        System.out.println("Enter room type: 1 for single bed, 2 for double bed:");
        String roomTypeStr = "";
        RoomType roomType = null;
        while (roomTypeStr.isEmpty()){
            roomTypeStr = scanner.nextLine();
            switch (roomTypeStr){
                case "1":
                    roomType = RoomType.SINGLE;
                    break;
                case "2":
                    roomType = RoomType.DOUBLE;
                    break;
                default:
                    System.out.println("Something is wrong, please enter 1 or 2");
                    break;
            }
        }
        final Room room = new Room(roomNumber, roomPrice, roomType);

        AdminResource.addARoom(room);
        System.out.println("Create new room successfully!");
        waitExitStatement();
    }

    public static void waitExitStatement() {
        System.out.println("Press any key to continue.");
        try {
            System.in.read();
            clearConsole();
        } catch (Exception e) {
        }
    }

    private static void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

}
