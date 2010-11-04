package view;

import java.awt.ComponentOrientation;
import java.awt.Font;

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
}
