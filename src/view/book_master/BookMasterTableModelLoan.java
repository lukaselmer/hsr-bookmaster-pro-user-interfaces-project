package view.book_master;

import java.util.List;

import view.BookMasterTableModel;
import domain.Book;
import domain.Copy;
import domain.Library;
import domain.Loan;

public class BookMasterTableModelLoan extends BookMasterTableModel<Loan> {

	private static final long serialVersionUID = -2039525012989574974L;

	public enum ColumnName implements AbstractColumnName {
		STATUS("Status"), ID("Exemplar-ID"), TITLE("Titel"), LOAN_UNTIL("Ausgeliehen Bis"), LOAN_TO("Ausgeliehen An");
		private String name;

		private ColumnName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public BookMasterTableModelLoan(Library library) {
		super(library);
	}

	@Override
	protected List<Loan> getInitialObjects() {
		return library.getCurrentLoans();
	}

	@Override
	protected AbstractColumnName[] getColumnNames() {
		return ColumnName.values();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Loan l = currentObjects.get(row);
		Copy c = l.getCopy();
		Book b = c.getBook();
		if (col == -1) {
			return l;
		} else if (getColumnName(col).equals(ColumnName.STATUS.toString())) {
			return l.isOverdue() ? "Fällig" : "Ok";
		} else if (getColumnName(col).equals(ColumnName.ID.toString())) {
			return c.getInventoryNumber();
		} else if (getColumnName(col).equals(ColumnName.TITLE.toString())) {
			return b.getName();
		} else if (getColumnName(col).equals(ColumnName.LOAN_UNTIL.toString())) {
			return Loan.getFormattedDate(l.getDueDate());
		} else if (getColumnName(col).equals(ColumnName.LOAN_TO.toString())) {
			return l.getCustomer();
		} else {
			throw new RuntimeException("Undefined column name!");
		}
	}

	@Override
	public int getDefaultSortedColumn() {
		return ColumnName.TITLE.ordinal();
	}
}
