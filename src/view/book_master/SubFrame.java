package view.book_master;

import java.awt.event.WindowAdapter;

public abstract interface SubFrame<T> {
	public abstract T getObject();

	public abstract void toFront();

	public abstract boolean isVisible();

	public abstract void addWindowListener(WindowAdapter windowAdapter);
}
