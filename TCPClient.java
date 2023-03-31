import java.net.Socket;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TCPClient {
     public static void main(String[] args) {
             try{
                 //InetAddress aHost = InetAddress.getByName(args[0]);
                 //int aPort = Integer.parseInt(args[1]);

                 Socket s = new Socket("127.0.0.1", 50000);
                 DataOutputStream dout = new
                DataOutputStream(s.getOutputStream());
                 BufferedReader bin = new BufferedReader(new
                InputStreamReader(s.getInputStream()));

                 //DataInputStream din = new
                DataInputStream(s.getInputStream());


                 System.out.println("Target IP: " + s.getInetAddress() + "Target Port: " + s.getPort());
                 System.out.println("Local IP: " + s.getLocalAddress() + "Local Port: " + s.getLocalPort());



                 dout.write(("HELO\n").getBytes());
                 dout.flush();
                 System.out.println("SENT: HELO ");


                 String str = bin.readLine();
                 System.out.println("RCVD: "+str);


                 bin.close();
                 dout.close();
                 s.close();

                 }
             catch(Exception e ){System.out.println(e);}
         }

}