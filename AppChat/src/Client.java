import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {

	public static void main(String[] args) {
		String host = "localhost";
		int portNumber = 1234;
		try {
			Socket cliSocket = new Socket(host, portNumber);
			PrintWriter out = new PrintWriter(cliSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader( new InputStreamReader(cliSocket.getInputStream()));
			System.out.println("Message reçu: ");

			BufferedReader stdIn =
	                new BufferedReader(new InputStreamReader(System.in));
	            String fromServer;
	            String fromUser;

	            while ((fromServer = in.readLine()) != null) {
	                System.out.println("Server: " + fromServer);
	                if (fromServer.equals("Bye."))
	                    break;
	                
	                fromUser = stdIn.readLine();
	                if (fromUser != null) {
	                    System.out.println("Client: " + fromUser);
	                    out.println(fromUser);
	                }
	            }
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
