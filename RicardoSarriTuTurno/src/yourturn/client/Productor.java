package yourturn.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.SwingConstants;

public class Productor implements ActionListener {

	private JFrame frame;
	private JButton btnPescado;
	private JButton btnCarne;
	private JLabel lblTurno;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Productor window = new Productor();
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
	public Productor() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Elija un ticket...");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		btnCarne = new JButton("Carnicería");
		btnCarne.setBounds(41, 123, 153, 23);
		frame.getContentPane().add(btnCarne);
		btnCarne.addActionListener(this);

		btnPescado = new JButton("Pescadería");
		btnPescado.setBounds(243, 123, 153, 23);
		frame.getContentPane().add(btnPescado);
		btnPescado.addActionListener(this);

		lblTurno = new JLabel("");
		lblTurno.setHorizontalAlignment(SwingConstants.CENTER);
		lblTurno.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTurno.setBounds(139, 35, 153, 57);
		frame.getContentPane().add(lblTurno);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	// accion cuando pulsamos botones
	public void actionPerformed(ActionEvent e) {
		lblTurno.setText("");
		int tipoTicket = 0;
		if (e.getSource() == btnCarne) {
			tipoTicket = Ticket.CARNE;
		} else if (e.getSource() == btnPescado) {
			tipoTicket = Ticket.PESCADO;
		} else {
			return;
		}

		Intercambio intercambio = new Intercambio(Intercambio.ACCION.PIDE_TICKET,
				new Ticket(tipoTicket, Ticket.NULL_TICKET));
		pideNumero(intercambio);
	}//

	public void pideNumero(Intercambio intercambio) {

		String Host = "localhost";
		int Puerto = 6000;// puerto remoto
		Socket cliente = null;

		try {

			cliente = new Socket(Host, Puerto);

			// Conectar al servidor y enviar Ticket con el objeto intercambio
			ObjectOutputStream oStream = new ObjectOutputStream(cliente.getOutputStream());
			ObjectInputStream iStream = new ObjectInputStream(cliente.getInputStream());

			// enviar Ticket con el objeto intercambio
			oStream.reset();
			oStream.writeObject(intercambio);

			// Recuperar el nuevo ticket y e incluir en el Label
			Intercambio intercambioDevuelto = (Intercambio) iStream.readObject();
			Ticket ticket = intercambioDevuelto.getTicket();
			lblTurno.setText(ticket.toString());

		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			lblTurno.setText("Error! (code1)");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			lblTurno.setText("Error! (code2)");
		} finally {
		}

		/**
		 * @TODO
		 *
		 *       String host = "localhost"; int port = 6000;//puerto remoto
		 * 
		 *       try { //Conectar al servidor y enviar Ticket con el objeto intercambio
		 * 
		 *       //enviar Ticket con el objeto intercambio
		 * 
		 * 
		 *       // Recuperar el nuevo ticket y e incluir en el Label Intercambio
		 *       intercambioDevuelto.... Ticket ticket= intercambioDevuelto.getTicket();
		 *       lblTurno.setText(ticket.toString());
		 * 
		 * 
		 * 
		 *       }catch (ClassNotFoundException cnfe){ cnfe.printStackTrace();
		 *       lblTurno.setText("Error! (code1)"); }catch (IOException ioe){
		 *       ioe.printStackTrace(); lblTurno.setText("Error! (code2)"); }finally { }
		 ********/
	}
}
