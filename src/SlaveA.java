import java.net.*;
import java.io.*;
import java.util.*;
public class SlaveA {
	public static void main(String[] args) {
		
		String hostName = "127.0.0.1";
		int portNumber = 30122;
		char slaveType = 'a';
		
		try (
			      Socket slaveSocket = new Socket(hostName, portNumber);
			        		
			      BufferedReader requestReader= // stream to read text response from master
			      new BufferedReader(
			      new InputStreamReader(slaveSocket.getInputStream())); 	
			        		
			        PrintWriter responseWriter = // stream to write text requests to server
			            new PrintWriter(slaveSocket.getOutputStream(), true);
		  ){
				System.out.println("Slave A connected");
				ArrayList<String> myJobs = new ArrayList<String>();
				Object myJobs_Lock = new Object();
				
				SlaveThread reader = new SlaveThread (requestReader, slaveType, myJobs, myJobs_Lock);
				SlaveThread writer = new SlaveThread (responseWriter, slaveType, myJobs, myJobs_Lock);
				reader.start();
				writer.start();
				  
				try{
					reader.join();
					writer.join();
				}
				catch(Exception e) {};
				  
				  
		  } catch (UnknownHostException e) {
	        System.err.println("Don't know about host " + hostName);
	        System.exit(1);
		  } catch (IOException e) {
	        System.err.println("Couldn't get I/O for the connection to " +
	            hostName);
	        System.exit(1);
		  }	
	}
}
