package chat.room.lakshya.server;

import chat.room.lakshya.shared.security.RSA;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    public static void main(String[] args) {
        RSA decryptor = new RSA();
        final int PORT=9999;
        //PORT = Integer.parseInt(args[1]);

        ServerSocket chatServer = null;
        try {
            chatServer = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, ClientConnection> clients = new ConcurrentHashMap<>();
        Authenticator auth = new Authenticator("C:\\Users\\asus\\Desktop\\Main projects\\java-chat-master\\src\\main\\java\\chat\\room\\lakshya\\server\\AuthTest.txt");

        new Thread(new ServerConsole(clients)).start();

        while (chatServer != null) {
            try {
                Socket clientSocket = chatServer.accept();
                ClientConnectionRunnable clientConnectionInit = new ClientConnectionRunnable(clientSocket, clients, decryptor, auth);
                new Thread(clientConnectionInit).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}