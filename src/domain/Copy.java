package domain;

import java.util.Observable;

public class Copy extends Observable {

	public enum Condition {
		NEW("Neu"), GOOD("Gut"), DAMAGED("Beschädigt"), WASTE("Schlecht"); // LOST("Verloren")
		private String name;

		private Condition(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public static long nextInventoryNumber = 1;

	private final long inventoryNumber;
	private final Book book;
	private Condition condition = Condition.NEW;
	private Loan currentLoan;
	private boolean lost;

	public Copy(Book book) {
		this.book = book;
		inventoryNumber = nextInventoryNumber++;
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

	public boolean isLent() {
		return currentLoan != null;
	}

	public int getDaysOfExpectedLeftLoanDuration() {
		if (!isLent())
			return -1;
		else
			return getCurrentLoan().getDaysOfExpectedLeftLoanDuration();
	}

	@Override
	public String toString() {
		return getBook() + (currentLoan == null ? "" : " - " + currentLoan);
	}

	public void setLost() {
		this.lost = true;
		setChanged();
		notifyObservers();
	}

	public boolean isLost() {
		return lost;
	}

}
