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
        //create ClientPrintHandler for handling methods
        ClientPrintHandler printHandler = new ClientPrintHandler();


        //ask for DNS name/IP from user
        String hostName;
        System.out.println("Please enter the DNS name/IP of the HTTP server.");
        hostName = sysIn.readLine();

        //holds info given by the user
        String sendersEmail;
        String receiversEmail;
        String subject;
        String emailContents;


        //possibly use later depending on how we handle commands
        //String userCommandChoice;

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

        //check if process is to be repeated
        do{
            //get info from user
            sendersEmail = printHandler.SendersEmailAddressHandler(sysIn);
            receiversEmail = printHandler.ReceiversEmailAddressHandler(sysIn);
            subject = printHandler.SubjectEmailHandler(sysIn);
            emailContents = printHandler.ContentsOfEmail(sysIn);

            //send messages to the server & read responses
            ClientSendHandler sendHandler = new ClientSendHandler(sendersEmail, receiversEmail, subject, emailContents);

            //possibly make it so that the user has to choose which command to send
            //System.out.println("Please enter HELO, MAIL FROM, RCPT TO, DATA, or MESSAGE");
            sendHandler.SendCodeToServer(socketOut, socketIn, "HELO");
            sendHandler.SendCodeToServer(socketOut, socketIn, "MAIL FROM");
            sendHandler.SendCodeToServer(socketOut, socketIn, "RCPT TO");
            sendHandler.SendCodeToServer(socketOut, socketIn, "DATA");
            sendHandler.SendCodeToServer(socketOut, socketIn, "MESSAGE");

            //sends "QUIT" to server if "no"
            fromUser = printHandler.ContinueOrQuit(sysIn);
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