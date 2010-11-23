package view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import view.book.BookEdit;
import view.book.BookNew;
import view.book_detail.BookDetail;
import view.book_master.SubFrame;
import view.customer.CustomerEdit;
import view.customer.CustomerNew;
import view.loan_detail.LoanDetail;
import view.loans_report.LoansReport;
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

	public void openLoansReportFrame(List<Loan> loans) {
		new LoansReport(this, loans);
	}

}
