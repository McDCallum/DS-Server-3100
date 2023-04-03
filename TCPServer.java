import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

public class TCPServer {
     public static void main(String[] args) throws IOException {

         int aPort = Integer.parseInt(args[0]);
         System.out.println("Port Number: " + aPort);
         ServerSocket ss = new ServerSocket(aPort);
         while(true){

             try{
                 Socket s = ss.accept();
                 DataInputStream din = new DataInputStream(s.getInputStream());
                 DataOutputStream dout = new DataOutputStream(s.getOutputStream());

                 System.out.println("Target IP: " + s.getInetAddress() + "Target Port: " + s.getPort());
                 System.out.println("Local IP: " + s.getLocalAddress() + "Local Port: " + s.getLocalPort());
                 try {TimeUnit.SECONDS.sleep(10);}
                    catch(InterruptedException e) {System.out.println(e);}

                 String str = (String)din.readUTF();
                 System.out.println("RCVD: " +str);

                 dout.writeUTF("GDAY");
                 System.out.println("SENT: GDAY");

                 str = (String)din.readUTF();
                 System.out.println("RCVD: " +str);

                 dout.writeUTF("BYE");
                 System.out.println("SENT: BYE");

                 din.close();
                 dout.close();
                 s.close();
             }

             catch(Exception e) {System.out.println(e);
            }
             

         }
    }
}
