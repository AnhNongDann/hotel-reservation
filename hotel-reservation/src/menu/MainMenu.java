package menu;

import api.AdminResource;
import api.HotelResource;
import models.Customer;
import models.IRoom;
import models.Reservation;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

import static Constants.ConfigConstants.*;


public class MainMenu extends Menu {
    private HotelResource hotelResource = HotelResource.getInstance();
    private AdminResource adminResource = AdminResource.getInstance();


    public MainMenu() {
        super(Arrays.asList("1. Find and reserve a room",
                "2. See my reservation",
                "3. Create an account",
                "4. Admin",
                "5. Exit"));
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
                    serveRoom();
                    break;
                case "2":
                    getAllReservation();
                    break;
                case "3":
                    createAccount();
                    break;
                case "4":
                    AdminMenu adminMenu = new AdminMenu();
                    adminMenu.showMenu();
                    break;
                case "5":
                    System.out.println("-------------------------Thank you. See you later!-------------------------");
                    return;
                default:
                    System.out.println("Please enter your selected option in range (1-5).");
            }
        }


    }

    private void getAllReservation() {
        final Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your Email with format: name@domain.com");
        final String customerEmail = scanner.nextLine();
        Customer customer = adminResource.getCustomer(customerEmail);
        if (customer == null) {
            System.out.println("Something is wrong, this is not email entered with us. pleas help me check again.");
            waitExitStatement();
        } else {
            Collection<Reservation> allReservation = hotelResource.getCustomersReservations(customerEmail);

            if (allReservation.isEmpty()) {
                System.out.println("You have not reservation any room with us.");
                waitExitStatement();
            } else {
                for (Reservation reservation : allReservation) {
                    System.out.println(reservation);
                }
                waitExitStatement();
            }
        }

    }

    private void serveRoom() {
        final Scanner scanner = new Scanner(System.in);

        String checkInDateStr = "";
        Date checkInDate = null;
        while (Objects.isNull(checkInDate)) {
            // clear console
            System.out.print("\033[H\033[2J");
            System.out.flush();
            if (!checkInDateStr.isEmpty()) {
                System.out.println("Maybe Check-In Date entered is wrong format, please help me enter again.");
            }
            System.out.println("Enter Check-In Date with format mm/dd/yyyy. example 03/25/2024");
            checkInDateStr = scanner.nextLine();
            checkInDate = convertStringToDate(checkInDateStr);
        }

        String checkOutDateStr = "";
        Date checkOutDate = null;
        while (Objects.isNull(checkOutDate)) {
            // clear console
            System.out.print("\033[H\033[2J");
            System.out.flush();
            if (!checkOutDateStr.equals("")) {
                System.out.println("Maybe Check-Out Date entered is wrong format or Check-Out Date before Check-In Date, please help me enter again.");
            }
            System.out.println("Enter Check-Out Date with format mm/dd/yyyy. example 03/25/2024");
            checkOutDateStr = scanner.nextLine();
            checkOutDate = convertStringToDate(checkOutDateStr);
            if (checkOutDate != null && checkOutDate.before(checkInDate)){
                checkOutDate = null;
            }
        }
        serveRoom(scanner, checkInDate, checkOutDate);
    }

    public void serveRoom(final Scanner scanner, Date checkInDate, Date checkOutDate) {
        clearConsole();
        System.out.println("Your request is Check-in Date : " + dateToString(checkInDate) + " and Check-out Date : " + dateToString(checkOutDate));

        Collection<IRoom> availableRooms = hotelResource.findARoom(checkInDate, checkOutDate);
        if (!availableRooms.isEmpty()) {
            System.out.println("With your request, we have some option as bellow: ");
            availableRooms.forEach(r -> {
                System.out.println(r + "\n");
            });

            System.out.println("Please enter you room which you want to book, enter \"exit\" if you want to exit.");

            IRoom bookedRoom = null;
            while (bookedRoom == null) {
                String roomNumber = scanner.nextLine();
                if (roomNumber.equals("exit")) {
                    return;
                }

                Optional<IRoom> opBookedRoom = availableRooms.stream().filter(r -> r.getRoomNumber().equals(roomNumber)).findFirst();
                if (opBookedRoom.isPresent()) {
                    bookedRoom = opBookedRoom.get();
                } else {
                    System.out.println("The room number entered is wrong, please help me check and enter your selected room number again.");
                }
            }

            serveRoom(scanner, bookedRoom, checkInDate, checkOutDate);
        } else {
            System.out.println("We have no available rooms for you from " + dateToString(checkInDate) + " to " + dateToString(checkOutDate));
            System.out.println("We recommend you book room from " + dateToString(addRecommendPlusDays(checkInDate)) + " to " + dateToString(addRecommendPlusDays(checkOutDate)));
            System.out.println("Would you like to book room in that time? y/n");
            String agreeWithRecommend = "";
            while (agreeWithRecommend.isEmpty()) {
                agreeWithRecommend = scanner.nextLine();
                if (agreeWithRecommend.equals("n")) {
                    return;
                }
                if (!agreeWithRecommend.equals("y")) {
                    System.out.println("the word entered is wrong, please enter y or n");
                    agreeWithRecommend = "";
                }
            }

            checkInDate = addRecommendPlusDays(checkInDate);
            checkOutDate = addRecommendPlusDays(checkOutDate);
            serveRoom(scanner, checkInDate, checkOutDate);
        }

        return;
    }

    private void serveRoom(final Scanner scanner, IRoom bookedRoom, Date checkInDate, Date checkOutDate) {
        System.out.println("The room you request is " + bookedRoom);
        Customer customer = null;
        String customerEmail = "";
        while (customer == null) {
            System.out.println("Please enter your email registered with us or enter \"register\" to create an account");
            customerEmail = scanner.nextLine();

            if (customerEmail.equals("register")) {
                createAccount();
                System.out.println("The room you request is " + bookedRoom);
                continue;
            }

            customer = adminResource.getCustomer(customerEmail);
            if (customer == null) {
                System.out.println("Something is wrong, this is not email entered with us. pleas help me check again.");
            }
        }

        Reservation reservation = hotelResource.bookARoom(customerEmail, bookedRoom, checkInDate, checkOutDate);
        System.out.println("Book room successfully");
        System.out.println(reservation);
        waitExitStatement();
    }

    private void createAccount() {
        final Scanner scanner = new Scanner(System.in);
        while (true) {

            System.out.println("Enter Email format: name@domain.com");
            final String email = scanner.nextLine();

            System.out.println("First Name:");
            final String firstName = scanner.nextLine();

            System.out.println("Last Name:");
            final String lastName = scanner.nextLine();

            try {
                hotelResource.createACustomer(email, firstName, lastName);
                System.out.println("Account is created successfully!");
                waitExitStatement();
                return;
            } catch (IllegalArgumentException ex) {
                System.out.println("Something is wrong, please help me check your email.");
            }
        }
    }

    public static void waitExitStatement() {
        System.out.println("Press any key to continue.");
        try {
            System.in.read();
            clearConsole();
        } catch (Exception e) {
        }
    }


    public Date addRecommendPlusDays(Date date) {
        return new Date(date.getTime() + (RECOMMEND_PLUS_DAY * TIME_FOR_ONE_DAY));
    }


    private Date convertStringToDate(String dateValue) {
        try {
            Pattern pattern = Pattern.compile(DATE_FORMAT_REGEX);

            if (!pattern.matcher(dateValue).matches()) {
                return null;
            }

            return new SimpleDateFormat(DATE_FORMAT).parse(dateValue);
        } catch (ParseException ex) {
            return null;
        }
    }


    private static void clearConsole() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }

    private static String dateToString(Date date) {
        Format formatter = new SimpleDateFormat(DATE_FORMAT);
        return formatter.format(date);
    }
}
