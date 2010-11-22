package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import view.book.BookEdit;
import view.book.BookForm;
import view.book.BookNew;
import view.book_detail.BookDetail;
import view.book_master.SubFrame;
import view.customer.CustomerEdit;
import view.customer.CustomerNew;
import domain.Book;
import domain.Customer;
import domain.Library;
import domain.Loan;

public class BookMasterUiManager {
	protected final Library library;
	@SuppressWarnings("rawtypes")
	protected List<SubFrame> subFrames = new ArrayList<SubFrame>();
	protected LoanDetail loanDetailFrame;
	private CustomerNew customerNewFrame;
	private BookNew bookNewFrame;

	public BookMasterUiManager(Library library) {
		this.library = library;
	}

	public Library getLibrary() {
		return library;
	}

	// protected <T> SubFrame<T> createFrame(T t) {
	// return createFrame(t, 0);
	// }

	// protected <T> SubFrame<T> createFrame(T o, int i) {
	// if (o instanceof Book) {
	// if (i == 0)
	// return (SubFrame<T>) new BookDetail(this, (Book) o);
	// else if (i == 1)
	// return (SubFrame<T>) new BookEdit(getLibrary(), (Book) o);
	// else
	// throw new NotImplementedException();
	// } else if (o instanceof Customer) {
	// return (SubFrame<T>) new CustomerEdit(this, (Customer) o);
	// } else {
	// throw new NotImplementedException();
	// }
	// }

	public void openBookDetailWindow(Book o) {
		for (SubFrame<Book> f : subFrames) {
			if (f instanceof BookDetail && f.getObject().equals(o)) {
				f.toFront();
				return;
			}
		}
		final SubFrame<Book> f = new BookDetail(this, o);
		subFrames.add(f);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				subFrames.remove(f);
			}
		});
	}

	public void openBookEditWindow(Book o) {
		for (SubFrame<Book> f : subFrames) {
			if (f instanceof BookEdit && f.getObject().equals(o)) {
				f.toFront();
				return;
			}
		}
		final SubFrame<Book> f = new BookEdit(this, o);
		subFrames.add(f);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				subFrames.remove(f);
			}
		});
	}

	public void openCustomerEditWindow(Customer o) {
		for (SubFrame<Customer> f : subFrames) {
			if (f instanceof CustomerEdit && f.getObject().equals(o)) {
				f.toFront();
				return;
			}
		}
		final SubFrame<Customer> f = new CustomerEdit(this, o);
		subFrames.add(f);
		f.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				subFrames.remove(f);
			}
		});
	}

	public void openLoanWindow(Customer c) {
		if (loanDetailFrame == null) {
			loanDetailFrame = new LoanDetail(this, c);
			loanDetailFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent arg0) {
					loanDetailFrame = null;
				}
			});
		} else {
			loanDetailFrame.toFront();
			loanDetailFrame.updateCustomer(c);
		}
	}

	public void openCustomerNewWindow() {
		if (customerNewFrame == null) {
			customerNewFrame = new CustomerNew(this);
			customerNewFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent arg0) {
					customerNewFrame = null;
				}
			});
		} else {
			customerNewFrame.toFront();
		}
	}

	public void openBookNewFrame() {
		if (bookNewFrame == null) {
			bookNewFrame = new BookNew(this);
			bookNewFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent arg0) {
					Book b = bookNewFrame.getSavedObject();
					bookNewFrame = null;
					if (b != null)
						openBookDetailWindow(b);
					bookNewFrame = null;
				}
			});
		} else {
			bookNewFrame.toFront();
		}
	}

	// boolean detailOpen = false;
	// for (SubFrame<Object> bd : subFrames) {
	// if (bd.getObject().equals(object)) {
	// detailOpen = true;
	// bd.toFront();
	// break;
	// }
	// if (bd.getClass().equals(LoanDetail.class)) {
	// detailOpen = true;
	// bd.toFront();
	// LoanDetail ld = (LoanDetail) (Object) bd;
	// ld.updateLoan((Loan) object);
	// break;
	// }
	// }
	// if (!detailOpen) {
	// final SubFrame<T> bd = createFrame(object, i);
	// subFrames.add((SubFrame<Object>) bd);
	// bd.getFrame().addWindowListener(new WindowAdapter() {
	// @Override
	// public void windowClosed(WindowEvent arg0) {
	// subFrames.remove(bd);
	// }
	// });
	// }

}
