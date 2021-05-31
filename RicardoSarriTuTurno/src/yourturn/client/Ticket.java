package yourturn.client;

import java.io.Serializable;
import java.text.NumberFormat;



public class Ticket implements Serializable{

	/**
	 * 
	 */
	
	public static final int CARNE = 0;
	public static final int PESCADO = 1;
	public static final int NULL_TICKET = -1;
	
	public static final String CODE[] = {"C","P"};
	public static final String LONG_CODE[] = {"CARNICERÍA","PESCADERÍA"};
	private static NumberFormat nf;
	static { 
		nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
	}
	
	
	
	private int tipo;
	private int numero;
	
	
	public Ticket(int tipo, int numero) {
		this.tipo=tipo;
		this.numero=numero;
		
	}
	
	
	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	
	
	public String toString() {
		
		String retString="Nadie.";
		if(NULL_TICKET!=numero) {
			retString= CODE[tipo]+nf.format(numero);
		}
		return retString;
	}
}
