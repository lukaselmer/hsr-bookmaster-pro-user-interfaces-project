package view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

public class BookMasterActions {
	
	public static abstract class ActClose extends AbstractAction{
		private static final long serialVersionUID = -3249467952876507516L;
		public ActClose(){
			putValue(MNEMONIC_KEY, KeyEvent.VK_S);
			putValue(SHORT_DESCRIPTION, "Schliesst aktuelles Fenster");
			putValue(NAME, "Schliessen");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));		
		}
		@Override
		public abstract void actionPerformed(ActionEvent arg0);
	}
}
