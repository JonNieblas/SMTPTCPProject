/*
 * Server App upon TCP
 * A thread is started to handle every client TCP connection to this server
 * Weiying Zhu
 */

import java.net.*;
import java.io.*;

public class SMTPServer extends Thread {
    private Socket clientTCPSocket;

    public SMTPServer(Socket socket) {
        super("TCPMultiServerThread");
        clientTCPSocket = socket;
    }

    public void run() {

        try {
            PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
            BufferedReader cSocketIn = new BufferedReader(
                    new InputStreamReader(
                            clientTCPSocket.getInputStream()));

            String fromClient, toClient;

            //send client 220 response
            String successfulConnectResponse = "220 cs3700a.msudenver.edu";
            cSocketOut.println(successfulConnectResponse);

            while ((fromClient = cSocketIn.readLine()) != null) {

                toClient = fromClient.toUpperCase();
                cSocketOut.println(toClient);

                if (fromClient.equals("Bye"))
                    break;
            }

            cSocketOut.close();
            cSocketIn.close();
            clientTCPSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}