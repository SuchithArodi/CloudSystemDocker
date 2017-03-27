import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

import javax.sound.sampled.Line;
//check close of sockets

public class LBalServer extends Thread {
	// can't i change static??
	static ServerSocket lBalSS;
	static int lBalPortNum = 9000;
	static Socket s;
	// new server port
	static int ser1PN = 8001;
	// public LBalServer()
	// every thread will have
	// socket to get connected
	// id, server id it is connected to

	//int serverPNum;
	static String serverip = " ";
	// DataInputStream dis,disToserver;
	// DataOutputStream dout,doutToserver;
	// every client will have its own inp output stream
	DataInputStream dis;
	DataOutputStream dout;
	static int clientNumber = 0;
	static ArrayList<String> freeServers = new ArrayList<String>();

	public LBalServer() {

	}

	public LBalServer(String serverip) throws IOException {
		//this.serverPNum = serPNum;
		this.dis = new DataInputStream(s.getInputStream());
		this.dout = new DataOutputStream(s.getOutputStream());
		this.serverip = serverip;

	}

	public static void main(String[] args) {
		try {

			// create the server socket for the load balancer
			lBalSS = new ServerSocket(lBalPortNum);
			System.out.println("before calling method");
			new LBalServer().processClientReq();

		} catch (Exception e) {
			System.out.println(e);
		}
	}


	
	public void processClientReq() throws IOException {
		// check if the request is redirected !
		int id = 0;
		
		
		while (true) {
			// listen to the port
			// should the connection get accepted here??
			System.out.println("waiting for client");
			s = lBalSS.accept();
			// as somebody connects, create a thread and assign a ID????
			// s.close()
			System.out.println("befor bread: " + s);
//			BufferedReader bReader = new BufferedReader(new FileReader("C:\\Users\\Suchith\\workspace\\Load_Balancer\\src\\ListOfIPsserver.txt"));
//			String ipline;
//			while ((ipline = bReader.readLine()) != null){
//				freeServers.add(ipline);
//				System.out.println(ipline);
//			}
			
			 BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Suchith\\workspace\\Load_Balancer\\src\\ListOfIPsserver.txt"));
			 String line = null;
			 while ((line = br.readLine()) != null) {
				 String temp = line.substring(2, line.length()-2);
				 freeServers.add(temp);
			   System.out.println(temp);
			 }		
			
			
//			try (Stream<String> stream = Files.lines(Paths.get("ListOfIPsserver.txt"))) {
////		        stream.forEach(System.out::println);
////		        stream.forEach();
//				stream.forEach(s -> freeServers.add(s));
//			}
			//System.out.println("IP line "+freeServers.toString());
			//ArrayList<String> freeServers = getTheServers();
			//logic to assign the ports????
			//do i need to have the synchronized block??
			
			//if 5 servers 0,1,2,3 is assigned in round robin fashion , since the id increments sequentially
			int serverIndex =(clientNumber % (freeServers.size())) ;
			serverip =  freeServers.get(serverIndex);
			clientNumber++;
			
			LBalServer t_multiClients = new LBalServer(serverip);
			t_multiClients.start();
		}
	}
	

	public void run() {
		System.out.println("got connected to load balancer");
		System.out.println();
		System.out.println("***********************");
		System.out.println("This is " + clientNumber+"'th" + "client");
		try {
			connectToDiffServer();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void connectToDiffServer() throws UnknownHostException, IOException {
		// redirect the connection

		String sendMessage = " ";
		String recievedMessage = " ";
		System.out.println("check"+this.serverip);
		Socket server1 = new Socket(this.serverip , 8001);

		// check to print client id!!
		DataOutputStream doutToServer1 = new DataOutputStream(server1.getOutputStream());
		DataInputStream disToServer1 = new DataInputStream(server1.getInputStream());

		// revieved these messages from client, before rediredircteing

		recievedMessage = readMessage(dis);
		System.out.println("Message recvd from client is : " + recievedMessage);

		// "This is to inform client about working out load balancer
		sendMessage = "This is to inform client about working out load balancer :"
				+ "fromload balncer:, redirecting the request from client ";
		writeMessage(dout, sendMessage);

		// redirect the message to the new client
		sendMessage =  recievedMessage;
		writeMessage(doutToServer1, sendMessage);
		// now the server1 should reply , which should be written to client1,
		// via LBal
		recievedMessage = readMessage(disToServer1);
		System.out.println("Recvd message ffrom server 1 : " + recievedMessage);
		System.out.println("Redirecting to the client");
		sendMessage =  recievedMessage;
		writeMessage(dout, sendMessage);

		dout.flush();
		doutToServer1.flush();

		dout.close();
		doutToServer1.close();
	}

	public static void writeMessage(DataOutputStream d_out, String msg) throws IOException {
		d_out.writeUTF(msg);
	}

	public static String readMessage(DataInputStream d_is) throws IOException {
		if((d_is.available()>0) && (d_is.readUTF() != null)){
			String stringRecv = (String) d_is.readUTF();
			return stringRecv;
		}
		return "";
	}
	
//	public  ArrayList<Integer> getTheServers()
//	{
//		//these are the servers up , and listening to the particular port number
//		freeServers = new ArrayList<Integer>();
//		
//		freeServers.add(8001);
//		freeServers.add(8002);
//		//freeServers.add(8003);
//		//freeServers.add(8004);
//		//freeServers.add(8005);
////		freeServers.add(8006);
//		
//		return freeServers;
//		
//	}

}
