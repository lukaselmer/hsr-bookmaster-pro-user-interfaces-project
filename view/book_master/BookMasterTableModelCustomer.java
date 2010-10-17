package view.book_master;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import domain.Customer;
import domain.Library;

public class BookMasterTableModelCustomer extends AbstractTableModel {

	public enum ColumnName {
		NAME("Vorname"), SURNAME("Nachname"), STREET("Strasse"), CITY("Stadt"), ZIP("PLZ");
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
	List<Customer> currentCustomers;

	public BookMasterTableModelCustomer(Library library) {
		this.library = library;
		currentCustomers = library.getCustomers();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return currentCustomers.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Customer c = currentCustomers.get(row);
		if (col == -1) {
			return c;
		} else if (getColumnName(col).equals(ColumnName.NAME.toString())) {
			return c.getName();
		} else if (getColumnName(col).equals(ColumnName.SURNAME.toString())) {
			return c.getSurname();
		} else if (getColumnName(col).equals(ColumnName.STREET.toString())) {
			return c.getStreet();
		} else if (getColumnName(col).equals(ColumnName.CITY.toString())) {
			return c.getCity();
		} else if (getColumnName(col).equals(ColumnName.ZIP.toString())) {
			return c.getZip();
		} else {
			throw new RuntimeException("Undefined column name!");
		}
	}

	@Override
	public String getColumnName(int column) {
		return "" + columnNames[column];
	}

	public void updateCustomers(List<Customer> newCustomers) {
		currentCustomers = newCustomers;
		fireTableDataChanged();
	}
}
