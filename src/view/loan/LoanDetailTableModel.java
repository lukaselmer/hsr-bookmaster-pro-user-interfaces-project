package view.loan;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import view.BookMasterTableModel;
import view.BookMasterTableModel.AbstractColumnName;
import view.book.BookDetailTableModel.ColumnName;

import domain.Book;
import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Loan;
import domain.Shelf;

public class LoanDetailTableModel extends BookMasterTableModel<Loan> {

	private static final long serialVersionUID = 3423188203390577160L;

	private Customer customer;

	public enum ColumnName implements AbstractColumnName {
		STATUS("Status"), LOAN_UNTIL("Ausgeliehen Bis"), INVENTORY_NUMBER("Exemplar-ID"), TITLE("Titel"), AUTHOR("Author");
		private String name;

		private ColumnName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public LoanDetailTableModel(Library library, Customer customer) {
		super(library);
		this.customer = customer;
		currentObjects = new ArrayList<Loan>(getInitialObjects());
	}

	@Override
	protected List<Loan> getInitialObjects() {
		return library.getCustomerLoans(customer);
	}

	@Override
	protected AbstractColumnName[] getColumnNames() {
		return ColumnName.values();
	}

	@Override
	public Object getValueAt(int row, int col) {
		if (currentObjects.size() == 0) {
			return new Loan(new Customer("", ""), new Copy(new Book("", "", "", Shelf.A1)));
		}
		Loan l = currentObjects.get(row);
		Copy c = l.getCopy();
		Book b = l.getBook();
		if (col == -1) {
			return l;
		} else if (getColumnName(col).equals(ColumnName.STATUS.toString())) {
			return l.isOverdue() ? "Fällig" : "Ok";
		} else if (getColumnName(col).equals(ColumnName.INVENTORY_NUMBER.toString())) {
			return c.getInventoryNumber();
		} else if (getColumnName(col).equals(ColumnName.LOAN_UNTIL.toString())) {
			return Loan.getFormattedDate(l.getDueDate());
		} else if (getColumnName(col).equals(ColumnName.TITLE.toString())) {
			return b.getName();
		} else if (getColumnName(col).equals(ColumnName.AUTHOR.toString())) {
			return b.getAuthor();
		} else {
			assert false; // Execution should never reach this point: Undefined
							// column name!
			return null;
		}
	}

	@Override
	public int getDefaultSortedColumn() {
		return ColumnName.INVENTORY_NUMBER.ordinal();
	}

	public void updateObjects(Customer customer) {
		this.customer = customer;
		updateObjects(getInitialObjects());
	}
}