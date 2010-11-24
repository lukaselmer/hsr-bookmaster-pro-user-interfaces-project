package view;

import java.awt.ComponentOrientation;
import java.awt.Font;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdesktop.swingx.JXTitledSeparator;

public class ViewUtil {
	private ViewUtil() {
	}

	public static JXTitledSeparator getSeparator(String text) {
		JXTitledSeparator separator = new JXTitledSeparator();
		separator.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		separator.setFont(new Font(separator.getFont().getName(), Font.BOLD, separator.getFont().getSize()));
		separator.setTitle(text);
		return separator;
	}

	public static JTextArea getTextArea(String text) {
		JTextArea textArea = new JTextArea(text);
		textArea.setBorder(new JTextField().getBorder());
		textArea.setEditable(false);
		textArea.setColumns(10);
		textArea.setLineWrap(true);
		return textArea;
	}

	public static JTextField getTextField(String text) {
		JTextField textField = new JTextField(text);
		textField.setCaretPosition(0);
		textField.setEditable(false);
		textField.setColumns(10);
		return textField;
	}

	public static JMenu getHelpMenu(JFrame frame) {
		JMenu mnHelp = new JMenu("Hilfe");
		JMenuItem mnItemHelp = new JMenuItem(getHelpAction(frame));
		mnHelp.add(mnItemHelp);
		return mnHelp;
	}

	private static Action getHelpAction(final JFrame frame) {
		return new BookMasterActions.ActHelp() {
			private static final long serialVersionUID = 4091420013220897849L;

			@Override
			public JFrame getFrame() {
				return frame;
			}
		};
	}
}
