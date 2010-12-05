package view.book;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import domain.Book;
import domain.Copy;
import domain.Library;
import domain.Loan;

public class BookDetailTableModel extends AbstractTableModel {

	private static final long serialVersionUID = 6585199961830637974L;

	public enum ColumnName {
		INVENTORY_NUMBER("Exemplar-ID"), AVAILABILITY("Verfügbarkeit");
		private String name;

		private ColumnName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	ColumnName[] columnnames = ColumnName.values();
	Library library;
	List<Copy> currentCopies;

	public BookDetailTableModel(Library library, Book book) {
		this.library = library;
		currentCopies = library.getCopiesOfBook(book);
	}

	@Override
	public int getColumnCount() {
		return columnnames.length;
	}

	@Override
	public int getRowCount() {
		return currentCopies.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Copy c = currentCopies.get(row);
		if (col == -1) {
			return c;
		} else if (getColumnName(col).equals(ColumnName.INVENTORY_NUMBER.toString())) {
			return c.getInventoryNumber();
		} else if (getColumnName(col).equals(ColumnName.AVAILABILITY.toString())) {
			String s = "";
			if (c.isLent()){
				if (c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() == 0) s = "Ausgeliehen bis " + Loan.getFormattedDate(c.getCurrentLoan().getDueDate()) + " (heute)";
				else if (c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() == 1) s = "Ausgeliehen bis " + Loan.getFormattedDate(c.getCurrentLoan().getDueDate()) + " (noch " + c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() + " Tag)";
				else if (c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() > 1) s = "Ausgeliehen bis " + Loan.getFormattedDate(c.getCurrentLoan().getDueDate()) + " (noch " + c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() + " Tage)";
				else if (c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() == -1) s = "Ausgeliehen bis " + Loan.getFormattedDate(c.getCurrentLoan().getDueDate()) + " (" + -c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() + " Tag überfällig)";
				else s = "Ausgeliehen bis " + Loan.getFormattedDate(c.getCurrentLoan().getDueDate()) + " (" + -c.getCurrentLoan().getDaysOfExpectedLeftLoanDuration() + " Tage überfällig)";
			} else {
				s = "Verfügbar";
			}
			return s;
		} else {
			throw new RuntimeException("Undefined column name!");
		}
	}

	@Override
	public String getColumnName(int col) {
		return "" + columnnames[col];
	}
	
	public void updateCopies(List<Copy> newCopies){
		currentCopies = newCopies;
		fireTableDataChanged();
	}

}
