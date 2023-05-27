import java.io.*;
import java.net.*;

public class Client {
     public static void main(String[] args) {

     try {
         Socket socket = new Socket("localhost", 50000);
         BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

         // Send HELO message
         outputStream.write(("HELO\n").getBytes());
         String response = reader.readLine();
         outputStream.flush();

         // Send AUTH message
         String username = System.getProperty("user.name");
         outputStream.write(("AUTH " + username + "\n").getBytes());
         response = reader.readLine();
         outputStream.flush();

         // Request job
         outputStream.write(("REDY\n").getBytes());
         response = reader.readLine();

         if (response.equals("NONE")) {
             // No available jobs, quit
             outputStream.write(("QUIT\n").getBytes());
             response = reader.readLine();
             outputStream.flush();

             outputStream.close();
             socket.close();
         }

         // Split job details
         String[] splitJob = response.split(" ");
         int jobID = Integer.parseInt(splitJob[2]);
         outputStream.flush();

         // Get number of records
         outputStream.write(("GETS All\n").getBytes());
         response = reader.readLine();
         String[] split = response.split(" ");
         int records = Integer.parseInt(split[1]);
         outputStream.flush();

         // Confirm receipt of records
         outputStream.write(("OK\n").getBytes());
         int cpu = 0;
         String serverType = null;
         int serverID = 0;
         int numServersLargestType = 0;

         // Find largest server
         String previousServerType = null;
         for (int i = 0; i < records; i++) {
             response = reader.readLine();
             String[] splitStr = response.split(" ");
             int strCPU = Integer.parseInt(splitStr[4]);
             String type = splitStr[0];
             if (strCPU > cpu) {
                 cpu = strCPU;
                 serverType = type;
         }

         // Increase number of servers of largest type
         if (serverType.equals(type) && cpu == strCPU) {
             if (serverType.equals(previousServerType)) {
                 numServersLargestType++;
             } else {
                 numServersLargestType = 1;
                 previousServerType = type;
             }
         }

         outputStream.flush();
         }

         // Confirm number of servers of largest type
         outputStream.write(("OK\n").getBytes());
         response = reader.readLine();
         outputStream.flush();

         // Schedule first job
         outputStream.write(("SCHD " + jobID + " " + serverType + " " + serverID + "\n").getBytes());
         serverID++;
         if (serverID >= numServersLargestType) {
             serverID = 0;
         }
         response = reader.readLine();
         outputStream.flush();

         // Schedule all other jobs
         while (!response.equals("NONE")) {
             outputStream.write(("REDY\n").getBytes());
             response = reader.readLine();

             if (response.equals("NONE")) {
                 break;
             }

             splitJob = response.split(" ");
             String command = splitJob[0];
             if (command.equals("JOBN")) {
                 jobID = Integer.parseInt(splitJob[2]);
                 outputStream.flush();

                 outputStream.write(("SCHD " + jobID + " " + serverType + " " + serverID + "\n").getBytes());
                 serverID++;
                 if (serverID >= numServersLargestType) {
                     serverID = 0;
                 }
             response = reader.readLine();
             outputStream.flush();
             }
         }

         // Quit
         outputStream.write(("QUIT\n").getBytes());
         response = reader.readLine();
         outputStream.flush();

         outputStream.close();
         socket.close();

     } catch (Exception e) {
         System.out.println(e);
     }

     }
}