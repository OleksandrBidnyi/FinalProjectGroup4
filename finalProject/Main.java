package finalProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static finalProject.Controller.*;

/**
 * Created by Aleksandr on 03.05.2017.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Controller controller = new Controller();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        first:
        while (true) {
            System.out.println("1-search rooms, 2- search hotels by name, 3- search hotel by city\n" +
                    "Authorize for reservation: 4-enter, 5- registration." +
                    "\n0- finish work");
            String choice1 = bufferedReader.readLine();

            if (choice1.equals("0")) break;
            //search room
            if (choice1.equals("1")) {
                while (true) {
                    System.out.println("For searching rooms you should enter parameters split they by symbol / in such format:" +
                            "city/hotel`s name/count of persons/low price/up price, example: Lviv/Dnipro/2/200/500 \n" +
                            "if parameter doesn`t matter - use symbol *. " +
                            "Also: 9- main menu, 0- finish work");
                    String choice4 = bufferedReader.readLine();
                    if (choice4.equals("0")) break first;
                    if (choice4.equals("9")) continue first;
                    String s[] = choice4.split("/");
                    if (s.length != 5) {
                        System.out.println("Incorrect input!");
                        continue;
                    }
                    Map<String, String> map = new HashMap<>();
                    map.put(CITY, s[0].trim());
                    map.put(HOTEL_NAME, s[1].trim());
                    map.put(PERSONS, s[2].trim());
                    map.put(MIN_PRICE, s[3].trim());
                    map.put(MAX_PRICE, s[4].trim());
                    List<Room> rooms = controller.findRoom(map);
                    if (rooms.size() == 0) System.out.println("Haven`t any result. Retry with another parameters.");
                    rooms.forEach(System.out::println);
                    System.out.println("Log in to the main menu for booking." +
                            "\n Do you want to repeat the search?");
                }
            }

            //search hotels by name
            if (choice1.equals("2")) {
                while (true) {
                    System.out.println("Enter the name of hotel, or part of name;" +
                            " get all list - just press Enter; " +
                            "9- previous menu; 0- finish work");
                    String choice4 = bufferedReader.readLine();
                    if (choice4.equals("0")) break first;
                    if (choice4.equals("9")) continue first;
                    controller.findHotelByName(choice4.trim()).forEach(System.out::println);
                    System.out.println("Do you want to continue the search?");
                }

            }

            //search hotel by city
            if (choice1.equals("3")) {
                while (true) {
                    System.out.println("Enter name of city, or part of name; " +
                            "get all list  - just press Enter; " +
                            "9- previous menu; 0- finish work");
                    String choice2 = bufferedReader.readLine();
                    if (choice2.equals("0")) break first;
                    if (choice2.equals("9")) continue first;
                    controller.findHotelByCity(choice2.trim()).forEach(System.out::println);
                    System.out.println("Do you want to continue the search?");
                }
            }

            //enter
            if (choice1.equals("4")) {
                User user;
                while (true) {
                    System.out.println("Enter. \n" +
                            "Enter your name and password using symbol /, example: Peter/1111; " +
                            "or 9- previous menu, 0- finish work");
                    String choice2 = bufferedReader.readLine();
                    if (choice2.equals("admin/0000")) {
                        Admin.root();
                        continue first;
                    }
                    if (choice2.length() == 1) {
                        if (choice2.equals("0")) break first;
                        if (choice2.equals("9")) continue first;
                    }
                    String nameAndPass[] = choice2.split("/");
                    if (nameAndPass.length != 2) {
                        System.out.println("Incorrect input");
                        continue;
                    }
                    user = controller.enter(nameAndPass[0].toLowerCase().trim(), nameAndPass[1].trim());

                    //user has entered by his own name
                    if (user != null) {
                        third:
                        while (true) {
                            System.out.println("\n1- search rooms/reserve, 2- search hotel by name," +
                                    "3- search hotel by city," +
                                    "\n5- list of yours reserving rooms/cancel reservation," +
                                    "\n7- edit name/password, 9- log out, 0- finish work");
                            String choice3 = bufferedReader.readLine();
                            if (choice3.equals("0")) break first;
                            if (choice3.equals("9")) {
                                user = null;
                                continue first;
                            }

                            //edit name/password
                            if (choice3.equals("7")) {
                                while (true) {
                                    System.out.println("For editing your profile, enter you current name and password" +
                                            "using symbol /, example: Peter/1111; or 1- cancel, 0- finish work");
                                    String choice4 = bufferedReader.readLine();
                                    if (choice4.equals("0")) break first;
                                    if (choice4.equals("1")) continue third;
                                    String split[] = choice4.split("/");
                                    if (split.length != 2) {
                                        System.out.println("Incorrect input");
                                        continue;
                                    }
                                    if (split[0].trim().trim().equals(user.getName().toLowerCase()) &&
                                            split[1].trim().equals(user.getPassword())) {
                                        while (true) {
                                            System.out.println("Enter new name and password using symbol /," +
                                                    " example: Masha/2222; or 1- cancel, 0- finish work");
                                            String choice5 = bufferedReader.readLine();
                                            if (choice5.equals("0")) break first;
                                            if (choice5.equals("1")) continue third;
                                            String splitNewNameAndPass[] = choice5.split("/");
                                            if (splitNewNameAndPass.length != 2) {
                                                System.out.println("Incorrect enter");
                                                continue;
                                            }
                                            if (controller.editUser(new User(user.getId(), splitNewNameAndPass[0].trim(),
                                                    splitNewNameAndPass[1].trim()))) {
                                                System.out.println("Profile has been successfully changed! " +
                                                        "Your current profile: " + user);
                                            } else {
                                                System.out.println("Something is going wrong! Retry please.");
                                                continue third;
                                            }
                                        }
                                    }
                                    System.out.println("Incorrect login or password");
                                    user = null;
                                    continue first;
                                }
                            }

                            //search hotel by name
                            if (choice3.equals("2")) {
                                while (true) {
                                    System.out.println("Enter the hotels name, or part of name;" +
                                            "get all list  - just press Enter;  +\n" +
                                            "9- previous menu; 0- finish work ");
                                    String choice4 = bufferedReader.readLine();
                                    if (choice4.equals("0")) break first;
                                    if (choice4.equals("9")) continue third;
                                    controller.findHotelByName(choice4.trim()).forEach(System.out::println);
                                    System.out.println("Do you want to continue the search?");
                                }
                            }

                            if (choice3.equals("3")) {
                                while (true) {
                                    System.out.println("Enter city`s name, or part of name;" +
                                            " get all list - just press Enter" +
                                            " 9- previous menu; 0- finish work ");
                                    String choice4 = bufferedReader.readLine();
                                    if (choice4.equals("0")) break first;
                                    if (choice4.equals("9")) continue third;
                                    controller.findHotelByCity(choice4.trim()).forEach(System.out::println);
                                    System.out.println("Do you want to continue the search??");
                                }
                            }

                            //cancel booking
                            if (choice3.equals("5")) {
                                while (true) {
                                    System.out.println("Booking rooms by you:");
                                    List<Room> booked = controller.bookedByUser(user);
                                    if (booked.size() == 0) continue third;
                                    booked.forEach(System.out::println);
                                    System.out.println("\nFor canceling reserve, enter rooms id and hotels id using symbol /," +
                                            "example: 555/777; 9- previous menu; 0- finish work");
                                    String choice4 = bufferedReader.readLine();
                                    if (choice4.equals("0")) break first;
                                    if (choice4.equals("9")) continue third;
                                    String s[] = choice4.split("/");
                                    if (s.length != 2) {
                                        System.out.println("Incorrect input id");
                                        continue;
                                    }
                                    try {
                                        long roomId = Long.parseLong(s[0].trim());
                                        long hotelId = Long.parseLong(s[1].trim());
                                        controller.cancelReserve(roomId, user.getId(), hotelId);
                                    } catch (NumberFormatException e) {
                                        System.out.println("Enter int value!");
                                    }
                                }
                            }

                            //search rooms
                            if (choice3.equals("1")) {
                                second:
                                while (true) {
                                    System.out.println("For searching rooms, enter parameters using symbol /, in format:" +
                                            "city/hotel`s name/count of persons/low price/up price, example: Lviv/Dnipro/2/200/500 \n" +
                                            "if parameter doesn`t matter - use symbol *. " +
                                            "Also: 9- previous menu, 0- exit from administrator mode ");
                                    String choice4 = bufferedReader.readLine();
                                    if (choice4.equals("0")) break first;
                                    if (choice4.equals("9")) continue third;
                                    String s[] = choice4.split("/");
                                    if (s.length != 5) {
                                        System.out.println("Некорректный ввод!");
                                        continue;
                                    }
                                    Map<String, String> map = new HashMap<>();
                                    map.put(CITY, s[0].trim());
                                    map.put(HOTEL_NAME, s[1].trim());
                                    map.put(PERSONS, s[2].trim());
                                    map.put(MIN_PRICE, s[3].trim());
                                    map.put(MAX_PRICE, s[4].trim());
                                    List<Room> rooms = controller.findRoom(map);
                                    if (rooms.size() == 0) {
                                        System.out.println("Haven`t any result. Retry with another parameters."
                                                + "\nDo you want to retry search?");
                                        continue;
                                    }
                                    rooms.forEach(System.out::println);
                                    System.out.println();
                                    forth:
                                    while (true) {
                                        System.out.println("1- retry search, 2- booking, 9- main menu, 0- exit");
                                        String choice5 = bufferedReader.readLine();
                                        if (choice5.equals("0")) break first;
                                        if (choice5.equals("9")) continue third;
                                        if (choice5.equals("1")) break;
                                        if (choice5.equals("2")) {
                                            while (true) {
                                                System.out.println("For booking enter rooms id and hotels id using /," +
                                                        "example: 555/777, 9- previous menu; 0- finish work");
                                                String choice6 = bufferedReader.readLine();
                                                if (choice6.equals("0")) break first;
                                                if (choice6.equals("9")) continue third;
                                                String s1[] = choice6.split("/");
                                                if (s1.length != 2) {
                                                    System.out.println("Incorrect id");
                                                    continue;
                                                }
                                                try {
                                                    long roomId = Long.parseLong(s1[0].trim());
                                                    long hotelId = Long.parseLong(s1[1].trim());
                                                    if (controller.bookRoom(roomId, user.getId(), hotelId))
                                                        continue forth;
                                                } catch (NumberFormatException e) {
                                                    System.err.println("Use int value");
                                                }
                                            }
                                        }
                                        System.out.println("Incorrect input");
                                    }

                                }
                            }
                            System.out.println("Incorrect input");
                        }
                    }//the end of user with name
                }
            }
            //registration
            if (choice1.equals("5")) {
                while (true) {
                    System.out.println("Registration. Enter name and password using symbol /, example Peter/1111; " +
                            "or 1- back to main menu, 0- finish work");
                    String choice2 = bufferedReader.readLine();
                    if (choice2.length() == 1) {
                        if (choice2.equals("0")) break first;
                        if (choice2.equals("1")) continue first;
                    }
                    String nameAndPass[] = choice2.split("/");
                    if (nameAndPass.length != 2) {
                        System.out.println("Incorrect input");
                        continue;
                    }
                    boolean b = controller.registerUser(new User(0, nameAndPass[0].trim(), nameAndPass[1].trim()));
                    if (b) continue first;
                }
            }
            System.out.println("Incorrect value");
        }
        System.out.println("Good luck");
        bufferedReader.close();
    }
}
