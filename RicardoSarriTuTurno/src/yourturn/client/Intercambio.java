package yourturn.client;

import java.io.Serializable;

public class Intercambio implements Serializable {
	/**
	 * 
	 */
	

	public static enum ACCION {
			PIDE_TICKET,
			SIGUIENTE
	}
	
	private ACCION accion;
	private Ticket ticket;
		
	public Intercambio() {
		
	}
	
	public Intercambio(ACCION accion, Ticket ticket) {
		
		this.setAccion(accion);
		this.setTicket(ticket);
		
		
	}

	public ACCION getAccion() {
		return accion;
	}

	public void setAccion(ACCION accion) {
		this.accion = accion;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	
	
	
}
