package view.book;

import java.awt.EventQueue;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import view.book_master.SubFrame;

import domain.Book;
import domain.Library;

import application.LibraryApp;

public class BookEdit extends BookForm implements SubFrame<Book> {
	private Book book;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
					Library l = LibraryApp.inst();
					new BookEdit(l, l.getBooks().get(new Random().nextInt(l.getBooks().size())));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public BookEdit(Library library, Book book) {
		super(library);
		this.book = book;
		txtName.setText(book.getName());
		txtAuthor.setText(book.getAuthor());
		txtPublisher.setText(book.getPublisher());
		// txtCity.setText(book.getCity());
		// txtZip.setText("" + book.getZip().intValue());
		formValidator.validateAll();
	}

	@Override
	protected String getWindowTitle() {
		return "Buchtitel Bearbeiten";
	}

	@Override
	protected String getSaveButtonTitle() {
		return "Änderungen Speichern";
	}

	@Override
	protected void saveBook(Book b) {
		book.updateValues(b);
		JOptionPane.showMessageDialog(frmBookForm, "Buchtitel wurde erfolgreich gespeichert.", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
	}

	public Book getBook() {
		return book;
	}

	@Override
	public Book getObject() {
		return getBook();
	}
}
