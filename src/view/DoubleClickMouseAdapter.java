package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class DoubleClickMouseAdapter extends MouseAdapter implements MouseListener {

	public abstract void leftDoubleClick(MouseEvent mouseEvent);

	@Override
	public void mouseClicked(MouseEvent mouseEvent) {
		if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseEvent.BUTTON1) {
			leftDoubleClick(mouseEvent);
		}
	}
}
