package finalProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static finalProject.Controller.*;

/**
 * Created by Aleksandr on 02.05.2017.
 */
public class Admin {
    public static void root() throws IOException {
        Controller controller = new Controller();
        System.out.println("You entered as administrator.");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        first:
        while (true) {
            System.out.println("1- find user by name," +
                    " 2 - users activity(booked rooms," +
                    " 3- delete users," +
                    "\n4- find hotels by name," +
                    " 5 - find hotels by city," +
                    " 6- edit hotels," +
                    " 7- add hotels," +
                    " 8- delete hotels," +
                    "\n9 - find rooms," +
                    " 10- edit rooms," +
                    " 11- add rooms," +
                    " 12- delete rooms," +
                    "\n0 - exit administrator mode");
            String choice1 = br.readLine();
            if (choice1.equals("0")) break;

            //users activity(booked rooms)
            if (choice1.equals("2")) {
                System.out.println("Users activity. Booking rooms:");
                List<Room> booked = controller.booked();
                if (booked.size() == 0) System.out.println("hasn`t booking rooms");
                booked.stream().forEach(r -> System.out.println(r + "has been booked by " + r.getUserReserved()));
                System.out.println();
                continue first;
            }

            //finding users by name
            if (choice1.equals("1")) {
                while (true) {
                    System.out.println("Searching users. Enter users name, or part of name;" +
                            "or 9 - previous menu, 0 - exit from administrator mode");
                    String choice2 = br.readLine();
                    if (choice2.equals("0")) break first;
                    if (choice2.equals("9")) continue first;
                    controller.findUsersByName(choice2.trim()).forEach(System.out::println);
                    System.out.println("Continue search");
                }
            }

            // finding hotels by name
            if (choice1.equals("4")) {
                while (true) {
                    System.out.println("Enter name of hotel or part of name;" +
                            "or 9- previous menu, 0- exit from administrator mode");
                    String choice2 = br.readLine();
                    if (choice2.equals("0")) break first;
                    if (choice2.equals("9")) continue first;
                    controller.findHotelByName(choice2.trim()).forEach(System.out::println);
                    System.out.println("Continue search");
                }
            }

            //finding hotel by city
            if (choice1.equals("5")) {
                while (true) {
                    System.out.println("Enter name of city, or part of city`s name;" +
                            "or 9- previous menu, 0- exit from administrator mode");
                    String choice2 = br.readLine();
                    if (choice2.equals("0")) break first;
                    if (choice2.equals("9")) continue first;
                    controller.findHotelByCity(choice2.trim()).forEach(System.out::println);
                    System.out.println("Continue search");
                }
            }

            //finding rooms
            if (choice1.equals("9")) {
                while (true) {
                    System.out.println("For searching rooms you should enter parameters split they by symbol / in such format:" +
                            "city/hotel`s name/count of persons/low price/up price, example: Lviv/Dnipro/2/200/500 \n" +
                            "if parameter doesn`t matter - use symbol *. " +
                            "Also: 9- previous menu, 0- exit from administrator mode ");
                    String choice2 = br.readLine();
                    if (choice2.equals("0")) break first;
                    if (choice2.equals("9")) continue first;
                    String s[] = choice2.split("/");
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
                    System.out.println("Retry search?");
                }
            }
            //delete users
            if (choice1.equals("3")) {
                while (true) {
                    System.out.println("For deleting user, enter his name or id;" +
                            "or 9- previous menu, 0- exit from administrator mode");
                    String choice2 = br.readLine();
                    if (choice2.equals("0")) break first;
                    if (choice2.equals("9")) continue first;
                    long id;
                    try {
                        id = Long.parseLong(choice2.trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Incorrect input! Put int value");
                        continue;
                    }
                    User user = controller.findUserById(id);
                    if (user == null) {
                        System.out.println("User with this Id hasn`t been found.");
                        continue;
                    }
                    System.out.println("Are you sure you want to delete user: " + user + "?" +
                            "Put 1 for accept, or any button for cancel");
                    String ch = br.readLine();
                    if (ch.equals("1")) {
                        boolean b = controller.removeUser(user);
                        if (b) System.out.println("User " + user + "has been deleted.");
                        else System.out.println("Something going wrong");
                    }
                    System.out.println("Are we keeping to delete users?");
                }
            }

            //editing hotels
            if (choice1.equals("6")) {
                while (true) {
                    System.out.println("For editing hotel enter id;" +
                            "or 9- previous menu, 0- exit from administrator mode");
                    String choice2 = br.readLine();
                    if (choice2.equals("0")) break first;
                    if (choice2.equals("9")) continue first;
                    long id;
                    try {
                        id = Long.parseLong(choice2.trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Incorrect input! Put int value");
                        continue;
                    }
                    Hotel hotel = controller.findHotelById(id);
                    if (hotel == null) continue;
                    while (true) {
                        System.out.println("Editing hotrl " + hotel +
                                "\n Put new name for hotel and city using symbol /, example: Hilton/Kiev; 0 - cancel");

                        String choice3 = br.readLine();
                        if (choice3.equals("0")) break;
                        String s[] = choice3.split("/");
                        if (s.length != 2) {
                            System.out.println("Incorrect input!");
                            continue;
                        }
                        Hotel newHotel = controller.editHotel(new Hotel(id, s[0].trim(), s[1].trim()));
                        if (newHotel != null) {
                            System.out.println("Hotel has been edited: " + newHotel + System.lineSeparator());
                        } else System.out.println("Something going wrong");
                    }
                    System.out.println("Are we keeping to edit hotels?");
                }
            }

            //edit rooms
            if (choice1.equals("10")) {
                while (true) {
                    System.out.println("For editing room enter id; " +
                            "or 9- previous menu, 0- exit from administrator mode");
                    String choice2 = br.readLine();
                    if (choice2.equals("0")) break first;
                    if (choice2.equals("9")) continue first;
                    long id;
                    try {
                        id = Long.parseLong(choice2.trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Incorrect input! Put int value" + System.lineSeparator());
                        continue;
                    }
                    Room room = controller.findRoomById(id);
                    if (room == null) {
                        System.out.println("Room with id: " + id + " hasn`t been found." + System.lineSeparator());
                        continue;
                    }
                    while (true) {
                        System.out.println("Editing room " + room + "\nEnter new data, price and count of persons using symbol /," +
                                "example 400/3;  or 0 - cancel");
                        String choice3 = br.readLine();
                        if (choice3.equals("0")) break;
                        String s[] = choice3.split("/");
                        if (s.length != 2) {
                            System.out.println("Incorrect input!");
                            continue;
                        }
                        int persons;
                        int price;
                        try {
                            persons = Integer.parseInt(s[1].trim());
                            price = Integer.parseInt(s[0].trim());
                        } catch (NumberFormatException e) {
                            System.err.println("Incorrect input! Put int value");
                            continue;
                        }
                        Room newRoom = controller.editRoom(new Room(id, price, persons, room.getHotel(), room.getUserReserved()));
                        if (newRoom != null) {
                            System.out.println("Room has been edited: " + room + System.lineSeparator());
                            break;
                        }
                        System.out.println("Something going wrong");
                    }
                    System.out.println("Are we keeping to edit rooms?");
                }
            }
            //add hotels
            if (choice1.equals("7")) {
                while (true) {
                    System.out.println("For adding hotel enter name and city of hotel: " +
                            "example: Hilton/Kiev" +
                            "\n or 9- previous menu, 0- exit from administrator mode");
                    String choice2 = br.readLine();
                    if (choice2.equals("0")) break first;
                    if (choice2.equals("9")) continue first;
                    String s[] = choice2.split("/");
                    if (s.length != 2) {
                        System.out.println("Incorrect input!");
                        continue;
                    }
                    Hotel hotel = controller.addHotel(new Hotel(0, s[0].trim(), s[1].trim()));
                    if (hotel != null) System.out.println("Hotel has been added: " + hotel);
                    System.out.println("Are we keeping to add hotels?");
                }
            }

            //add rooms
            if (choice1.equals("11")) {
                while (true) {
                    System.out.println("For adding room enter data in format:" +
                            "hotels id/count of persons/price, example 555/3/250" +
                            "\nor 9- previous menu, 0- exit from administrator mode");
                    String choice2 = br.readLine();
                    if (choice2.equals("0")) break first;
                    if (choice2.equals("9")) continue first;
                    String s[] = choice2.split("/");
                    if (s.length != 3) {
                        System.out.println("Incorrect input");
                        continue;
                    }
                    long hotelId;
                    int persons;
                    int price;
                    try {
                        hotelId = Long.parseLong(s[0].trim());
                        persons = Integer.parseInt(s[1].trim());
                        price = Integer.parseInt(s[2].trim());
                    } catch (NumberFormatException e) {
                        System.err.println("Incorrect input! Put int value");
                        continue;
                    }
                    Hotel hotel = controller.findHotelById(hotelId);
                    if (hotel == null) continue;
                    Room room = controller.addRoom(new Room(0,price,persons,hotel));
                    if (room!=null) System.out.println("Room has been added: " + room);
                    System.out.println("Are we keeping to add rooms?");
                }
            }


            System.out.println("Incorrect input!\n");
        }
    }
}
