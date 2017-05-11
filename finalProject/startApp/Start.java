package finalProject.startApp;

import finalProject.console.UserVsAdmin;

import java.io.IOException;

/**
 * Created by Aleksandr on 10.05.2017.
 */
/**
 * This section provides log on for the administrator and the user
 */
public class Start {
    public static void main(String[] args) throws IOException {
        UserVsAdmin userVsAdmin = new UserVsAdmin();
        userVsAdmin.console();
    }
}
