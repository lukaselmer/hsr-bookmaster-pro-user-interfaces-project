package view.loan;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import domain.Book;
import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Loan;

public class LoanDetailTableModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 5453188403390577160L;
	
	public enum ColumnName{
		STATUS("Status"),  LOAN_UNTIL("Ausgeliehen Bis"), INVENTORY_NUMBER("Exemplar-ID"), TITLE("Titel"), AUTHOR("Author");
		private String name;
		
		private ColumnName(String name){
			this.name = name;
		}
		
		@Override
		public String toString(){
			return name;
		}
	}
	
	ColumnName[] columnNames = ColumnName.values();
	Library library;
	List<Loan> currentLoans;
	
	
	
	public LoanDetailTableModel(Library library, Customer customer) {
		this.library = library;
		currentLoans = library.getCustomerLoans(customer);
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
		Book b = l.getBook();
		if (col == -1){
			return l;
		} else if (getColumnName(col).equals(ColumnName.STATUS.toString())){
			return l.isOverdue() ? "Fällig" : "Ok";
		} else if (getColumnName(col).equals(ColumnName.INVENTORY_NUMBER.toString())){
			return c.getInventoryNumber();
		} else if (getColumnName(col).equals(ColumnName.LOAN_UNTIL.toString())){
			return Loan.getFormattedDate(l.getDueDate());
		} else if (getColumnName(col).equals(ColumnName.TITLE.toString())){
			return b.getName();
		} else if (getColumnName(col).equals(ColumnName.AUTHOR.toString())){
			return b.getAuthor();
		} else {
			throw new RuntimeException("Undefined column name!");
		}
	}
	
	@Override
	public String getColumnName(int col){
		return "" + columnNames[col];	
	}
	
	public void updateLoans(List<Loan> newLoans) {
		currentLoans = newLoans;
		fireTableDataChanged();
	}
	
}