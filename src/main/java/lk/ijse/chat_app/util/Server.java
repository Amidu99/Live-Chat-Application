package lk.ijse.chat_app.util;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static ArrayList<UserManager> users = new ArrayList<UserManager>();

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(7777);
        Socket accept;
        while (true) {
            accept = serverSocket.accept();
            UserManager userManager = new UserManager(accept, users);
            users.add(userManager);
            userManager.start();
        }
    }
}