package yourturn.server;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JLabel;

public class miHilo implements Runnable{
	private JLabel label;
	public miHilo (JLabel label) {
		this.label=label;
	}
	public void run() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		while (1!=0) {
			Calendar cal = Calendar.getInstance();
			String horaActual = sdf.format(cal.getTime());
			this.label.setText(horaActual);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
