package view.book;

import javax.swing.JOptionPane;

import view.BookMasterUiManager;
import domain.Book;

public class BookNew extends BookForm {
	/**
	 * The view to create a new book
	 * 
	 * @param uimanager
	 */
	public BookNew(BookMasterUiManager uimanager) {
		super(uimanager);
	}

	@Override
	protected String getWindowTitle() {
		return "Neuer Buchtitel";
	}

	@Override
	protected void saveBook(Book b) {
		library.addBook(b);
		library.createAndAddCopy(b);
		JOptionPane.showMessageDialog(frmBookForm, "Buchtitel wurde erfolgreich erstellt und der Buchtiteltabelle hinzugefügt.", "Hinweis",
				JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	protected String getSaveButtonString() {
		return "Buchtitel Erfassen";
	}
}
