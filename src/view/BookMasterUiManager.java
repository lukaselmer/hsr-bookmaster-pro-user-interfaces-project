package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import view.book_master.SubFrame;
import view.customer.CustomerEdit;
import domain.Book;
import domain.Customer;
import domain.Library;

public class BookMasterUiManager {
	protected final Library library;
	protected List<SubFrame<Object>> subFrames = new ArrayList<SubFrame<Object>>();

	public BookMasterUiManager(Library library) {
		this.library = library;
	}

	public Library getLibrary() {
		return library;
	}

	public <T> void openWindow(T object) {
		boolean detailOpen = false;
		for (SubFrame<Object> bd : subFrames) {
			if (bd.getObject().equals(object)) {
				detailOpen = true;
				bd.toFront();
				break;
			}
		}
		if (!detailOpen) {
			final SubFrame<T> bd = createFrame(object);
			subFrames.add((SubFrame<Object>) bd);
			bd.getFrame().addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent arg0) {
					subFrames.remove(bd);
				}
			});
		}
	}

	protected <T> SubFrame<T> createFrame(T o) {
		if (o instanceof Book) {
			return (SubFrame<T>) new BookDetail(library, (Book) o);
		} else if (o instanceof Customer) {
			return (SubFrame<T>) new CustomerEdit(library, (Customer) o);
		} else {
			throw new NotImplementedException();
		}
	}

}
