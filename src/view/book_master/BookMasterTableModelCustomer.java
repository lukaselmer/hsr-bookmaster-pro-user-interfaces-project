package view.book_master;

import java.util.List;

import view.BookMasterTableModel;
import domain.Customer;
import domain.Library;

public class BookMasterTableModelCustomer extends BookMasterTableModel<Customer> {

	private static final long serialVersionUID = 8466707343843649023L;

	public enum ColumnName implements AbstractColumnName {
		SURNAME("Nachname"), NAME("Vorname"), STREET("Strasse"), CITY("Stadt"), ZIP("PLZ");
		private String name;

		private ColumnName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public BookMasterTableModelCustomer(Library library) {
		super(library);
	}

	@Override
	public Object getValueAt(int row, int col) {
		Customer c = currentObjects.get(row);
		if (col == -1) {
			return c;
		} else if (getColumnName(col).equals(ColumnName.SURNAME.toString())) {
			return c.getSurname();
		} else if (getColumnName(col).equals(ColumnName.NAME.toString())) {
			return c.getName();
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
	protected List<Customer> getInitialObjects() {
		return library.getCustomers();
	}

	@Override
	protected AbstractColumnName[] getColumnNames() {
		return ColumnName.values();
	}

	@Override
	public int getDefaultSortedColumn() {
		return ColumnName.SURNAME.ordinal();
	}
}
