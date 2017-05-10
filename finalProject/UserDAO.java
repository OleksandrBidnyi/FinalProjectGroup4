package finalProject;

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
public class UserDAO implements DAO<User> {
    private static UserDAO userDAO;
    private List<User> usersBase = new ArrayList<>();
    private File file;

    //Singletone
    public static UserDAO getUserDAO() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }


    //Create file for storing users
    private UserDAO() {
        try {
            file = new File("src/userBase.txt");
            if (file.createNewFile())
                System.out.println("File of userBase has created");

        } catch (IOException e) {
            System.err.println("File of userBase hasn`t created");
        }

        //managing collection of users from file, while creating ex. of finalProject.UserDAO()
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (br.readLine() == null)
                System.out.println("finalProject.User Base is empty!");
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

    //method writes information to userBase.txt

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

    //method check valid of data
    private boolean validInspect(User user) {
        boolean notValid = false;
        if ((user.getName() == null) || user.getName().equals("") ||
                (user.getPassword() == null) || (user.getPassword().equals(""))) {
            notValid = true;
        }
        return notValid;
    }

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
        System.out.println("finalProject.User has been added to dataBase");
        return true;
    }

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
            System.err.println("finalProject.User with ID %d is absent in dataBase");
            return false;
        } catch (NullPointerException e) {
            System.err.println("Add correct information about user!");
            return false;
        }
        return true;
    }

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

    @Override
    public List<User> getBase() {
        return usersBase;
    }
}
