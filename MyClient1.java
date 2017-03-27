
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;

/*The client class makes a request to the server
 * here the request is sent to the load balancer , and then re-directed to another server
 */
public class MyClient1 {
	Socket s;
	DataOutputStream dout;
	DataInputStream dis;

	public MyClient1() {
	}

	public MyClient1(Socket s) throws IOException {
		this.s = s;
		this.dout = new DataOutputStream(s.getOutputStream());
		this.dis = new DataInputStream(s.getInputStream());
	}

	public static void main(String[] args) {
		try {
			while (true) {
				// load balancer
				int loadBalPort = 9000;
				String sendMessage = " ";
				String recievedMessage = " ";
				// send the request connection to load bal
				Socket s = new Socket("localhost", loadBalPort);
				MyClient1 cObj = new MyClient1(s);
				cObj.testMethod(sendMessage, recievedMessage, loadBalPort);
				s.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void testMethod(String sendMessage, String recievedMessage, int loadBalPort) throws IOException {
		int choose;
		Scanner in = new Scanner(System.in);
		String wikilog = "****************************************************************************************"
				+ '\n' + "****************************************************************************************"
				+ '\n' + "*                  WELCOME TO RIT WIKIPEDIA             " + '\n'
				+ "****************************************************************************************" + '\n'
				+ "****************************************************************************************";
		System.out.println(wikilog);
		System.out.println(" Choose from the menu below ");
		System.out.println("-----------------------------");
		System.out.println("1 - Search");
		System.out.println("2 - Quit");
		choose = in.nextInt();
		if (choose == 2) {
			dout.flush();
			dout.close();
			System.exit(0);
		} else {
			String search;
			System.out.println("Please enter the string you want to search in RIT Wikipedia Database");
			Scanner in1 = new Scanner(System.in);
			search = in1.next();
			if (search.isEmpty()) {
				System.out.println("Please enter a valid search string ");

			} else {
				sendMessage = "1" + search;
				writeMessage(dout, sendMessage);
				recievedMessage = readMessage(dis);
				recievedMessage = readMessage(dis);
				/*
				 * Decrypt the JSON data recieved and store it into different
				 * variables for further use
				 */
				JsonObject obh = Json.parse(recievedMessage).asObject();
				int id = obh.get("id").asInt();
				String filename = obh.get("file_name").asString();
				String file_contents = obh.get("filecontent").asString();
				String editors = obh.get("editors").asString();
				String contributors = obh.get("contributors").asString();
				System.out.println("ID: " + id + " File name " + filename + " file contents " + file_contents
						+ " editors " + editors + " contributors " + contributors);
				/*
				 * create new file in the client system and save the contents
				 * received from the server
				 */

				String name = filename + ".txt";
				File curfile = new File(System.getProperty("user.home"), name);
				PrintWriter writer = new PrintWriter(curfile, "UTF-8");
				// writer.println(wikilog);
				writer.println(file_contents);
				writer.println(editors);
				writer.println(contributors);
				writer.close();
				/*
				 * Store the content of the file in a byte array
				 */
				byte[] f1 = Files.readAllBytes(curfile.toPath());
				/*
				 * Open the VI editor with the contents of the msg received from
				 * the server and the client can write the file
				 */
				System.out.println("Opening VI editor");
//				ProcessBuilder file_open = new ProcessBuilder("/usr/bin/vi", curfile.getAbsolutePath());
//				file_open.redirectOutput(ProcessBuilder.Redirect.INHERIT);
//				file_open.redirectError(ProcessBuilder.Redirect.INHERIT);
//				file_open.redirectInput(ProcessBuilder.Redirect.INHERIT);
//
//				Process ppProcess = file_open.start();
//				try {
//					ppProcess.waitFor();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				System.out.println("Exiting VI");

				/*
				 * get the byte array of the file again and compare with the
				 * other byte array value. if there is a change then ask the
				 * user if he wants to keep the change? If so then do send the
				 * new file to the server for approval
				 */

				byte[] f2 = Files.readAllBytes(curfile.toPath());
				if (Arrays.equals(f1, f2)) {
				} else {
					System.out.println('\n'+'\n'+'\n'+'\n'+'\n');
					System.out.println("We see that you have changed the document. Do you prefer to save it?");
					System.out.println("y/n");
					String yn;
					Scanner in2 = new Scanner(System.in);
					yn = in2.next();
					if (yn.equals("y")) {
						System.out.println("Your changes will be saved once its approved by the editor of the document");
						dout.flush();
						dout.close();
						s.close();
						Socket s2 = new Socket("localhost", loadBalPort);
						DataOutputStream d2out = new DataOutputStream(s2.getOutputStream());
						DataInputStream d2in = new DataInputStream(s2.getInputStream());

						String filedata = new String(Files.readAllBytes(curfile.toPath()));
						String wridata = "4" + id + filedata;
						writeMessage(d2out, wridata);
						String getfroml = readMessage(d2in);
						// System.out.println("MSG from LBAL"+ getfroml);
						d2out.flush();
						d2out.close();
						s2.close();
					} else {
						System.out.println("Your changes are not saved !!");
					}
				}

				dout.flush();
				dout.close();
				in.close();
				in1.close();
			}
		}
	}

	public static void writeMessage(DataOutputStream dout, String msg) throws IOException {
		dout.writeUTF(msg);
	}

	public static String readMessage(DataInputStream dis) throws IOException {
		String stringRecv = (String) dis.readUTF();
		return stringRecv;

	}
}
