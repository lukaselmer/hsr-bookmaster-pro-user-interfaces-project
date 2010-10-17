package view.book_master;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import domain.Book;
import domain.Copy;
import domain.Library;
import domain.Loan;

public class BookMasterTableModelLoan extends AbstractTableModel {

	public enum ColumnName {
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

	private static final long serialVersionUID = 8466707343843649023L;
	ColumnName[] columnNames = ColumnName.values();
	Library library;
	List<Loan> currentLoans;

	public BookMasterTableModelLoan(Library library) {
		this.library = library;
		currentLoans = library.getCurrentLoans();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return currentLoans.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Loan l = currentLoans.get(row);
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
	public String getColumnName(int column) {
		return "" + columnNames[column];
	}

	public void updateLoans(List<Loan> newLoans) {
		currentLoans = newLoans;
	}
}
