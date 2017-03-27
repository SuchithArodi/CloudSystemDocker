import java.io.*;

public class Test {
	
	
	public static void main(String [] atrgs) throws IOException
	{
	 BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Suchith\\workspace\\Load_Balancer\\src\\ListOfIPsserver.txt"));
	 String line = null;
	 while ((line = br.readLine()) != null) {
	   System.out.println(line);
	 }}

}
