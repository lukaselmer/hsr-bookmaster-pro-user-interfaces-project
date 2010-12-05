package view.book_master;

import java.util.List;

import view.BookMasterTableModel;
import domain.Book;
import domain.Library;
import domain.Loan;
import domain.Shelf;

public class BookMasterTableModelBook extends BookMasterTableModel<Book> {
	private static final long serialVersionUID = -9213162719594224055L;

	public enum ColumnName implements AbstractColumnName {
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

	public BookMasterTableModelBook(Library library) {
		super(library);
	}

	@Override
	protected List<Book> getInitialObjects() {
		return library.getBooks();
	}

	@Override
	protected AbstractColumnName[] getColumnNames() {
		return ColumnName.values();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (currentObjects.size() == 0)
			return new Book("", "", "", Shelf.A1);
		Book b = currentObjects.get(row);
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
					Loan l = library.getNextAvailibleCopyOfBook(b).getCurrentLoan();
					return l != null ? "ab " + Loan.getFormattedDate(l.getDueDate()) : "nicht verfügbar";
				} else {
					return "nicht verfügbar";
				}
			}
		} else {
			assert false; // Execution should never reach this point: Undefined column name!
			return null;
		}
	}

	@Override
	public int getDefaultSortedColumn() {
		return ColumnName.TITLE.ordinal();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == ColumnName.SHELF.ordinal();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		// TODO: Exception!
		return getValueAt(0, columnIndex).getClass();
	}
}
