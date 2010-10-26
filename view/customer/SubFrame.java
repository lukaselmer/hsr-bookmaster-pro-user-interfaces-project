package view.customer;

import java.awt.Window;

import javax.swing.JFrame;

public abstract interface SubFrame<T> {
	public abstract T getObject();

	public abstract void toFront();

	public abstract JFrame getFrame();
}
