package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Library extends Observable {

	private List<Copy> copies;
	private List<Customer> customers;
	private List<Loan> loans;
	private List<Book> books;

	public Library() {
		copies = new ArrayList<Copy>();
		customers = new ArrayList<Customer>();
		loans = new ArrayList<Loan>();
		books = new ArrayList<Book>();
	}

	public Loan createAndAddLoan(Customer customer, Copy copy) {
		if (!isCopyLent(copy)) {
			Loan l = new Loan(customer, copy);
			loans.add(l);
			setChanged();
			notifyObservers();
			return l;
		} else {
			return null;
		}
	}

	public Customer createAndAddCustomer(String name, String surname) {
		Customer c = new Customer(name, surname);
		customers.add(c);
		setChanged();
		notifyObservers();
		return c;
	}

	public Book createAndAddBook(String name) {
		Book b = new Book(name);
		books.add(b);
		setChanged();
		notifyObservers();
		return b;
	}

	public Copy createAndAddCopy(Book title) {
		Copy c = new Copy(title);
		copies.add(c);
		setChanged();
		notifyObservers();
		return c;
	}

	public Book findByBookTitle(String title) {
		for (Book b : books) {
			if (b.getName().equals(title)) {
				return b;
			}
		}
		return null;
	}

	public List<Book> searchBooksByTitle(String title) {
		List<Book> foundBooks = new ArrayList();
		for (Book b : books) {
			if (b.getName().toLowerCase().contains(title.toLowerCase())) {
				foundBooks.add(b);
			}
		}
		return foundBooks;
	}

	public boolean isCopyLent(Copy copy) {
		for (Loan l : loans) {
			if (l.getCopy().equals(copy) && l.isLent()) {
				return true;
			}
		}
		return false;
	}

	public List<Copy> getCopiesOfBook(Book book) {
		List<Copy> res = new ArrayList<Copy>();
		for (Copy c : copies) {
			if (c.getBook().equals(book)) {
				res.add(c);
			}
		}
		return res;
	}

	public List<Loan> getLentCopiesOfBook(Book book) {
		List<Loan> lentCopies = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.getCopy().getBook().equals(book) && l.isLent()) {
				lentCopies.add(l);
			}
		}
		return lentCopies;
	}

	public List<Loan> getAvailibleCopiesOfBook(Book book) {
		List<Loan> availibleCopies = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.getCopy().getBook().equals(book) && !l.isLent()) {
				availibleCopies.add(l);
			}
		}
		return availibleCopies;
	}

	public Copy getNextAvailibleCopyOfBook(Book book) {
		Copy nextAvailibleCopy = null;
		for (Copy c : copies) {
			if (c.getBook().equals(book)) {
				if (!c.isLent()) {
					return c;
				}
				if (nextAvailibleCopy == null
						|| c.getDaysOfExpectedLeftLoanDuration() < nextAvailibleCopy.getDaysOfExpectedLeftLoanDuration()) {
					nextAvailibleCopy = c;
				}
			}
		}
		return nextAvailibleCopy;
	}

	public List<Loan> getCustomerLoans(Customer customer) {
		List<Loan> lentCopies = new ArrayList<Loan>();
		for (Loan l : loans) {
			if (l.getCustomer().equals(customer)) {
				lentCopies.add(l);
			}
		}
		return lentCopies;
	}

	public List<Loan> getOverdueLoans() {
		List<Loan> overdueLoans = new ArrayList<Loan>();
		for (Loan l : getLoans()) {
			if (l.isOverdue())
				overdueLoans.add(l);
		}
		return overdueLoans;
	}

	public List<Copy> getAvailableCopies() {
		return getCopies(false);
	}

	public List<Copy> getLentOutBooks() {
		return getCopies(true);
	}

	private List<Copy> getCopies(boolean isLent) {
		List<Copy> retCopies = new ArrayList<Copy>();
		for (Copy c : copies) {
			if (isLent == isCopyLent(c)) {
				retCopies.add(c);
			}
		}
		return retCopies;
	}

	public List<Copy> getCopies() {
		return copies;
	}

	public List<Loan> getLoans() {
		return loans;
	}

	public List<Book> getBooks() {
		return books;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

}
