import java.io.*;
import java.net.*;

public class SMTPClient {
    public static void main(String[] args) throws IOException {

        Socket tcpSocket = null;
        PrintWriter socketOut = null;
        BufferedReader socketIn = null;

        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;
        //create ClientHelper for handling methods
        ClientHelper helper = new ClientHelper();

        //ask for DNS name/IP from user
        String hostName;
        System.out.println("Please enter the DNS name/IP of the HTTP server.");
        hostName = sysIn.readLine();

        //holds info given by the user
        String sendersEmail;
        String receiversEmail;
        String subject;
        String emailContents;

        //connect to server
        try {
            tcpSocket = new Socket(hostName, 5210);
            socketOut = new PrintWriter(tcpSocket.getOutputStream(), true);
            socketIn = new BufferedReader(new InputStreamReader(tcpSocket.getInputStream()));
            fromServer = socketIn.readLine();
            System.out.println(fromServer);//print 220 response from server
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: "  + hostName);
            System.exit(1);
        }

//        while ((fromUser = sysIn.readLine()) != null) {
//            System.out.println("Client: " + fromUser);
//            socketOut.println(fromUser);
//
//            if ((fromServer = socketIn.readLine()) != null)
//            {
//                System.out.println("Server: " + fromServer);
//            }
//            else {
//                System.out.println("Server replies nothing!");
//                break;
//            }
//
//            if (fromUser.equals("Bye."))
//                break;
//
//        }

        //check if process is to be repeated
        do{
            //get info from user
            sendersEmail = helper.SendersEmailAddressHandler(sysIn);
            receiversEmail = helper.ReceiversEmailAddressHandler(sysIn);
            subject = helper.SubjectEmailHandler(sysIn);
            emailContents = helper.ContentsOfEmail(sysIn);

            fromUser = helper.ContinueOrQuit(sysIn);
            if(fromUser.equals("no")){
                socketOut.println("QUIT");
                fromServer = socketIn.readLine();
                System.out.println(fromServer);
            }
        } while(fromUser.equals("yes"));

        socketOut.close();
        socketIn.close();
        sysIn.close();
        tcpSocket.close();
    }
}