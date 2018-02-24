/**
 * Server Class responsible for receiving SMTP messages from
 * the SMTPClient Class, then returning messages based on
 * client input.
 *
 * Has one class that assists, ServerSendHandler.
 *
 * Jonathan Nieblas
 */
import java.net.*;
import java.io.*;
public class SMTPServer extends Thread {
    private Socket clientTCPSocket;
    InetAddress ip;

    public SMTPServer(Socket socket) {
        super("TCPMultiServerThread");
        clientTCPSocket = socket;
    }

    public void run() {

        int HELOCounter = 0;
        int MAILFROMCounter = 0;
        int RCPTTOCounter = 0;
        int DATACounter = 0;

        //create ip for address info
        try {
            ip = InetAddress.getLocalHost();
        } catch(UnknownHostException ex){
            ex.printStackTrace();
        }
        try {
            PrintWriter cSocketOut = new PrintWriter(clientTCPSocket.getOutputStream(), true);
            BufferedReader cSocketIn = new BufferedReader(new InputStreamReader(clientTCPSocket.getInputStream()));
            String fromClient;

            //send client 220 response
            String successfulConnectResponse = "220 " + ip.getHostAddress();
            cSocketOut.println(successfulConnectResponse);

            ServerSendHandler sendHandler = new ServerSendHandler(clientTCPSocket, ip);

            //read lines from client
            while ((fromClient = cSocketIn.readLine()) != null) {
                if (fromClient.equals("QUIT")) {
                    System.out.println(fromClient);
                    cSocketOut.println("221 " + ip.getHostAddress() + " closing connection");
                    break;
                }
                if(HELOCounter == 0 && !fromClient.equals("")) {
                    HELOCounter = sendHandler.HELOHandler(fromClient, cSocketOut);
                } else if(HELOCounter == 1 && MAILFROMCounter == 0 && !fromClient.equals("")){
                    MAILFROMCounter = sendHandler.MAILFROMHandler(fromClient, cSocketOut);
                } else if(MAILFROMCounter == 1 && RCPTTOCounter == 0 && !fromClient.equals("")){
                    RCPTTOCounter = sendHandler.RCPTTOHandler(fromClient, cSocketOut);
                } else if(RCPTTOCounter == 1 && DATACounter == 0 && !fromClient.equals("")){
                    DATACounter = sendHandler.DATAHandler(fromClient, cSocketOut);
                } else if(DATACounter == 1){
                    System.out.println(fromClient);
                    if(fromClient.trim().equals(".")){
                        cSocketOut.println("250 Message received and to be delivered");
                        HELOCounter = 0;
                        MAILFROMCounter = 0;
                        RCPTTOCounter = 0;
                        DATACounter = 0;
                    }
                }
            }

            System.out.println("Thread Terminated.");
            cSocketOut.close();
            cSocketIn.close();
            clientTCPSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}