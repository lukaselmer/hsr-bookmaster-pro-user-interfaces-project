package domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Observable;

public class Loan extends Observable {

	private Copy copy;
	private Customer customer;
	private GregorianCalendar pickupDate, returnDate;
	private final static int DAYS_TO_RETURN_BOOK = 30;

	public Loan(Customer customer, Copy copy) {
		this.copy = copy;
		this.customer = customer;
		pickupDate = new GregorianCalendar();
		copy.setCurrentLoan(this);
	}

	public boolean isLent() {
		return returnDate == null;
	}

	public boolean returnCopy() {
		try {
			returnCopy(new GregorianCalendar());
			setChanged();
			notifyObservers();
		} catch (IllegalLoanOperationException e) {
			return false;
		}
		return true;
	}

	public void returnCopy(GregorianCalendar returnDate) throws IllegalLoanOperationException {
		if (returnDate.before(pickupDate)) {
			throw new IllegalLoanOperationException("Return Date is before pickupDate");
		}
		this.returnDate = returnDate;
		copy.setCurrentLoan(null);
		setChanged();
		notifyObservers();
	}

	public void setPickupDate(GregorianCalendar pickupDate) throws IllegalLoanOperationException {
		if (!isLent()) {
			throw new IllegalLoanOperationException("Loan is already retuned");
		}
		this.pickupDate = pickupDate;
		setChanged();
		notifyObservers();
	}

	public GregorianCalendar getPickupDate() {
		return pickupDate;
	}

	public GregorianCalendar getReturnDate() {
		return returnDate;
	}

	public Copy getCopy() {
		return copy;
	}

	public Customer getCustomer() {
		return customer;
	}

	@Override
	public String toString() {
		return "Loan of: " + copy.getBook().getName() + "\tFrom: " + customer.getName() + " " + customer.getSurname() + "\tPick up: "
				+ getFormattedDate(pickupDate) + "\tReturn: " + getFormattedDate(returnDate) + "\tDays: " + getDaysOfLoanDuration();
	}

	public static String getFormattedDate(GregorianCalendar date) {
		if (date != null) {
			DateFormat f = SimpleDateFormat.getDateInstance();
			return f.format(date.getTime());
		}
		return "00.00.00";
	}

	public int getDaysOfLoanDuration() {
		GregorianCalendar d = (returnDate == null) ? new GregorianCalendar() : returnDate;
		return (int) (d.getTimeInMillis() - pickupDate.getTimeInMillis()) / 1000 / 60 / 60 / 24;
	}

	public int getDaysOfExpectedLeftLoanDuration() {
		int days = DAYS_TO_RETURN_BOOK - getDaysOfLoanDuration();
		if (days < 0)
			return -1;
		return days;
	}

	public GregorianCalendar getDueDate() {
		GregorianCalendar dueDate = (GregorianCalendar) pickupDate.clone();
		dueDate.add(GregorianCalendar.DAY_OF_YEAR, DAYS_TO_RETURN_BOOK);
		return dueDate;
	}

	public int getDaysOverdue() {
		if (!isOverdue())
			return 0;
		GregorianCalendar dueDate = getDueDate();
		return (int) (new GregorianCalendar().getTimeInMillis() - dueDate.getTimeInMillis()) / 1000 / 60 / 60 / 24;
	}

	public boolean isOverdue() {
		if (!isLent())
			return false;

		GregorianCalendar dueDate = (GregorianCalendar) pickupDate.clone();
		dueDate.add(GregorianCalendar.DAY_OF_YEAR, DAYS_TO_RETURN_BOOK);
		dueDate.add(GregorianCalendar.HOUR_OF_DAY, 23);
		dueDate.add(GregorianCalendar.MINUTE, 59);
		dueDate.add(GregorianCalendar.SECOND, 59);

		return (new GregorianCalendar().after(dueDate));
	}

	public Book getBook() {
		return getCopy().getBook();
	}
}
