

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Pattern;

import org.bson.Document;

import com.eclipsesource.json.JsonObject;
import com.mongodb.BasicDBObject;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class Server1 extends Thread {

	static ServerSocket server1;
	static Socket s1;
	static int portNumber = 0;
	int clientId = 0;
	DataInputStream dis;
	DataOutputStream dout;
	public Server1(int clientId) throws IOException {
		this.clientId = clientId;
		this.dis = new DataInputStream(s1.getInputStream());
		this.dout = new DataOutputStream(s1.getOutputStream());

	}

	public static void main(String [] args) {
		try {
			
			
			//portNumber = Integer.parseInt(args[0]);
			portNumber = 8000;
			server1 = new ServerSocket(portNumber);
			int id = 0;
			
			
			while (true) {
				// listen to the port
				s1 = server1.accept();
				// as somebody connects, create a thread
				System.out.println("here begore creating thread");
				Server1 t_multiClients = new Server1(id++);
				System.out.println("here after creating thread");
				t_multiClients.start();
				// s.close();
			}
			

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void run() {
		// read msg from mutliple clients
		System.out.println("did i come in run");
		try {
			connectToClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connectToClient() throws IOException {
		String sendMessage = " ";
		String recievedMessage = " ";
		MongoClient cilent_mongo = new MongoClient("localhost", 27017);
		String check1 = cilent_mongo.getConnectPoint();
		System.out.println(check1);
		MongoDatabase db = cilent_mongo.getDatabase("wikipedia");
		MongoCollection<Document> coll_content = db.getCollection("contents");
		MongoCollection<Document> coll_user = db.getCollection("user");
		MongoCollection<Document> coll_temp = db.getCollection("temp");
		MongoCursor<Document> cursor = null;
		recievedMessage = readMessage(dis);
		System.out.println("Recieved from client" + clientId + " via Laad Bal : " + recievedMessage);
		BasicDBObject query;
		System.out.println(recievedMessage);
		if(recievedMessage.substring(0, 1).equals("1")){
			String data = recievedMessage.substring(1);
			String pattern = ".*" + data + ".*";
			String content = "";
			//System.out.println("Data "+ data);
			//cursor = coll_content.find(regex("file_name", pattern, "i")).iterator();
			//cursor = coll_content.find({"file_name" : {$in : [/pattern] }})interrupt();
			
			BasicDBObject searchquery = new BasicDBObject();
			searchquery.put("file_name", Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
			cursor = coll_content.find(searchquery).iterator();
			
			//while(cursor.hasNext()){
				//System.out.println(cursor.toString());
				Document doc = cursor.next();
				System.out.println(doc.getString("filecontent"));
				//content = doc.getString("filecontent");
				content = doc.toJson();
				System.out.println(doc.getString("file_name"));
				System.out.println(doc.toString());
				JsonObject json = new JsonObject();
				json.add("id", doc.getInteger("id", 0));
				json.add("file_name", doc.getString("file_name"));
				json.add("filecontents", doc.getString("filecontent"));
				json.add("editors", doc.getString("editors"));
				json.add("contributors", doc.getString("contributors"));
				//content = json.toString();
				System.out.println("New JSON" + content);
				
				// pass the content back to the load balancer 
				//break;
			//}
			//System.out.println("Outside of while"+content+"c");
			writeMessage(dout, content);
			cursor.close();
			
		}else if (recievedMessage.substring(0, 1).equals("2")){
			String data = recievedMessage.substring(1);
			//String pattern = "*." + data + ".*";
			String content = "";
			//query = new BasicDBObject();
			System.out.println("in 2nd if " + data);
			BasicDBObject searchquery = new BasicDBObject();
			searchquery.put("user", data);
			cursor = coll_user.find(searchquery).iterator();
			//cursor = coll_content.find(regex("file_name", pattern, "i")).iterator();
			//while(cursor.hasNext()){
				Document doc = cursor.next();
				System.out.println(doc.getString("user"));
				content = doc.toJson();
				//String role = doc.getString("role");
				System.out.println(doc.getString("user"));
				
				//System.out.println(role);
				// pass the content back to the load balancer 
//				break;
//			}
			writeMessage(dout, content);
			cursor.close();
		}else if (recievedMessage.substring(0, 1).equals("3")){
			String data = recievedMessage.substring(1);
			//String pattern = "*." + data + ".*";
			String content = "";
			//query = new BasicDBObject();
			System.out.println("in 3rd if " + data);
			BasicDBObject searchquery = new BasicDBObject();
			searchquery.put("editors", data);
			cursor = coll_temp.find(searchquery).iterator();
			//cursor = coll_content.find(regex("file_name", pattern, "i")).iterator();
			while(cursor.hasNext()){
				Document doc = cursor.next();
				System.out.println(doc.getString("user"));
				content = doc.toJson();
				//String role = doc.getString("role");
				System.out.println(doc.getString("user"));
				//System.out.println(role);
				// pass the content back to the load balancer 
//				break;
				writeMessage(dout, content);
			}
			
			cursor.close();
			
		}else if (recievedMessage.substring(0, 1).equals("4")){
			String data = recievedMessage.substring(2);
				/*
				 * This is only for updating the existing files 
				 * New files and its details cannot be done here 
				 */
				// push contents in the file and if the contributor is not present then add him 
			
			
			String id = recievedMessage.substring(1, 2);
			int d = Integer.parseInt(id);
			//String c = data;
			//String dString = "Kp kp";
			BasicDBObject doc_update  = new BasicDBObject();
			BasicDBObject obj111 = new BasicDBObject();
			obj111.append("filecontent",data);
			//obj111.append("contributors", dString);
			doc_update.append("$set", obj111);
			//doc_update.append("$set", );
			System.out.println("INSIDE 4 " +data+" ID "+ d+" ");
			BasicDBObject sq = new BasicDBObject().append("id", d);
            coll_content.findOneAndUpdate(sq, doc_update);
			
		}else if (recievedMessage.substring(0, 1).equals("5")){
			String data = recievedMessage.substring(1);
			/*
			 * Add user to the database 
			 */
			
			
			int id = 10;
			String user = "Deekshith";
			String pass = "GGG";
			int role = 	2;
			Document doc123 = new Document();
			doc123.put("id", id);
			doc123.put("user", user);
			doc123.put("password", pass);
			doc123.put("role", role);
			coll_user.insertOne(doc123);
			
		}else if (recievedMessage.substring(0, 1).equals("6")){
			String data = recievedMessage.substring(1);
			/*
			 * Push the temp data into the database 
			 */
			int id = 10;
			String file_name = "UCLA";
			String filecontent = "Blah";
			String editors = "aaadi";
			String contributors = "sumoo"; 
			
			Document doc123 = new Document();
			doc123.put("id", id);
			doc123.put("file_name", file_name);
			doc123.put("filecontent", filecontent);
			doc123.put("editors", editors);
			doc123.put("contributors", contributors);
			coll_temp.insertOne(doc123);
		}else if (recievedMessage.substring(0, 1).equals("7")){
			String data = recievedMessage.substring(1);
			/*
			 * update the user role 
			 */
			int id = 10;
			int role = 1; 
			BasicDBObject doc_update  = new BasicDBObject();
			BasicDBObject obj111 = new BasicDBObject();
			obj111.append("role", role);
			doc_update.append("$set", obj111);
			BasicDBObject sq = new BasicDBObject().append("id", id);
            coll_user.findOneAndUpdate(sq, doc_update);
		}
		
		sendMessage = "hopefully works , sent from server 1 ";
		writeMessage(dout, sendMessage);

		// diff btw server1.getport and s.getPort
		// System.out.println("Recieved from Load balancer at port : " +
		// s.getPort());

	}

	public static void writeMessage(DataOutputStream d_out, String msg) throws IOException {
		d_out.writeUTF(msg);
	}

	public static String readMessage(DataInputStream d_is) throws IOException {
		String stringRecv = (String) d_is.readUTF();
		return stringRecv;

	}
	
}
