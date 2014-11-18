import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.print.attribute.standard.Severity;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class IhmClient extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField msgField, serverAddress, serverPort;
	private JButton loginBtn, logoutBtn, whoIsInBtn;
	private JTextArea broadcastArea;
	private boolean connecte;
	private Client client;
	
	private int defPort;
	private String defAddress;
	
	IhmClient (String address, int port){
		super();
		defAddress = address;
		defPort = port;
		
		setTitle("Client");
		setSize(700,500); 
		setLocation(50,200); 
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new GridLayout(2, 1));
		
		serverAddress.setText("127.0.0.1");
		serverPort.setText("1234");
		
		JPanel pConnect = new JPanel(new GridLayout(2,1));
		pConnect.add(serverAddress);
		pConnect.add(serverPort);
		JPanel pChat = new JPanel(new GridLayout(1,1));
		
		this.add(pConnect);
		
		serverAddress = new JTextField(address);
		serverPort = new JTextField(port);
		serverPort.setHorizontalAlignment(SwingConstants.RIGHT);
	}
	
	 public static void main (String [] args) {
		 IhmClient cli = new IhmClient("127.0.0.1", 1234);
	 }

}
