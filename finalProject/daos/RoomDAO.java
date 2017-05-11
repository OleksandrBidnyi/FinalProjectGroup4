package finalProject.daos;

import finalProject.entities.Hotel;
import finalProject.entities.Room;
import finalProject.entities.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Created by Aleksandr on 26.04.2017.
 */
/**
 * RoomDAO is a realization of interface DAO with his own fields and methods that can add,
 * remove and edit Room in Room's DataBase.
 */
public class RoomDAO implements DAO<Room> {
    private static RoomDAO roomDAO;
    private List<Room> roomList = new ArrayList<>();
    private File file;

    //Singleton
     /**
     * getRoomDAO method provides access to get rooms from Room's DataBase.
     * @return room from Room's DataBase.
     */
    public static RoomDAO getRoomDAO() {
        if (roomDAO == null) {
            roomDAO = new RoomDAO();
        }
        return roomDAO;
    }

     /**
     * RoomDAO method creates file for saving rooms in Room's DataBase.
     */    private RoomDAO() {
        try {
            file = new File("src/finalProject/dataBase/roomBase.txt");
            if (file.createNewFile()) {
                System.out.println("File has created!");
            }
        } catch (IOException e) {
            System.err.println("File of roomBase hasn`t created!");
        }

        HotelDAO hotelDAO = HotelDAO.getHotelDAO();
        UserDAO userDAO = UserDAO.getUserDAO();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (br.readLine() == null) {
                System.out.println("roomBase is empty!");
            } else {
                Stream<String> streamFromFiles = Files.lines(Paths.get(file.getAbsolutePath()));
                streamFromFiles.forEach(line -> {
                    String fields[] = line.split("@");
                    if (roomList.stream().anyMatch(room -> room.getId() == Long.parseLong(fields[0]))) {
                        throw new RuntimeException("roomBase is damaged");
                    }
                    if (fields.length != 5 && fields.length != 4) {
                        throw new RuntimeException("roomBase is damaged");
                    }
                    Hotel hotel = null;
                    User user = null;
                    for (int i = 0; i < hotelDAO.getBase().size(); i++) {
                        if (hotelDAO.getBase().get(i).getId() == Long.parseLong(fields[3])) {
                            hotel = hotelDAO.getBase().get(i);
                            break;
                        }
                    }
                    if (fields.length == 5) {
                        for (int i = 0; i < userDAO.getBase().size(); i++) {
                            if (userDAO.getBase().get(i).getId() == Long.parseLong(fields[4])) {
                                user = userDAO.getBase().get(i);
                                break;
                            }
                        }
                    }
                    if (hotel != null) {
                        roomList.add(new Room(Long.parseLong(fields[0]), Integer.parseInt(fields[1]),
                                Integer.parseInt(fields[2]), hotel, user));
                    }
                });
                streamFromFiles.close();
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("roomBase is damaged!");
        }
    }

     /**
     * ValidInspect method checks the availability of the room in the database.
     */
    private boolean validInspect(Room room) {
        boolean notValid = false;
        if ((room.getId() == 0) || (room.getPersons() == 0) || (room.getHotel() == null)) {
            notValid = true;
        }
        return notValid;
    }

    public File getFile() {
        return file;
    }

     /**
     * writeToFile method writes data in a Room's DataBase.
     * @param file - file to write.
     * @param list - list of rooms from Room's DataBase.
     */
    public boolean writerToFile(File file, List<Room> list) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            for (Room room : list) {

                bufferedWriter.write(room.getId() + "@");
                bufferedWriter.write(room.getPrice() + "@");
                bufferedWriter.write(room.getPersons() + "@");
                bufferedWriter.write(String.valueOf(room.getHotel().getId()));
                if (room.getUserReserved() != null) {
                    bufferedWriter.write("@" + room.getUserReserved().getId());
                }
                bufferedWriter.write(System.lineSeparator());
            }
            bufferedWriter.flush();
        } catch (IOException e) {
            System.err.println("Failed to save data to database!");
            return false;
        }
        return true;
    }

     /**
     * Add method adds room to Room's DataBase.
     * @param room - room to be added.
     */
    @Override
    public boolean add(Room room) {
        if (!HotelDAO.getHotelDAO().getBase().contains(room.getHotel())) {
            System.out.println("This hotel is absent in the base! Firstly add hotel");
            return false;
        }
        try {
            if (validInspect(room)) {
                System.out.println("The ID field, count of persons and hotel should be filled!");
                return false;
            } else {
                if (roomList.stream().anyMatch(u -> u.getId() == room.getId())) {
                    System.out.println("This room with same ID is already exist");
                    return false;
                } else {
                    roomList.add(room);
                    writerToFile(file, roomList);
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Add correct information to file!");
            return false;
        }
        return true;
    }
    
     /**
     * edit method allows to adjust data in Room's DataBase.
     * @param room - room to edit from Room's DataBase.
     */
    @Override
    public boolean edit(Room room) {

        try {
            Room roomToEdit = roomList.stream().filter(r ->
                    r.getId() == room.getId()).findAny().get();
            if (validInspect(room)) {
                System.out.println("The ID field, count of persons and hotel should be filled!");
                return false;
            } else {
                roomToEdit.setPrice(room.getPrice());
                roomToEdit.setPersons(room.getPersons());
                roomToEdit.setHotel(room.getHotel());
                roomToEdit.setUserReserved(room.getUserReserved());
                writerToFile(file, roomList);
            }
        } catch (NoSuchElementException e) {
            System.err.printf("The finalProject.entities.Room with this ID %d isn`t in base." + "\n", room.getId());
            return false;
        } catch (NullPointerException e) {
            System.err.println("Add correct information to file!");
            return false;
        }
        return true;
    }

      /**
     * Remove method allows to remove room from Room's DataBase.
     * @param room - room to remove from Room's DataBase.
     */
    @Override
    public boolean remove(Room room) {

        if (!roomList.contains(room)) {
            System.out.println("This room is absent in dataBase!");
            return false;
        } else {
            try {
                if (roomList.stream().anyMatch(room1 ->
                        room1.getId() == room.getId())) {
                    roomList.remove(room);
                    writerToFile(file, roomList);
                }
            } catch (NullPointerException e) {
                System.err.println("Base is empty or this room is absent in dataBase");
                return false;
            }
            return true;
        }
    }

     /**
     * getBase method allows to return list of Rooms from Room's DataBase
     */
    @Override
    public List<Room> getBase() {
        return roomList;
    }

}
