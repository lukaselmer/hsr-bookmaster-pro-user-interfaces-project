package test;

import java.util.List;

import junit.framework.TestCase;
import domain.Customer;
import domain.Library;
import domain.Loan;
import domain.Book;
import domain.Shelf;

public class LibraryTest extends TestCase {

	Library library;

	@Override
	protected void setUp() throws Exception {

		library = new Library();

		// Books
		// Book b1 = library.createAndAddBook("Design Pattern");
		// Book b2 = library.createAndAddBook("Refactoring");
		// Book b3 = library.createAndAddBook("Clean Code");
		Book b1 = library.addBook(new Book("Design Pattern", "Terence Parr", "The Pragmatic Programmers", Shelf.A1));
		Book b2 = library.addBook(new Book("Refactoring", "Terence Parr", "The Pragmatic Programmers", Shelf.A1));
		Book b3 = library.addBook(new Book("Clean Code", "Terence Parr", "The Pragmatic Programmers", Shelf.A1));

		// Books
		library.createAndAddCopy(b1);
		library.createAndAddCopy(b1);
		library.createAndAddCopy(b1);

		library.createAndAddCopy(b2);
		library.createAndAddCopy(b2);

		library.createAndAddCopy(b3);

		// Customers
		library.addCustomer(new Customer("Keller", "Peter"));
		library.addCustomer(new Customer("Mueller", "Fritz"));
		library.addCustomer(new Customer("Meier", "Martin"));
	}

	public void testGetBooksPerTitle() {

		Book t = library.findByBookTitle("Design Pattern");
		assertEquals(3, library.getCopiesOfBook(t).size());

		Book t2 = library.findByBookTitle("Clean Code");
		assertEquals(1, library.getCopiesOfBook(t2).size());

		Book t3 = library.findByBookTitle("noTitle");
		assertEquals(0, library.getCopiesOfBook(t3).size());
	}

	public void testLoans() {
		Book t = library.findByBookTitle("Design Pattern");

		assertEquals(0, library.getLentCopiesOfBook(t).size());

		Customer c = library.getCustomers().get(0);

		Loan lo = library.createAndAddLoan(c, library.getCopiesOfBook(t).get(0));

		assertEquals(1, library.getLentCopiesOfBook(t).size());
		assertEquals(lo, library.getLentCopiesOfBook(t).get(0));

		Loan lo2 = library.createAndAddLoan(c, library.getCopiesOfBook(t).get(0));
		assertNull(lo2);

		List<Loan> lo3 = library.getCustomerLoans(c);
		assertEquals(1, lo3.size());

	}

	public void testAvailability() {
		assertEquals(library.getCopies().size(), library.getAvailableCopies().size());

		Book t = library.findByBookTitle("Refactoring");
		Customer c = library.getCustomers().get(1);
		library.createAndAddLoan(c, library.getCopiesOfBook(t).get(0));

		assertEquals(1, library.getLentOutBooks().size());

	}

}
