import java.net.Socket;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TCPClient {
     public static void main(String[] args) {
         try {
             int jobID = 0;
            //create a new socket
            Socket s = new Socket("localhost", 50000);

            //initialise the inputs readers and writers
            BufferedReader in = new BufferedReader(new
            InputStreamReader(s.getInputStream()));
            DataOutputStream out = new
            DataOutputStream(s.getOutputStream());

            //send 'helo'
            out.write(("HELO\n").getBytes());
            out.flush();
            System.out.println("SENT: HELO");

            //return recieved message
            String str = in.readLine();
            System.out.println("RCVD: "+str);

            //return authorisation and username
            String username = System.getProperty("user.name");
            out.write(("AUTH " + username + "\n").getBytes());
            
            //read the response
            str = in.readLine();
            System.out.println("RCVD: " + str);

            //send 'redy'
            out.write(("REDY\n").getBytes());
            out.flush();
            str = in.readLine();
            System.out.println("RCVD: " + str);

            if (str.startsWith("JOBN")) {
                String[] jobInfo = str.split(" ");
                jobID = Integer.parseInt(jobInfo[2]);
                System.out.println("Job ID: " + jobID);
             }

            //write 'quit'
            out.write(("QUIT\n").getBytes());
            out.flush();
            str = in.readLine();
            System.out.println("RCVD: " + str);

            //close the socket
            in.close();
            out.close();
            s.close();

        } catch(Exception e) {
            System.out.println(e);
        }
     }
}   