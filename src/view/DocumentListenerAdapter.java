package view;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public abstract class DocumentListenerAdapter implements DocumentListener {

	public abstract void documentUpdate(DocumentEvent e);

	@Override
	public void insertUpdate(DocumentEvent e) {
		documentUpdate(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		documentUpdate(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		documentUpdate(e);
	}
}
