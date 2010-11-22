package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import view.book.BookEdit;
import view.book_detail.BookDetail;
import view.book_master.SubFrame;
import view.customer.CustomerEdit;
import domain.Book;
import domain.Customer;
import domain.Library;
import domain.Loan;

public class BookMasterUiManager {
	protected final Library library;
	protected List<SubFrame<Object>> subFrames = new ArrayList<SubFrame<Object>>();
	protected LoanDetail loanDetailFrame;

	public BookMasterUiManager(Library library) {
		this.library = library;
	}

	public Library getLibrary() {
		return library;
	}

	public <T> void openWindow(T object) {
		openWindow(object, 0);
	}

	public <T> void openWindow(T object, int i) {
		boolean detailOpen = false;
		for (SubFrame<Object> bd : subFrames) {
			if (bd.getObject().equals(object)) {
				detailOpen = true;
				bd.toFront();
				break;
			}
			if ((Object) bd instanceof LoanDetail) {
				detailOpen = true;
				bd.toFront();
				LoanDetail ld = (LoanDetail) (Object) bd;
				ld.updateLoan((Loan) object);
				break;
			}
		}
		if (!detailOpen) {
			final SubFrame<T> bd = createFrame(object, i);
			subFrames.add((SubFrame<Object>) bd);
			bd.getFrame().addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent arg0) {
					subFrames.remove(bd);
				}
			});
		}
	}

	protected <T> SubFrame<T> createFrame(T t) {
		return createFrame(t, 0);
	}

	protected <T> SubFrame<T> createFrame(T o, int i) {
		if (o instanceof Book) {
			if (i == 0)
				return (SubFrame<T>) new BookDetail(this, (Book) o);
			else
				return (SubFrame<T>) new BookEdit(getLibrary(), (Book) o);
		} else if (o instanceof Customer) {
			return (SubFrame<T>) new CustomerEdit(library, (Customer) o);
		} else {
			throw new NotImplementedException();
		}
	}

	public void openLoanWindow(Loan l) {
		if (loanDetailFrame == null || !loanDetailFrame.getFrame().isValid()) {
			loanDetailFrame = new LoanDetail(library, l);
			loanDetailFrame.getFrame().addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent arg0) {
					loanDetailFrame = null;
				}
			});
		} else {
			loanDetailFrame.toFront();
			loanDetailFrame.updateLoan(l);
		}
	}

}
