package view;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

public class BookMasterActions {

	public static abstract class ActClose extends AbstractAction {
		private static final long serialVersionUID = -3249467952876507516L;

		public ActClose() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_L);
			putValue(SHORT_DESCRIPTION, "Schliesst aktuelles Fenster");
			putValue(NAME, "Schliessen");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));
			putValue(SMALL_ICON, new ImageIcon("data/icons/false.png"));
		}

		@Override
		public abstract void actionPerformed(ActionEvent arg0);
	}

	public static abstract class ActEnterPressed extends AbstractAction {
		private static final long serialVersionUID = -6339576504337199197L;

		public ActEnterPressed() {
			putValue(NAME, "Eingabe bestätigen");
		}

		@Override
		public abstract void actionPerformed(ActionEvent arg0);
	}

	public static abstract class ActHelp extends AbstractAction {
		private static final long serialVersionUID = -6338712504337199197L;

		public ActHelp() {
			putValue(NAME, "Hilfe");
			putValue(MNEMONIC_KEY, KeyEvent.VK_H);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
			putValue(SHORT_DESCRIPTION, "Zeigt die Hilfe an");
			putValue(SMALL_ICON, new ImageIcon("data/icons/help.png"));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String s = "Zeige hier die Hilfe an... (es wurde keine Benutzerhilfe geschrieben, deshalb wird hier nicht auf die Hilfe verlinkt)";
			JOptionPane.showMessageDialog(getFrame(), s, "Hilfe", JOptionPane.INFORMATION_MESSAGE);
		}

		public abstract JFrame getFrame();
	}

	public static abstract class ActCancel extends AbstractAction {
		private static final long serialVersionUID = 8540992059127356091L;

		public ActCancel() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_A);
			putValue(SHORT_DESCRIPTION, "Abbrechen");
			putValue(NAME, "Bricht aktuelle Aktion ab");
			putValue(SMALL_ICON, new ImageIcon("data/icons/cancel.png"));
		}
	}
}
