import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class ServerSendHandler {

    private Socket clientTCPSocket;
    InetAddress ip;

    public ServerSendHandler(Socket socket, InetAddress ip){
        this.clientTCPSocket = socket;
        this.ip = ip;
    }

    public int HELOHandler(String code, PrintWriter cSocketOut){
        int HELOCounter;
        System.out.println(code);
        if(!code.startsWith("HELO:")){
            cSocketOut.println("503 5.5.2 Send hello first");
            HELOCounter = 0;
        } else{
            cSocketOut.println("250 " + ip.getHostAddress() + " Hello " + clientTCPSocket.getRemoteSocketAddress());
            HELOCounter = 1;
        }
        return HELOCounter;
    }

    public int MAILFROMHandler(String code, PrintWriter cSocketOut){
        int MAILFROMCounter;
        System.out.println(code);
        if(!code.startsWith("MAIL FROM:")){
            cSocketOut.println("503 5.5.2 Need mail command");
            MAILFROMCounter = 0;
        } else{
            cSocketOut.println("250 2.1.0 Sender OK");
            MAILFROMCounter = 1;
        }

        return MAILFROMCounter;
    }

    public int RCPTTOHandler(String code, PrintWriter cSocketOut){
        int RCPTTOHandler;
        System.out.println(code);
        if(!code.startsWith("RCPT TO:")){
            cSocketOut.println("503 5.5.2 Need rcpt command");
            RCPTTOHandler = 0;
        } else{
            cSocketOut.println("350 2.1.5 Recipient OK");
            RCPTTOHandler = 1;
        }

        return RCPTTOHandler;
    }

    public int DATAHandler(String code, PrintWriter cSocketOut){
        int DATAHandler;
        System.out.println(code);
        if(!code.trim().equals("DATA")){
            cSocketOut.println("503 5.5.2 Need data command");
            DATAHandler = 0;
        } else{
            cSocketOut.println("354 Start mail input; end with <CRLF>.<CRLF>");
            DATAHandler = 1;
        }

        return DATAHandler;
    }
}
