package finalProject.startApp;

import finalProject.console.UserVsAdmin;

import java.io.IOException;

/**
 * Created by Aleksandr on 10.05.2017.
 */
public class Start {
    public static void main(String[] args) throws IOException {
        UserVsAdmin userVsAdmin = new UserVsAdmin();
        userVsAdmin.console();
    }
}
