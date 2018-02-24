import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientSendHandler {
    //info from user
    private String sendersEmail;
    private String receiversEmail;
    private String subject;
    private String emailContents;
    private String senderDomain;

    //records RTT of each object
    long currentTime;
    float before;
    float after;
    float rtt;

    String completeMessage;
    String toLine;
    String fromLine;
    String subjectLine;
    String emptyLine = "\r\n";

    public ClientSendHandler(String sEmail, String rEmail, String sub, String eContents){
        this.sendersEmail = sEmail;
        this.receiversEmail = rEmail;
        this.subject = sub;
        this.emailContents = eContents;
        this.senderDomain = sendersEmail.replaceAll(".*@", "");

        toLine = "To: " + this.receiversEmail + "\r\n";
        fromLine = "From: " + this.sendersEmail +"\r\n";
        subjectLine = "Subject: " + this.subject + "\r\n";
        completeMessage = toLine + fromLine + subjectLine + emptyLine + this.emailContents;
    }

    public void SendCodeToServer(PrintWriter socketOut, BufferedReader socketIn, String code) throws IOException{
        currentTime = System.currentTimeMillis();
        before = currentTime/1000.0f;
        switch (code){
            case "HELO": socketOut.println("HELO " + senderDomain + "\r\n");
                break;
            case "MAIL FROM": socketOut.println("MAIL FROM: " + sendersEmail + "\r\n");
                break;
            case "RCPT TO": socketOut.println("RCPT TO: " + receiversEmail + "\r\n");
                break;
            case "DATA": socketOut.println("DATA");
                break;
            case "MESSAGE": socketOut.println(completeMessage);
                break;
        }
        currentTime = System.currentTimeMillis();
        after = currentTime/1000.0f;
        rtt = after - before;
        System.out.println(socketIn.readLine());
        System.out.println("RTT: " + rtt + "\r\n");
    }
}
