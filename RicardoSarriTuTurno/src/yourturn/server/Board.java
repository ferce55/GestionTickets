package yourturn.server;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import yourturn.client.Ticket;
import yourturn.client.Intercambio;

import java.awt.Font;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Board {

	private JFrame frame;

	private static final int hist_turnos = 5;

	private static int numero = -1;
	private static final int maxNumero = 100;
	@SuppressWarnings("unchecked")
	private static final ArrayList<Ticket>[] tickets = new ArrayList[2];
	private static int[] turnos = { -1, -1 };
	private static ArrayList<JLabel> lblTurns = new ArrayList<JLabel>();

	/**
	 * Launch the application.
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		tickets[0] = new ArrayList<Ticket>();
		tickets[1] = new ArrayList<Ticket>();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Board window = new Board();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		int puerto = 6000;// Puerto
		ServerSocket server = new ServerSocket(puerto);
		Socket cliente = null;
		try {
			while (true) {

				cliente = new Socket();
				cliente = server.accept();
				ObjectOutputStream oStream = new ObjectOutputStream(cliente.getOutputStream());
				ObjectInputStream iStream = new ObjectInputStream(cliente.getInputStream());

				Intercambio intercambio = (Intercambio) iStream.readObject();
				int tipo = intercambio.getTicket().getTipo();

				switch (intercambio.getAccion()) {
				case PIDE_TICKET:
					numero = tickets[tipo].size();
					if (numero > maxNumero) {
						numero = numero - maxNumero;
					}
					Ticket ticket = new Ticket(tipo, numero);
					intercambio.setTicket(ticket);
					tickets[tipo].add(ticket);
					oStream.reset();
					oStream.writeObject(intercambio);
					break;
				case SIGUIENTE:
					if ((turnos[tipo] + 1) > (tickets[tipo].size() - 1) || tickets[tipo].size() == 0) {
						ticket = new Ticket(tipo, -1);
					} else {
						turnos[tipo]++;
						ticket = new Ticket(tipo, turnos[tipo]);
						lblTurns.add(new JLabel(ticket.toString()));
						historico(ticket);
					}
					intercambio.setTicket(ticket);
					oStream.reset();
					oStream.writeObject(intercambio);
					break;
				default:
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("");
		}

		cliente.close();
		server.close();

		/*********************
		 * @todo recoger Intercambio y según la accion
		 * 
		 *       int numeroPuerto = 6000;// Puerto try { while(true) { //Intercambio
		 *       intercambio = new Intercambio(); switch (intercambio.getAccion()) {
		 * 
		 *       case PIDE_TICKET: //hacer algo...
		 * 
		 *       break; case SIGUIENTE: //hacer algo... break; default: ///do nothing;
		 *       break; }
		 * 
		 *       } }catch(Exception e) { e.printStackTrace(); }finally { }
		 *****************/
	}

	/**
	 * Create the application.
	 */
	public Board() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Tablón de turnos");
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);

		JLabel lblHora = new JLabel("hora");
		lblHora.setBounds(331, 11, 93, 14);
		panel.add(lblHora);

		miHilo h = new miHilo(lblHora);
		for (int i = 0; i < hist_turnos; i++) {
			JLabel lblTurn_i = new JLabel("");
			lblTurn_i.setBounds(100, 50 + i * 15, 380, 60 + i * 15);
			if (i == 0) {
				lblTurn_i.setFont(new Font("Tahoma", Font.PLAIN, 20));
			}
			panel.add(lblTurn_i);
			lblTurns.add(lblTurn_i);

		}
		new Thread(h).start();

	}

	public static void historico(Ticket ticketAhora) {

		for (int i = hist_turnos; i > 1; i--) {
			JLabel lblTurn_i = lblTurns.get(i - 1);
			JLabel lblTurn_i_1 = lblTurns.get(i - 2);
			String text = lblTurn_i_1.getText();
			lblTurn_i.setText(text);
		}
		JLabel lblTurn_0 = lblTurns.get(0);
		lblTurn_0.setText(Ticket.LONG_CODE[ticketAhora.getTipo()] + ": " + ticketAhora);

	}

}
