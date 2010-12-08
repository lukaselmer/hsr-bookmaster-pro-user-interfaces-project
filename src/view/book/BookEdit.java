package view.book;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;

import view.BookMasterUiManager;
import view.book_master.SubFrame;
import domain.Book;

public class BookEdit extends BookForm implements SubFrame<Book>, Observer {
	protected final Book book;
	protected Book originalBook;

	/**
	 * The view to edit a book
	 * 
	 * @param uimanager
	 * @param book
	 */
	public BookEdit(BookMasterUiManager uimanager, Book book) {
		super(uimanager);
		this.book = book;
		updateForm(book);
		book.addObserver(this);
	}

	@Override
	protected void beforeDispose() {
		book.deleteObserver(this);
	}

	protected void updateForm(Book b) {
		this.originalBook = new Book(b);
		txtName.setText(b.getName());
		txtAuthor.setText(b.getAuthor());
		txtPublisher.setText(b.getPublisher());
		cmbShelf.setSelectedItem(b.getShelf());
		formValidator.validateAll();
	}

	@Override
	protected String getWindowTitle() {
		return "Buchtitel Bearbeiten";
	}

	@Override
	protected void saveBook(Book b) {
		if (frmBookForm.isVisible()) {
			book.updateValues(b);
			JOptionPane.showMessageDialog(frmBookForm, "Buchtitel wurde erfolgreich gespeichert.", "Hinweis",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	public Book getBook() {
		return book;
	}

	@Override
	public Book getObject() {
		return getBook();
	}

	@Override
	public void update(Observable o, Object obj) {
		// if(true)return;
		Book bookByDataInForm = formValidator.validateForm(null);
		Book b = (Book) o;
		if (bookByDataInForm.equals(b)) {
			// Updates in the form are equivalent, so no update or notification
			// is required, but original book should be updated
			this.originalBook = new Book(b);
			return;
		}
		boolean updateForm = false;
		if (bookByDataInForm.equals(originalBook)) {
			// This book has not yet been changed in this window, so override
			// the current form values with the new values
			updateForm = true;
		} else {
			int n = JOptionPane.showConfirmDialog(frmBookForm,
					"Der Buchtitel wurde über ein anderes Fenster bearbeitet. Wollen Sie die Änderungen in diesem Fenster überschreiben?",
					"Buchtitel geändert", JOptionPane.YES_NO_OPTION);
			updateForm = n == JOptionPane.YES_OPTION;
		}
		if (updateForm) {
			updateForm(book);
		}
	}

	@Override
	protected String getSaveButtonTitle() {
		return "Änderungen Speichern";
	}
}
