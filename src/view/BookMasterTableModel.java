package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import domain.Library;

public abstract class BookMasterTableModel<T> extends AbstractTableModel {

	private static final long serialVersionUID = -6388775570907555337L;

	public interface AbstractColumnName {
		public String toString();
	}

	protected final Library library;
	protected List<T> currentObjects;
	protected final AbstractColumnName[] columnNames;

	public BookMasterTableModel(Library library) {
		this.library = library;
		currentObjects = new ArrayList<T>(getInitialObjects());
		columnNames = getColumnNames();
	}

	protected abstract List<T> getInitialObjects();

	protected abstract AbstractColumnName[] getColumnNames();

	public void updateObjects(List<T> newObjects) {
		if (newObjects.equals(currentObjects)) {
			return; // objects are the same -> no change and return
		}
		currentObjects = newObjects;
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return currentObjects.size();
	}

	@Override
	public String getColumnName(int column) {
		return "" + columnNames[column];
	}

}
