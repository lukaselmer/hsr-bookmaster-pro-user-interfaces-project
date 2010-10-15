package domain;

import java.util.Observable;

public class Copy extends Observable {

	public enum Condition {
		NEW, GOOD, DAMAGED, WASTE, LOST
	}

	public static long nextInventoryNumber = 1;

	private final long inventoryNumber;
	private final Book book;
	private Condition condition;
	private Loan currentLoan;

	public Copy(Book book) {
		this.book = book;
		inventoryNumber = nextInventoryNumber++;
		condition = Condition.NEW;
	}

	public Book getBook() {
		return book;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
		setChanged();
		notifyObservers();
	}

	public long getInventoryNumber() {
		return inventoryNumber;
	}

	public Loan getCurrentLoan() {
		return currentLoan;
	}

	public void setCurrentLoan(Loan currentLoan) {
		this.currentLoan = currentLoan;
		setChanged();
		notifyObservers();
	}

	@Override
	public String toString() {
		return getBook() + (currentLoan == null ? "" : " - " + currentLoan);
	}

}
