import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Server {
	private int port = 1234;
	private SimpleDateFormat temps;
	private ArrayList<ClientThread> allClients;
	private static int serverNum;
	private boolean cont;
	
	
	public Server (int port){
		this.port = port;
		this.temps = new SimpleDateFormat("HH:mm:ss");
		this.allClients = new ArrayList<ClientThread>();
	}
	
	public void start(){
		cont = true;
		
		try {
			ServerSocket server = new ServerSocket(port);
			while (cont){
				afficher("Serveur en attente de clients sur le port " + port + ".");
				Socket s = server.accept();
				if (!cont)
					break;
				ClientThread clientT = new ClientThread(s);
				allClients.add(clientT);
				clientT.start();
			}
			
			server.close();
			//for (ClientThread clientThread : allClients) {
				//close input
				//close output
				//close socket
				//remove from list
			//}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stop(){
		cont = false;
		try {
			new Socket("localhost", port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void afficher(String msg) {
		String line = temps.format(new Date())+ " : " + msg;
		System.out.println(line);
	}
	
	public synchronized void broadcastMsg(String msg){
		String tmp = temps.format(new Date());
		String line = tmp + " : "+ msg + "\n";
		
		System.out.print(line);
		for (ClientThread clientThread : allClients) {
			if(!clientThread.printMsg(line)){
				allClients.remove(clientThread);
				afficher(tmp +" : Client deconnecte "+ clientThread.login+ ".");
			}
		}
	}
	
	synchronized void supprimer(int num) {
		for (ClientThread clientThread : allClients) {
			if (clientThread.num == num)
				allClients.remove(clientThread);
			break;
		}
	}
	
	
	public boolean checkPwd(String login, String pwd){
		return true;
	}
	
	class ClientThread extends Thread{
		protected int num;
		protected String login;
		private Socket socket;
		ObjectInputStream in;
		ObjectOutputStream out;
		ChatMessage objectMessage;
		String time;
		

		ClientThread(Socket socket) {
			num = serverNum + 1;
			this.socket = socket;
			System.out.println("Création de la liaison serveur client ...");
			
			try {
				in = new ObjectInputStream(socket.getInputStream());
				out = new ObjectOutputStream(socket.getOutputStream());
				login = (String) in.readObject();
				afficher(login + " vient de se connecter.");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			time = new Date().toString() + "\n";
		}
		
		public void run(){
			boolean cont = true;
			while (cont){
				try {
					objectMessage = (ChatMessage) in.readObject();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String message = objectMessage.getMessage();
				
				switch (objectMessage.getType()) {
				case ChatMessage.MESSAGE:
					broadcastMsg(login + " : " + message);
					break;
				case ChatMessage.WHOISIN:
					printMsg("Liste des utilisateurs connectés : ");
					for (ClientThread clientThread : allClients) {
						int i = 1;
						printMsg(i + "# " + clientThread.login + " depuis :" + clientThread.time);
						i++;
					}
					break;
				case ChatMessage.LOGOUT:
					printMsg(login + " s'est deconnecte.");
					cont = false;
					break;
				}
				
			}
			supprimer(num);
			close();
		}
		
		private void close() {
			try {
				if(out != null) out.close();
			}
			catch(Exception e) {}
			try {
				if(in != null) in.close();
			}
			catch(Exception e) {};
			try {
				if(socket != null) socket.close();
			}
			catch (Exception e) {}
		}
		

		public boolean printMsg(String message) {
			if(!socket.isConnected()){
				close();
				return false;
			}
			try {
				out.writeObject(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		}
	}
	
	public static void main(String[] args) {
		int portNumber = 1234;
		switch(args.length) {
			case 1:
				try {
					portNumber = Integer.parseInt(args[0]);
				}
				catch(Exception e) {
					System.out.println("Invalid port number.");
					System.out.println("Usage is: > java Server [portNumber]");
					return;
				}
			case 0:
				break;
			default:
				System.out.println("Usage is: > java Server [portNumber]");
				return;
				
		}
		Server server = new Server(portNumber);
		server.start();
	}

}
