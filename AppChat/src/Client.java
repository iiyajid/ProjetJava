import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Client {
	private ObjectInputStream in;		// to read from the socket
	private ObjectOutputStream out;		// to write on the socket
	private Socket socket;
	private String server = "127.0.0.1";
	private String login;
	private int port = 1234;
	private IhmClient ihmCli;
	
	
	Client(String server, int port, String login) {
		this.server = server;
		this.port = port;
		this.login = login;
	}
	
	public boolean start(){
		try {
			socket = new Socket(server, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		String message = "Connexion acceptee " + socket.getInetAddress() + ":" + socket.getPort();
		printMsg(message);
		
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		new EcouterServer().start();
		
		try {
			out.writeObject(login);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void printMsg(String message) {
		System.out.println(message);
	}
	
	public void envoyerMessage(ChatMessage message){
		try {
			out.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void close() {
		if (in != null)
			try {
				if (in != null)
					in.close();
				if (out != null)
					out.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	class EcouterServer extends Thread {

		public void run() {
			while(true) {
				try {
					String msg = (String) in.readObject();
					if(ihmCli == null) {
						System.out.println(msg);
						System.out.print("> ");
					}
					else {
						//ihmCli.append(msg);
					}
				}
				catch(IOException e) {
					printMsg("Server has close the connection: " + e);
					if(ihmCli != null) 
						//ihmCli.connectionFailed();
					break;
				}
				// can't happen with a String object but need the catch anyhow
				catch(ClassNotFoundException e2) {
				}
			}
		}
	}
	
	
	public static void main(String[] args) {
		int portNumber = 1234;
		String serverAddress = "127.0.0.1";
		String userName = "Anonymous";

		switch(args.length) {
			case 3:
				serverAddress = args[2];
			case 2:
				try {
					portNumber = Integer.parseInt(args[1]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Client [username] [portNumber] [serverAddress]");
					return;
				}
			case 1: 
				userName = args[0];
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Client [username] [portNumber] {serverAddress]");
			return;
		}
		// create the Client object
		Client client = new Client(serverAddress, portNumber, userName);
		if(!client.start())
			return;
		
		Scanner scan = new Scanner(System.in);
		while(true) {
			System.out.print("> ");
			String msg = scan.nextLine();
			if(msg.equalsIgnoreCase("LOGOUT")) {
				client.envoyerMessage(new ChatMessage(ChatMessage.LOGOUT, ""));
				break;
			}
			else if(msg.equalsIgnoreCase("WHOISIN")) {
				client.envoyerMessage(new ChatMessage(ChatMessage.WHOISIN, ""));				
			}
			else {				// default to ordinary message
				client.envoyerMessage(new ChatMessage(ChatMessage.MESSAGE, msg));
			}
		}
		client.close();	
	}
}
