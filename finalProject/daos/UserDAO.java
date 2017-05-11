package finalProject.daos;

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
 * UserDAO is a realization of interface DAO with his own fields and methods that can add,
 * remove and edit User in User's DataBase.
 */
public class UserDAO implements DAO<User> {
    private static UserDAO userDAO;
    private List<User> usersBase = new ArrayList<>();
    private File file;

    //Singletone
    /**
     * getUserDAO method provides access to get users from User's DataBase.
     * @return user from User's DataBase.
     */
    public static UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }


     /**
     * UserDAO method creates file for saving users in User's DataBase.
     */    private UserDAO() {
        try {
            file = new File("src/finalProject/dataBase/userBase.txt");
            if (file.createNewFile())
                System.out.println("File of userBase has created");

        } catch (IOException e) {
            System.err.println("File of userBase hasn`t created");
        }

        //managing collection of users from file, while creating ex. of finalProject.daos.UserDAO()
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (br.readLine() == null)
                System.out.println("finalProject.entities.User Base is empty!");
            else {
                Stream<String> streamFromFiles = Files.lines(Paths.get(file.getAbsolutePath()));
                streamFromFiles.forEach(line -> {
                    String fields[] = line.split(" ");

                    if (fields.length != 3) throw new RuntimeException("Base of users is Damaged!");
                    usersBase.add(new User(Long.parseLong(fields[0]), fields[1], fields[2]));
                });
            }

        } catch (IOException | NumberFormatException e) {
            throw new RuntimeException("Base of users is Damaged");
        }

    }

     /**
     * writeToFile method writes data in a User's DataBase.
     * @param file - file to write information to userBase.txt
     * @param list - list of users from User's DataBase.
     */
    private boolean writeToFile(File file, List<User> list) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file)))) {

            for (User user : list) {
                bufferedWriter.write(user.getId() + " ");
                bufferedWriter.write(user.getName() + " ");
                bufferedWriter.write(user.getPassword() + "\n");
            }
        } catch (IOException e) {
            System.err.println("Failed to save data to database!");
            return false;
        }
        return true;
    }

     /**
     * ValidInspect method checks the availability of the user in the database.
     */
    private boolean validInspect(User user) {
        boolean notValid = false;
        if ((user.getName() == null) || user.getName().equals("") ||
                (user.getPassword() == null) || (user.getPassword().equals(""))) {
            notValid = true;
        }
        return notValid;
    }

    /**
     * Add method adds user to User's DataBase.
     * @param user - user to be added.
     */
    @Override
    public boolean add(User user) {
        try {
            if (validInspect(user)) {
                System.out.println("Fields have to be filled!");
                return false;
            } else {
                if (usersBase.stream().anyMatch(u -> (u.getId() ==
                        user.getId()) || u.getName().toLowerCase().
                        equals(user.getName().toLowerCase()))) {
                    System.out.println("This user already exist");
                    return false;
                } else {
                    usersBase.add(user);
                    writeToFile(file, usersBase);
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Add correct information to file!");
            return false;
        }
        System.out.println("finalProject.entities.User has been added to dataBase");
        return true;
    }

     /**
     * edit method allows to adjust data in User's DataBase.
     * @param user - user to edit from User's DataBase.
     */
    @Override
    public boolean edit(User user) {
        try {
            User userToEdit = usersBase.stream().filter(
                    u -> u.getId() == user.getId()).findAny().get();
            if (validInspect(user)) {
                System.err.println("Fields name and password have to be filled!");
                return false;
            } else {
                userToEdit.setName(user.getName());
                userToEdit.setPassword(user.getPassword());
                writeToFile(file, usersBase);
            }
        } catch (NoSuchElementException e) {
            System.err.println("finalProject.entities.User with ID %d is absent in dataBase");
            return false;
        } catch (NullPointerException e) {
            System.err.println("Add correct information about user!");
            return false;
        }
        return true;
    }

    /**
     * Remove method allows to remove user from User's DataBase.
     * @param user - user to remove from User's DataBase.
     */
    @Override
    public boolean remove(User user) {
        if (usersBase.contains(user)) {
            try {
                RoomDAO roomDAO = RoomDAO.getRoomDAO();
                roomDAO.getBase().forEach(r -> {
                    if (r.getUserReserved() != null && r.getUserReserved().
                            equals(user)) {
                        r.setUserReserved(null);
                    }
                });
                roomDAO.writerToFile(roomDAO.getFile(), roomDAO.getBase());
                usersBase.remove(user);
                writeToFile(file, usersBase);
                return true;
            } catch (NullPointerException e) {
                System.err.println("DataBase is empty!");
            }
        } else System.out.println("This user is absent in dataBase!");
        return false;
    }

     /**
     * getBase method allows to return list of Users from User's DataBase
     */
    @Override
    public List<User> getBase() {
        return usersBase;
    }
}
