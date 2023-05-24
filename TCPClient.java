import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Client {
     private static Socket server;
     private static DataOutputStream out;
     private static BufferedReader in;
     private static String serverMessage;

     public static void main(String[] args) {
         try {
             server = new Socket("localhost", 50000);
             out = new DataOutputStream(server.getOutputStream());
             in = new BufferedReader(new InputStreamReader(server.getInputStream()));
             String username = System.getProperty("user.name");

             // Variables for tracking largest server
             int serverCount = 0;
             int serverCoreSize = 0;
             String largestServerType = "";
             int largestServerCount = 0;
             int coreSize = 0;
             String serverType = "";
             String[] jobDetails;

             // Handshake Section
             sendMsg("HELO");
             System.out.println("SENT: HELO");
             String response = in.readLine();

             // Check that the server is running and responds 'helo'
             System.out.println("RCVD: " + response);

             sendMsg("AUTH " + username);
             response = in.readLine();
             System.out.println("RCVD: " + response);

             sendMsg("REDY");
             System.out.println("SENT: REDY");
             response = in.readLine();
             System.out.println("RCVD: " + response);

             // while there are still jobs being sent from the server
             while (!response.equals("NONE")) {
                 // while still receiving jobs from the server
                 if (response.startsWith("JOBN")) {
                     // loop through finding the largest server only once
                     boolean cycle = true;
                     while (cycle) {
                         // split the jobDetails into an array of strings
                         jobDetails = response.split(" ");
                         System.out.println(jobDetails);

                         sendMsg("GETS All");
                         response = in.readLine();
                         System.out.println(response);
                         String[] dataDetails = response.split(" ");
                         int nRecs = Integer.parseInt(dataDetails[1]);
                         int recSize = Integer.parseInt(dataDetails[2]);
                         sendMsg("OK");

                         for (int i = 0; i < nRecs; i++) {
                             response = in.readLine();
                             System.out.println(response);
                             String[] serverDetails = response.split(" ");
                             serverType = serverDetails[0];
                             serverCount = Integer.parseInt(serverDetails[1]);
                             serverCoreSize = Integer.parseInt(serverDetails[4]);

                             if (serverCoreSize > coreSize) {
                                 coreSize = serverCoreSize;
                                 largestServerCount = serverCount;
                                 largestServerType = serverType;
                             }
                         }
                         cycle = false;
                     }

                     System.out.println("***********");
                     System.out.println(largestServerType);
                     System.out.println("Found largest server ^");
                     sendMsg("OK");

                     // schedule the jobs
                     System.out.println("largest serverCount below");
                     System.out.println(largestServerCount);
                     for (int i = 0; i <= largestServerCount; i++) {
                         sendMsg("SCHD " + jobDetails[2] + " " + largestServerType + " " + i);
                         System.out.println(jobDetails[2] + " " + largestServerType + " " + i);
                         response = in.readLine();
                         System.out.println(response);
                     }
                 }
             }

             sendMsg("QUIT");
             response = in.readLine();
             System.out.println(response);
             System.out.println("done");

             server.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     public static void sendMsg(String message) throws IOException {
         out.write((message + "\n").getBytes());
         out.flush();
     }
}