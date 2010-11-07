package view.book;

import java.awt.EventQueue;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import application.LibraryApp;
import domain.Book;
import domain.Library;

public class BookNew extends BookForm {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
					new BookNew(LibraryApp.inst());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public BookNew(Library library) {
		super(library);
	}

	@Override
	protected String getWindowTitle() {
		return "Neuer Buchtitel";
	}

	@Override
	protected String getSaveButtonTitle() {
		return "Buchtitel Erfassen";
	}

	@Override
	protected void saveBook(Book b) {
		library.addBook(b);
		JOptionPane.showMessageDialog(frmBookForm, "Buchtitel wurde erfolgreich erstellt und der Buchtiteltabelle hinzugefügt.", "Hinweis",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
