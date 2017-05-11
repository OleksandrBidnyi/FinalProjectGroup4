package finalProject.startApp;

import finalProject.console.UserVsAdmin;

import java.io.IOException;

/**
 * This section provides log on for the administrator and the user
 * To log in as administrator you must to use following fields: admin/0000.
 */
public class Start {
    public static void main(String[] args) throws IOException {
        UserVsAdmin userVsAdmin = new UserVsAdmin();
        userVsAdmin.console();
    }
}
