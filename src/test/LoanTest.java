package test;

import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;
import domain.Book;
import domain.Copy;
import domain.Customer;
import domain.IllegalLoanOperationException;
import domain.Loan;
import domain.Shelf;

public class LoanTest extends TestCase {

	public void testLoanCreation() {
		Loan l = createSampleLoan();
		assertTrue(new GregorianCalendar().equals(l.getPickupDate()));
		assertEquals("Keller", l.getCustomer().getName());
		assertEquals("Design Pattern", l.getCopy().getBook().getName());
		assertEquals(l.getCopy().getCurrentLoan(), l);
	}

	private Loan createSampleLoan() {
		Customer customer = new Customer("Keller", "Peter");
		Book title = new Book("Design Pattern", "Terence Parr", "The Pragmatic Programmers", Shelf.A1);
		Copy copy = new Copy(title);
		Loan loan = new Loan(customer, copy);
		return loan;
	}

	public void testReturn() {
		Loan l = createSampleLoan();
		assertTrue(l.isLent());
		assertEquals(l.getCopy().getCurrentLoan(), l);
		l.returnCopy();
		assertFalse(l.isLent());
		assertNull(l.getCopy().getCurrentLoan());
	}

	public void testDurationCalculation() throws IllegalLoanOperationException {
		Loan l = createSampleLoan();
		assertEquals(0, l.getDaysOfLoanDuration());

		GregorianCalendar returnDate = (GregorianCalendar) l.getPickupDate().clone();
		returnDate.add(Calendar.DAY_OF_YEAR, 12);

		l.returnCopy(returnDate);

		assertEquals(12, l.getDaysOfLoanDuration());
		assertEquals(l.getReturnDate(), returnDate);

	}

	public void testDateConsistency() throws IllegalLoanOperationException {
		Loan l = createSampleLoan();
		l.setPickupDate(new GregorianCalendar(2009, 01, 01));
		assertTrue(l.isLent());
		try {
			l.returnCopy(new GregorianCalendar(2008, 12, 31));
			fail("Book cannot retuned before the pickup date");
		} catch (IllegalLoanOperationException e) {

		}
		assertTrue(l.isLent());
		l.returnCopy(new GregorianCalendar(2009, 12, 31));
		assertFalse(l.isLent());
		try {
			l.setPickupDate(new GregorianCalendar(2008, 10, 31));
			fail("pickup date cannot be set after the book was returned");
		} catch (IllegalLoanOperationException e) {
		}
	}
}
