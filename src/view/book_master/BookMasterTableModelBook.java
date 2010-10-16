package view.book_master;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import domain.Book;
import domain.Library;
import domain.Loan;

public class BookMasterTableModelBook extends AbstractTableModel {

	public enum ColumnName {
		STATUS("Verfügbar"), SHELF("Regal"), TITLE("Titel"), AUTHOR("Author"), PUBLISHER("Verlag");
		private String name;

		private ColumnName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private static final long serialVersionUID = 8466707343843649023L;
	ColumnName[] columnNames = ColumnName.values();
	Library library;
	List<Book> currentBooks;

	public BookMasterTableModelBook(Library library) {
		this.library = library;
		currentBooks = library.getBooks();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return currentBooks.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Book b = currentBooks.get(row);
		if (col == -1) {
			return b;
		} else if (getColumnName(col).equals(ColumnName.TITLE.toString())) {
			return b.getName();
		} else if (getColumnName(col).equals(ColumnName.SHELF.toString())) {
			return b.getShelf();
		} else if (getColumnName(col).equals(ColumnName.AUTHOR.toString())) {
			return b.getAuthor();
		} else if (getColumnName(col).equals(ColumnName.PUBLISHER.toString())) {
			return b.getPublisher();
		} else if (getColumnName(col).equals(ColumnName.STATUS.toString())) {
			int availibleCopies = library.getAvailibleCopiesOfBook(b).size();
			if (availibleCopies > 0) {
				return availibleCopies == 1 ? "1 Exemplar" : availibleCopies + " Exemplare";
			} else {
				if (library.hasNextAvailibleCopyOfBook(b)) {
					return "ab " + Loan.getFormattedDate(library.getNextAvailibleCopyOfBook(b).getCurrentLoan().getDueDate());
				} else {
					return "nicht verfügbar";
				}
			}
		} else {
			throw new RuntimeException("Undefined column name!");
		}
	}

	@Override
	public String getColumnName(int column) {
		return "" + columnNames[column];
	}

	public void updateBooks(List<Book> newBooks) {
		currentBooks = newBooks;
	}
}
