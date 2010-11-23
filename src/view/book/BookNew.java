package view.book;

import java.awt.EventQueue;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import view.BookMasterUiManager;
import application.LibraryApp;
import domain.Book;

public class BookNew extends BookForm {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
					new BookNew(new BookMasterUiManager(LibraryApp.inst()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
		JOptionPane.showMessageDialog(frmBookForm, "Buchtitel wurde erfolgreich erstellt und der Buchtiteltabelle hinzugefügt.", "Hinweis",
				JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	protected String getSaveButtonString() {
		return "Buchtitel Erfassen";
	}
}
