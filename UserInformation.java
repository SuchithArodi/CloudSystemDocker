import java.io.File;
import java.io.*;
import java.io.IOException;
import java.util.Scanner;

/*This Class provides the necessary usage for the Users
 * For now all the information is  saved in a file
 * 1.Create the Account
 * 
 *  
	 */
public class UserInformation {

	String userName = " ";
	String emailID = " ";
	String password = " ";
	String role = " ";

	// null or space ???
	public UserInformation() {
		this.userName = null;
		this.emailID = null;
		this.role = null;
		this.password = null;

	}

	public static void main(String[] args) throws IOException {

		UserInformation userObj = new UserInformation();
		userObj.storeUserDetails();
		userObj.getUserDetails(userObj);
		userObj.printUserDetails();
		userObj.writeTofile();

	}

	// call this method only if the user wants to create the account
	public void getUserDetails(UserInformation userObj) {
		Scanner input = new Scanner(System.in);
		System.out.println("Enter the User Name :");
		userName = input.nextLine();
		System.out.println("Enter the email ID :");
		emailID = input.nextLine();
		System.out.println("Enter the password :");
		// works while running from terminal
		Console console = System.console();

		password = String.valueOf(console.readPassword());
		// password = (console.readPassword()).toString();
		// password = input.nextLine();
		System.out.println(password);

		// String text = String.valueOf(data);
		System.out.println("Choose your Role :");
		userObj.chooseRoles();

	}

	public void chooseRoles() {
		boolean isRole = false;
		Scanner input = new Scanner(System.in);

		System.out.println("Select 1: for Reader, Contributor, Editor");
		System.out.println("Select 2: for Reader,Editor");

		// if (role)

		while (!isRole) {
			int roleNum = input.nextInt();
			switch (roleNum) {
			case 1:
				System.out.println(" You can Read, Contribute, Edit the Wikipedia Page ");
				// set the role of the user as UserNonAdmin, if he can read and
				// Edit and Contribute, "ADMIN"
				// this access should be granted by super user**
				role = "UserAdmin";
				isRole = true;
				break;

			case 2:
				System.out.println("You can Read, Edit  the Wikipedia Page ");
				// set the role of the user as UserNonAdmin, if he can only read
				// and Edit
				role = "UserNonAdmin";
				isRole = true;
				break;

			default:
				System.out.println("Please select the correct option");
				break;
			}
		}

	}

	// REMOVE
	public void printUserDetails() {

		// check for regular expressions
		// and the type of the input by the user

		System.out.println(" Username : " + userName);
		System.out.println(" emailID : " + emailID);
		System.out.println(" password : " + password);
		System.out.println(" role : " + role);

	}

	/*
	 * create a Folder "UserDetails", if it doesn't exist Create a file
	 * "UserDetails.txt".This fle has all the user details
	 */
	public void storeUserDetails() throws IOException {

		if (!(new File("C:\\Users\\Suchith\\Downloads\\DS\\ds\\UserDetails.txt").exists())) {
			File f = new File("C:\\Users\\Suchith\\Downloads\\DS\\ds\\UserDetails.txt");
			f.getParentFile().mkdir();
			f.createNewFile();
		}
	}

	public void writeTofile() {
		// file write path???

		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try {
			// do not overwrite a new file
			fWriter = new FileWriter("C:\\Users\\Suchith\\Downloads\\DS\\ds\\UserDetails.txt", true);
			writer = new BufferedWriter(fWriter);
			String userInfo = userName + " " + emailID + " " + password + " " + role;
			writer.write(userInfo);
			writer.newLine();
			writer.close();
			System.err.println("Your input of " + userInfo.length() + " characters was saved.");
		} catch (Exception e) {
			System.out.println("Error!");
		}
	}

}
