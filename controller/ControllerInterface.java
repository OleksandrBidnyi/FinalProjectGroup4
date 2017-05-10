package finalProject.controller;

import finalProject.entities.Hotel;
import finalProject.entities.Room;
import finalProject.entities.User;

import java.util.List;
import java.util.Map;

/**
 * Created by Aleksandr on 10.05.2017.
 */
public interface ControllerInterface {
    boolean registerUser(User newUser);

    User enter(String name, String pass);

    List<Room> bookedByUser(User user);

    List<Hotel> findHotelByName(String name);

    List<Hotel> findHotelByCity(String city);

    boolean bookRoom(long roomId, long userId, long hotelId);

    boolean cancelReserve(long roomId, long userId, long hotelId);

    List<Room> findRoom(Map<String, String> params);

    boolean editUser(User user);

    boolean removeUser(User user);

    boolean removeHotel(Hotel hotel);

    boolean removeRoom(Room room);

    Room findRoomById(long id);

    Hotel editHotel(Hotel hotel);

    Room editRoom(Room room);

    Hotel addHotel(Hotel hotel);

    Room addRoom(Room room);

    Hotel findHotelById(long id);

    List<User> findUsersByName(String name);

    List<Room> booked();


}
