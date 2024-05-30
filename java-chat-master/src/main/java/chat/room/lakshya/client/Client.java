package chat.room.lakshya.client;

import chat.room.lakshya.shared.LoginResponse;
import chat.room.lakshya.shared.UserConsoleReader;
import chat.room.lakshya.shared.UserReader;
import chat.room.lakshya.shared.UserSystemInReader;
import chat.room.lakshya.shared.security.RSA;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("127.0.0.1",9999);

            RSA decryptor = new RSA();
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            writer.println(decryptor.getE());
            writer.println(decryptor.getN());
            writer.flush();

            ResponsePrinterLoop responsePrinterLoop = new ResponsePrinterLoop(clientSocket, decryptor);
            BigInteger e = new BigInteger(responsePrinterLoop.readLine());
            BigInteger n = new BigInteger(responsePrinterLoop.readLine());
            RSA encryptor = new RSA(e, n);
            UserReader consoleReader = System.console() == null ? new UserSystemInReader() : new UserConsoleReader();

            LoginResponse usernameResponse;
            do {
                System.out.print("Nickname: ");
                String username = consoleReader.readLine();
                System.out.print("Password: ");
                String password = consoleReader.readPassword();
                writer.println(encryptor.encryptString(username));
                writer.println(encryptor.encryptString(password));
                writer.flush();
                usernameResponse = LoginResponse.valueOf(decryptor.decryptString(responsePrinterLoop.readLine()));
                System.out.println(usernameResponse.name());
            } while (usernameResponse != LoginResponse.LOGIN_ACCPETED && usernameResponse != LoginResponse.REGISTERED);
            new Thread(responsePrinterLoop).start();
            while (true) {
                String messageToServer = consoleReader.readLine();
                if (!messageToServer.equals("")) {
                    writer.println(encryptor.encryptString(messageToServer));
                    writer.flush();
                } else if (messageToServer.toLowerCase().startsWith(":changepassword")) {
                    writer.println(encryptor.encryptString("changePassword: " + consoleReader.readPassword()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}