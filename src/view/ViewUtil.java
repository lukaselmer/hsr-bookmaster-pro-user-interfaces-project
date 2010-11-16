package view;

import java.awt.ComponentOrientation;
import java.awt.Font;

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
	
	public static JTextField getTextField(String text){
		JTextField textField = new JTextField(text);
		textField.setEditable(false);
		textField.setColumns(10);
		return textField;
	}
}
