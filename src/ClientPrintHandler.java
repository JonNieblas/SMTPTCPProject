import java.io.BufferedReader;
import java.io.IOException;

public class ClientPrintHandler {

    public String SendersEmailAddressHandler(BufferedReader sysIn) throws IOException{
        String sendersEmail;
        System.out.println("Please input the following criteria:");
        System.out.println("The sender's email address: ");
        sendersEmail = sysIn.readLine();
        return sendersEmail;
    }

    public String ReceiversEmailAddressHandler(BufferedReader sysIn) throws IOException{
        String receiversEmail;
        System.out.println("The receiver's email address: ");
        receiversEmail = sysIn.readLine();
        return receiversEmail;
    }

    public String SubjectEmailHandler(BufferedReader sysIn) throws IOException{
        String subject;
        System.out.println("The email's subject: ");
        subject = sysIn.readLine();
        return subject;
    }

    public String ContentsOfEmail(BufferedReader sysIn) throws IOException{
        StringBuilder contentsBody = new StringBuilder();
        String line;

        System.out.println("The contents of the email: ");
        System.out.println("(Line by line. \r\nEnd contents body with a period ('.') on its own line.");
        do {
            line = sysIn.readLine();
            contentsBody.append(line + "\r\n");
        } while (!line.trim().equals("."));

        return contentsBody.toString();
    }

    public String ContinueOrQuit (BufferedReader sysIn) throws IOException {
        String fromUser;
        do {
            System.out.println("");//gives some space
            System.out.println("Would you like to request another item?");
            System.out.println("Please enter yes or no:");
            fromUser = sysIn.readLine().toLowerCase().trim();
        } while (!fromUser.equals("yes") && !fromUser.equals("no"));
        return fromUser;
    }

}