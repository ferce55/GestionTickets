package yourturn.client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Consumidor implements ActionListener {

	private JFrame frame;
	private String[] colas = { "Pescadería", "Carnicería" };
	private String tipoCola = "";
	private int tipoTicket = -1;
	private Ticket currentTicket = null;
	private JLabel lblTurno;
	private JButton btnSiguiente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Consumidor window = new Consumidor();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the application.
	 */
	public Consumidor() {

		this.frame = new JFrame("Consumidor");
		this.tipoCola = (String) JOptionPane.showInputDialog(this.frame, "¿Estamos en la?", "Tipo de Consumidor",
				JOptionPane.QUESTION_MESSAGE, null, this.colas, this.colas[0]);

		if (this.tipoCola.equals("Pescadería")) {
			this.tipoTicket = Ticket.PESCADO;
		} else {
			this.tipoTicket = Ticket.CARNE;
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		this.frame.setTitle(tipoCola);
		this.frame.setBounds(100, 100, 450, 300);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(null);

		this.btnSiguiente = new JButton("Siguiente");
		this.btnSiguiente.setBounds(171, 118, 89, 23);
		this.frame.getContentPane().add(btnSiguiente);
		this.btnSiguiente.addActionListener(this);

		this.lblTurno = new JLabel(Ticket.CODE[this.tipoTicket] + "--");
		this.lblTurno.setBounds(191, 300, 46, 14);
		this.frame.getContentPane().add(lblTurno);

		currentTicket = new Ticket(this.tipoTicket, Ticket.NULL_TICKET);

	}

	// accion cuando pulsamos botones
	public void actionPerformed(ActionEvent e) {
		this.lblTurno.setText("");

		if (e.getSource() == btnSiguiente) {
			Intercambio intercambio = new Intercambio(Intercambio.ACCION.SIGUIENTE, this.currentTicket);

			System.out.println("pidiendo siguiente  ticket al [" + this.currentTicket.toString() + "]");

			siguiente(intercambio);

		}

	}//

	public void siguiente(Intercambio intercambio) {

		System.out.println("pidiendo numero Consumidor:");
		String Host = "localhost";
		int Puerto = 6000;
		Socket cliente = null;

		try {
			cliente = new Socket(Host, Puerto);
			ObjectOutputStream oStream = new ObjectOutputStream(cliente.getOutputStream());
			ObjectInputStream iStream = new ObjectInputStream(cliente.getInputStream());

			oStream.reset();
			oStream.writeObject(intercambio);

			Intercambio in;
			in = (Intercambio) iStream.readObject();

			currentTicket = in.getTicket();
			lblTurno.setText(currentTicket.toString());

			cliente.close();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			this.lblTurno.setText("Error! (code1)");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			this.lblTurno.setText("Error! (code2)");
		} finally {

		}

		/**********************
		 * @todo conectar al servidor String host = "localhost"; int port = 6000;
		 *
		 *       try {
		 * 
		 *       //Envio petición de ticket con el Intercambio
		 * 
		 * 
		 *       //recibo ticket a través del intercambio Intercambio
		 *       intercambioDevuelto ... currentTicket= in.getTicket();
		 *       lblTurno.setText(currentTicket.toString());
		 * 
		 * 
		 * 
		 *       }catch (ClassNotFoundException cnfe){ cnfe.printStackTrace();
		 *       this.lblTurno.setText("Error! (code1)"); }catch (IOException ioe){
		 *       ioe.printStackTrace(); this.lblTurno.setText("Error! (code2)");
		 *       }finally {
		 * 
		 *       }
		 **************/
	}
}
