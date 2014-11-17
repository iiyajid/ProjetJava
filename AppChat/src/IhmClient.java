import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class IhmClient extends JFrame implements ActionListener {
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
		
		JPanel haut = new JPanel(new GridLayout(3,1));
		JPanel bas = new JPanel(new GridLayout(1,5, 1, 3));
		
		serverAddress = new JTextField(address);
		serverPort = new JTextField(port);
		serverPort.setHorizontalAlignment(SwingConstants.RIGHT);
	}
	

}
