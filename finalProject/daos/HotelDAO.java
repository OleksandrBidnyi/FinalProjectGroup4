package finalProject.daos;

import finalProject.entities.Hotel;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Created by Aleksandr on 28.04.2017.
 */

/**
 * HotelDAO it's a realization of interface DAO with his own fields and methods that can add,
 * remove and edit Hotel in Hotel's DataBase.
 */

public class HotelDAO implements DAO<Hotel> {

    private static HotelDAO hotelDAO;
    private List<Hotel> hotelList = new ArrayList<>();
    private File file;

       /**
     * getHotelDAO method provides access to get hotels from Hotel's DataBase.
     * @return hotel from Hotel's DataBase.
     */
    public static HotelDAO getHotelDAO() {
        if (hotelDAO == null) {
            hotelDAO = new HotelDAO();
        }
        return hotelDAO;
    }

         /**
     * HotelDAO method creates file for saving hotels in Hotel's DataBase.
     */
    private HotelDAO() {
        try {
            file = new File("src/finalProject/dataBase/hotelBase.txt");
            if (file.createNewFile()) {
                System.out.println("File has created!");
            }
        } catch (IOException e) {
            System.err.println("File of hotelBase hasn`t created!");
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (br.readLine() == null) {
                System.out.println("hotelBase is empty!");
            } else {
                Stream<String> streamFromFiles = Files.lines(Paths.get(file.getAbsolutePath()));
                streamFromFiles.forEach(line -> {
                    String fields[] = line.split("@");
                    if (fields.length != 3) {
                        throw new RuntimeException("hotelBase is damaged");
                    }
                    if (hotelList.stream().anyMatch(hotel ->
                            hotel.getId() == Long.parseLong(fields[0]))) {
                        throw new RuntimeException("hotelBase is damaged");
                    }
                    hotelList.add(new Hotel(Long.parseLong(fields[0]), fields[1], fields[2]));
                });
                streamFromFiles.close();
            }
        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("hotelBase is damaged!");
        }
    }

     /**
     * ValidInspect method checks the availability of the hotel in the database.
     */
    private boolean validInspect(Hotel hotel) {
        boolean notValid = false;
        if ((hotel.getId() == 0) || (hotel.getCity() == null || hotel.getCity().equals(""))
                || (hotel.getName() == null || hotel.getName().equals(""))) {
            notValid = true;
        }
        return notValid;
    }

    /**
     * Add method adds hotel to Hotel's DataBase.
     * @param hotel - hotel to be added.
     */
    @Override
    public boolean add(Hotel hotel) {
        try {
            if (validInspect(hotel)) {
                System.out.println("Field ID, names and city should be filled!");
                return false;
            } else {
                if (hotelList.stream().anyMatch(hotelFromBase ->
                        hotelFromBase.getId() == hotel.getId())) {
                    System.out.println("finalProject.entities.Hotel with same ID has been already exist");
                    return false;
                }
                if (hotelList.stream().anyMatch(hotel1 ->
                        (hotel1.getCity().toLowerCase().trim().equals(hotel.getCity().toLowerCase().trim())
                                && hotel1.getName().toLowerCase().trim().equals(hotel.getName().toLowerCase().trim())))) {
                    System.out.println("finalProject.entities.Hotel has been added, but it`s had been already exist!");
                }
                hotelList.add(hotel);
                writeToFile(file, hotelList);
            }
        } catch (NullPointerException e) {
            System.err.println("Add correct information to file!");
            return false;
        }
        return true;
    }

   /**
     * writeToFile method writes data in a Hotel's DataBase.
     * @param file - file to write.
     * @param list - list of hotels from Hotel's DataBase.
     */
    private boolean writeToFile(File file, List<Hotel> list) {
        StringBuilder stringBuilder = new StringBuilder();
        list.stream().forEach(hotelFromList ->
                stringBuilder.append(hotelFromList.getId() + "@" + hotelFromList.getName()
                        + "@" + hotelFromList.getCity() + "\n"));
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.append(stringBuilder);
            bufferedWriter.flush();
        } catch (IOException e) {
            System.err.println("Failed to save data to database!");
        }
        return true;
    }

       /**
     * edit method allows to adjust data in Hotel's DataBase.
     * @param hotel - hotel to edit from Hotel's DataBase.
     */
    @Override
    public boolean edit(Hotel hotel) {
        try {
            if (validInspect(hotel)) {
                System.out.println("The ID field, name of hotel and city should be filled!");
                return false;
            } else {
                Hotel oldHotel = hotelDAO.getBase().stream().filter(
                        hotel1 -> hotel.getId() == hotel.getId()).findAny().get();

                oldHotel.setCity(hotel.getCity());
                oldHotel.setName(hotel.getName());
                writeToFile(file, hotelList);
            }
        } catch (NoSuchElementException e) {
            System.err.println("finalProject.entities.Hotel with ID %d is absent in the hotelBase");
        } catch (NullPointerException e) {
            System.err.println("Add correct information to file!");
            return false;
        }
        return true;
    }

       /**
     * Remove method allows to remove hotel from Hotel's DataBase.
     * @param hotel - hotel to remove from Hotel's DataBase.
     */
    @Override
    public boolean remove(Hotel hotel) {
        if (!hotelList.contains(hotel)) {
            System.out.println("This hotel is absent in dataBase");
            return false;
        } else {
            try {
                if (hotelList.stream().anyMatch(h ->
                        h.getId() == hotel.getId())) {
                    RoomDAO roomDAO = RoomDAO.getRoomDAO();
                    for (int i = roomDAO.getBase().size() - 1; i >= 0; i--) {
                        if (roomDAO.getBase().get(i).getId() == hotel.getId()) {
                            roomDAO.remove(roomDAO.getBase().get(i));
                        }
                    }
                    hotelList.remove(hotel);
                    writeToFile(file, hotelList);
                }
            } catch (NullPointerException e) {
                System.err.println("Base is empty or this hotel is absent in dataBase");
                return false;
            }
            return true;
        }
    }

        /**
     * getBase method allows to return list of Hotels from Hotel's DataBase
     */
    @Override
    public List<Hotel> getBase() {
        return hotelList;
    }
}
