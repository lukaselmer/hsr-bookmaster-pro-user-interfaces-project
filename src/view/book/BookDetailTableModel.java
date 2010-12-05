package view.book;

import java.util.ArrayList;
import java.util.List;

import view.BookMasterTableModel;
import domain.Book;
import domain.Copy;
import domain.Library;
import domain.Loan;

public class BookDetailTableModel extends BookMasterTableModel<Copy> {
	private static final long serialVersionUID = 1457079195819888790L;

	public enum ColumnName implements AbstractColumnName {
		INVENTORY_NUMBER("Exemplar-ID"), AVAILABILITY("Verfügbarkeit"), COPY_CONDITION("Zustand");
		private String name;

		private ColumnName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private Book book;

	public BookDetailTableModel(Library library, Book book) {
		super(library);
		this.book = book;
		currentObjects = new ArrayList<Copy>(getInitialObjects());
	}

	@Override
	protected List<Copy> getInitialObjects() {
		return library.getCopiesOfBook(book);
	}

	@Override
	protected AbstractColumnName[] getColumnNames() {
		return ColumnName.values();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (currentObjects.size() == 0)
			return new Copy(book);
		Copy c = currentObjects.get(row);
		if (col == -1) {
			return c;
		} else if (getColumnName(col).equals(ColumnName.INVENTORY_NUMBER.toString())) {
			return c.getInventoryNumber();
		} else if (getColumnName(col).equals(ColumnName.AVAILABILITY.toString())) {
			String s = "";
			if (c.isLent()) {
				if (c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() == 0)
					s = "Ausgeliehen bis " + Loan.getFormattedDate(c.getCurrentLoan().getDueDate()) + " (heute)";
				else if (c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() == 1)
					s = "Ausgeliehen bis " + Loan.getFormattedDate(c.getCurrentLoan().getDueDate()) + " (noch "
							+ c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() + " Tag)";
				else if (c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() > 1)
					s = "Ausgeliehen bis " + Loan.getFormattedDate(c.getCurrentLoan().getDueDate()) + " (noch "
							+ c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() + " Tage)";
				else if (c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() == -1)
					s = "Ausgeliehen bis " + Loan.getFormattedDate(c.getCurrentLoan().getDueDate()) + " ("
							+ -c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() + " Tag überfällig)";
				else
					s = "Ausgeliehen bis " + Loan.getFormattedDate(c.getCurrentLoan().getDueDate()) + " ("
							+ -c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() + " Tage überfällig)";
			} else {
				s = "Verfügbar";
			}
			return s;
		} else if (getColumnName(col).equals(ColumnName.COPY_CONDITION.toString())) {
			return c.getCondition();
		} else {
			assert false; // Execution should never reach this point: Undefined column name!
			return null;
		}
	}

	@Override
	public int getDefaultSortedColumn() {
		return ColumnName.INVENTORY_NUMBER.ordinal();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return columnIndex == ColumnName.COPY_CONDITION.ordinal();
	}

	public void updateObjects(Book book) {
		this.book = book;
		updateObjects(library.getCopiesOfBook(book));
	}

	// public void updateCopies(List<Copy> newCopies) {
	// currentCopies = newCopies;
	// fireTableDataChanged();
	// }

}
