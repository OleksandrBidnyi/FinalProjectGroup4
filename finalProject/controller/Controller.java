package finalProject.controller;

import finalProject.daos.HotelDAO;
import finalProject.daos.RoomDAO;
import finalProject.daos.UserDAO;
import finalProject.entities.Hotel;
import finalProject.entities.Room;
import finalProject.entities.User;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;



/**
 * Created by Aleksandr on 29.04.2017.
 */
/**
 * Class with internal functionality.
 */
public class Controller implements ControllerInterface {
    public static final String CITY = "city";
    public static final String HOTEL_NAME = "hotelName";
    public static final String PERSONS = "persons";
    public static final String MAX_PRICE = "maxPrice";
    public static final String MIN_PRICE = "minPrice";

     /**
     * registerUser method for registration new user in User's DataBase.
     * @param newUser - new user for registration.
     */   
    public boolean registerUser(User newUser) {
        long id;
        if (UserDAO.getUserDAO().getBase().size() != 0) {
            User userWithTheBigestId = UserDAO.getUserDAO().getBase().stream().max((u1, u2) ->
            {return Long.compare(u1.getId(), u2.getId());
            }).get();
            id = userWithTheBigestId.getId() + 1;
        } else id = 100;
        newUser = new User(id, newUser.getName(), newUser.getPassword());
        boolean b = UserDAO.getUserDAO().add(newUser);
        return b;
    }

     /**
     * enter method is for entering User to system.
     * @param name - name for enter to the system.
     * @param pass - password for enter to the system.
     * @return - returns user that passed into system(or not passed).
     */
    public User enter(String name, String pass) {
        if (UserDAO.getUserDAO().getBase().size() == 0) {
            System.out.println("User hasn`t been found");
            return null;
        }
        User user = null;
        try {
            user = UserDAO.getUserDAO().getBase().stream().filter(u ->
                    (u.getName().toLowerCase().equals(name)) && u.getPassword().equals(pass)).
                    findAny().get();
        } catch (Exception e) {
            System.out.println("Wrong user`s name or password");
            return null;
        }
        System.out.println("You enter as: " + name);
        return user;
    }

     /**
     * bookedUser method is to find reserved rooms by user.
     * @param user - user that reserved room.
     * @return - list of rooms that were reserved.
     */   
    public List<Room> bookedByUser(User user) {
        List<Room> list = RoomDAO.getRoomDAO().getBase().stream().filter(room ->
                room.getUserReserved() != null).filter(room -> room.getUserReserved().
                equals(user)).collect(Collectors.toList());
        if (list.size() == 0) {
            System.out.println("haven`t booking rooms");
        }
        return list;
    }

      /**
     * findHotelByName method make a search Hotels by their names.
     * @param name - Hotel's name.
     * @return - list of Hotels, that was searching.
     */
    public List<Hotel> findHotelByName(String name) {
        List<Hotel> list = HotelDAO.getHotelDAO().getBase().stream().filter(hotel ->
                hotel.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        if (list.size() == 0) {
            System.out.println("Hotels hasn`t been found");
        }
        return list;
    }

     /**
     * findHotelByCity - method make a search Hotels by their cities.
     * @param city - City's name.
     * @return - list of Hotels, that was searching.
     */
    public List<Hotel> findHotelByCity(String city) {
        List<Hotel> list = HotelDAO.getHotelDAO().getBase().stream().filter(hotel ->
                hotel.getCity().toLowerCase().contains(city.toLowerCase())).collect(Collectors.toList());
        if (list.size() == 0) {
            System.out.println("Hotels hasn`t been found");
        }
        return list;
    }

     /**
     * bookRoom - method makes room reservation.
     * @param roomId - the id of a room.
     * @param userId - the id of user for who is registering a room.
     * @param hotelId - the id of Hotel for the room.
     */
    public boolean bookRoom(long roomId, long userId, long hotelId) {
        try {
            User userToRegister = UserDAO.getUserDAO().getBase().stream().
                    filter(user -> user.getId() == userId).findAny().get();
            try {
                Hotel hotel = HotelDAO.getHotelDAO().getBase().stream().
                        filter(hotel1 -> hotel1.getId() == hotelId).findAny().get();
                try {
                    Room roomToReserve = hotel.getRooms().stream().filter(room ->
                            room.getId() == roomId).findAny().get();
                    if (roomToReserve.getUserReserved() == null) {
                        roomToReserve.setUserReserved(userToRegister);
                        System.out.printf("Number %s has been successful booked by user %s\n",
                                roomToReserve.toString(), userToRegister.toString() + System.lineSeparator());
                        RoomDAO.getRoomDAO().edit(roomToReserve);
                        return true;
                    } else {
                        System.out.println("The room can`t be booking!");
                    }
                } catch (NoSuchElementException | NullPointerException e) {
                    System.err.printf("Room with ID %d in hotel with ID %d hasn`t been found \n", roomId, hotelId);
                }
            } catch (NoSuchElementException | NullPointerException e) {
                System.err.printf("The hotel with ID %d is absent in base\n", hotelId);
            }
        } catch (NoSuchElementException e) {
            System.err.printf("The user with ID %d is absent in base. Register please!\n", userId);
        }
        return false;
    }

    /**
     * cancelReserve - method makes a cancel of registration a room for user.
     * @param roomId - the id of room.
     * @param userId - the id of user for who was registered a room.
     * @param hotelId -the id of room's Hotel.
     */
    public boolean cancelReserve(long roomId, long userId, long hotelId) {
        try {
            User userToRegister = UserDAO.getUserDAO().getBase().stream().
                    filter(user -> user.getId() == userId).findAny().get();
            try {
                Hotel hotel = HotelDAO.getHotelDAO().getBase().stream().
                        filter(hotel1 -> hotel1.getId() == hotelId).findAny().get();
                try {
                    Room roomToReserve = hotel.getRooms().stream().filter(room ->
                            room.getId() == roomId).findAny().get();
                    if ((roomToReserve.getUserReserved() != null) &&
                            (userToRegister.equals(roomToReserve.getUserReserved()))) {
                        roomToReserve.setUserReserved(null);
                        System.out.printf("Users %s reserve has been canceled, room %s.\n ",
                                userToRegister.toString(), roomToReserve.toString());
                        RoomDAO.getRoomDAO().edit(roomToReserve);
                        return true;
                    } else {
                        System.out.println("You hasn`t booked this room");
                    }
                } catch (NoSuchElementException | NullPointerException e) {
                    System.err.printf("finalProject.entities.Room with ID %d in hotel with ID %d hasn`t been found \n", roomId, hotelId);
                }
            } catch (NoSuchElementException | NullPointerException e) {
                System.err.printf("The hotel with ID %d is absent in base\n", hotelId);
            }
        } catch (NoSuchElementException e) {
            System.err.printf("The user with ID %d is absent in base. Register please!\n", userId);
        }
        return false;
    }

     /**
     * findRoom - method is founding a room by certain criteria.
     * @param params - these are the criteria for looking for a room.
     * @return - is returning collections of rooms, that were searching.
     */
    public List<Room> findRoom(Map<String, String> params) {
        List<Room> rooms = RoomDAO.getRoomDAO().getBase().stream().filter(room ->
                room.getUserReserved() == null).collect(Collectors.toList());
        String city = params.get(CITY);
        String hotelName = params.get(HOTEL_NAME);
        String personsString = params.get(PERSONS);
        String maxPriceStr = params.get(MAX_PRICE);
        String minPriceStr = params.get(MIN_PRICE);
        int maxPriceInt = Integer.MAX_VALUE;
        int minPriceInt = Integer.MIN_VALUE;

        if (!"*".equals(city)) rooms = rooms.stream().filter(room ->
                room.getHotel().getCity().toLowerCase().contains(city.toLowerCase())).collect(Collectors.toList());

        if (!"*".equals(hotelName)) rooms = rooms.stream().filter(room ->
                room.getHotel().getName().toLowerCase().contains(hotelName.toLowerCase())).collect(Collectors.toList());

        if (!"*".equals(personsString)) {
            try {
                rooms = rooms.stream().filter(room ->
                        room.getPersons() == Integer.parseInt(personsString)).collect(Collectors.toList());
            } catch (NumberFormatException e) {
                System.err.println("Error, when you entering count of users. You need int number or * symbol." +
                        "Thats why this filter hadn`t been using");
            }
        }

        if (!"*".equals(maxPriceStr)) {
            try {
                maxPriceInt = Integer.parseInt(maxPriceStr);
            } catch (NumberFormatException e) {
                System.err.println("Error, when you entering max price. You need int number or * symbol." +
                        "Thats why this filter hadn`t been using");
            }
        }

        if (!"*".equals(minPriceStr)) {
            try {
                minPriceInt = Integer.parseInt(minPriceStr);
            } catch (NumberFormatException e) {
                System.err.println("Error, when you entering min price. You need int number or * symbol." +
                        "Thats why this filter hadn`t been using");
            }
        }

        if (maxPriceInt < minPriceInt) {
            int temp = maxPriceInt;
            maxPriceInt = minPriceInt;
            minPriceInt = temp;
            System.out.println("Max price was less then Min price. We have to change their places");
        }
        final int maxPrice = maxPriceInt;
        final int minPrice = minPriceInt;

        return rooms.stream().filter(room -> (room.getPrice() <= maxPrice) &&
                (room.getPrice() >= minPrice)).collect(Collectors.toList());
    }

     /**
     * editUser - method to edit a data of user from User's DataBase.
     * @param user - user from User's DataBase.
     * @return - user what was edited.
     */
    public boolean editUser(User user) {
        return UserDAO.getUserDAO().edit(user);
    }

    /**
     * findUserById - method to find a User by he's id.
     * @param id - user's id.
     * @return - user that was searched.
     */
    public User findUserById(long id) {
        User user = null;
        try {
            user = UserDAO.getUserDAO().getBase().stream().filter(user1 ->
                    user1.getId() == id).findAny().get();
        } catch (NoSuchElementException e) {
        }
        return user;
    }

      /**
     * removeUser - method makes remove user from User's DataBase.
     * @param user - user to remove.
     * @return - user that was removed.
     */
    public boolean removeUser(User user) {
        return UserDAO.getUserDAO().remove(user);
    }

      /**
     * removeHotel = method makes remove hotel from Hotel's DataBase.
     * @param hotel - hotel to remove.
     * @return - hotel that was removed.
     */
    public boolean removeHotel(Hotel hotel) {
        return HotelDAO.getHotelDAO().remove(hotel);
    }

     /**
     * removeRoom - method makes remove room from Room's DataBase.
     * @param room - room to remove.
     * @return - room that was removed.
     */
    public boolean removeRoom(Room room) {
        return RoomDAO.getRoomDAO().remove(room);
    }

      /**
     * findRoomById - method to find a Room by it's id.
     * @param id - room's id.
     * @return - room that was searched.
     */
    public Room findRoomById(long id) {
        Room room = null;
        try {
            room=RoomDAO.getRoomDAO().getBase().stream().filter(room1 ->
            room1.getId()==id).findAny().get();
        }catch (NoSuchElementException e){}
        return room;
    }

     /**
     * editHotel - method to edit data of hotel from Hotel's DataBase.
     * @param hotel - hotel to edit.
     * @return - edited hotel.
     */
    public Hotel editHotel (Hotel hotel){
        if (HotelDAO.getHotelDAO().edit(hotel)){
            return hotel;
        }else {
            return null;
        }
    }

     /**
     * editRoom - method to edit data of room from Room's DataBase.
     * @param room - room to edit.
     * @return - edited room.
     */
    public Room editRoom(Room room){
        if (RoomDAO.getRoomDAO().edit(room)) return room;
        return null;
    }

     /**
     * addHotel - method add hotel to Hotel's DataBase.
     * @param hotel - hotel to add.
     * @return - hotel that was added.
     */
    public Hotel addHotel (Hotel hotel){
        long id;
        if (HotelDAO.getHotelDAO().getBase().size() != 0){
            Hotel hotelWithTheBiggestId = HotelDAO.getHotelDAO().getBase().stream().
                    max((r1,r2)->Long.compare(r1.getId(),r2.getId())).get();
            id = hotelWithTheBiggestId.getId() + 1;
        }
        else  id = 200;
        hotel = new Hotel(id,hotel.getName(),hotel.getCity());

        if (HotelDAO.getHotelDAO().add(hotel))
            return hotel;
        return null;
    }

      /**
     * addRoom - method add room to Room's DataBase.
     * @param room - room to add.
     * @return - room that was added.
     */
    public Room addRoom(Room room){
        long id;
        if(RoomDAO.getRoomDAO().getBase().size() != 0){
            Room roomWithTheBiggestId = RoomDAO.getRoomDAO().getBase().stream().max((r1, r2) ->
                    Long.compare(r1.getId(), r2.getId())).get();
            id = roomWithTheBiggestId.getId()+1;
        }
        else id=300;
        room = new Room(id,room.getPrice(),room.getPersons(),room.getHotel(),room.getUserReserved());
        if(RoomDAO.getRoomDAO().add(room)) return room;
        return null;
    }

      /**
     * findHotelById - method to find hotel by it's id.
     * @param id - hotel's id.
     * @return - hotel that was searched.
     */
    public Hotel findHotelById(long id){
        Hotel hotel = null;
        try{
            hotel= HotelDAO.getHotelDAO().getBase().stream().filter(h->h.getId()==id).findAny().get();

        }catch (NoSuchElementException e){
            System.err.println("The hotel with this ID hasn`t been found");
        }
        return  hotel;
    }

      /**
     * findUsersByName - method to find users from User's DataBase by their names.
     * @param name - user's searching name.
     * @return - list of users, that were searching for.
     */
    public List<User> findUsersByName (String name){
        List <User> list = UserDAO.getUserDAO().getBase().stream().filter(user ->
        user.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        if(list.size() == 0)
            System.out.println("finalProject.entities.User hasn`t been found");
        return list;
    }

    /**
     * booked - method that shows booked rooms from Room's DataBase.
     * @return - collections of rooms.
     */
    public List<Room> booked(){
        return RoomDAO.getRoomDAO().getBase().stream().filter(user->
        user.getUserReserved()!=null).collect(Collectors.toList());
    }
}
