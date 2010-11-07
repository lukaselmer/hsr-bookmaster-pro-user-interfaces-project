package validators;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.view.ValidationComponentUtils;
import com.jgoodies.validation.view.ValidationResultViewFactory;

public abstract class FormValidator<T> {

	protected JDialog popup;
	protected JLabel popupMessage;
	private JTextField[] fields;
	protected Validator<T> validator;
	private JComponent btnSave;
	//private JFrame frame;
	private JComponent currentComponent;

	public FormValidator(JFrame frame, JTextField[] fields, Validator<T> validator, JButton btnSave) {
		this.validator = validator;
		this.fields = fields;
		this.btnSave = btnSave;

		//this.frame = frame;
		popup = new JDialog(frame);
		popupMessage = new JLabel("");
		popup.getContentPane().setLayout(new FlowLayout());
		popup.setUndecorated(true);
		popup.getContentPane().setBackground(new Color(243, 255, 159));
		JLabel image = new JLabel(ValidationResultViewFactory.getErrorIcon());
		popup.getContentPane().add(image);
		popup.getContentPane().add(popupMessage);
		popup.setFocusableWindowState(false);

		frame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				updatePopupPosition();
			}

			@Override
			public void componentMoved(ComponentEvent e) {
				updatePopupPosition();
			}
		});

		for (final JTextField f : fields) {
			f.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					validateForm(f);
				}
			});
			f.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					validateForm(f);
					hidePopup();
				}

				@Override
				public void focusGained(FocusEvent e) {
					validateForm(f);
				}
			});
			if (ValidationComponentUtils.isMandatory(f) && f.getText().length() == 0) {
				ValidationComponentUtils.setMandatoryBackground(f);
				f.setToolTipText("Muss ausgefüllt werden");
			}
		}
	}

	protected void updatePopupPosition() {
		synchronized (popup) {
			if (currentComponent != null) {
				Point l = currentComponent.getLocationOnScreen();
				popup.setLocation((int) l.getX(), (int) l.getY() + currentComponent.getHeight() + 2);
			}
		}
	}

	public T validateForm(JTextField f) {
		T c = createObject();
		ValidationResult validation = validator.validate(c);
		btnSave.setToolTipText(validation.hasErrors() ? "<html>" + validation.getMessagesText().replaceAll("\n", "<br/>") + "</html>"
				: null);
		btnSave.setEnabled(!validation.hasErrors());
		if (f != null) {
			ValidationResult fieldValidation = validation.subResult(f.getName());
			if (ValidationComponentUtils.isMandatory(f) && f.getText().length() == 0) {
				ValidationComponentUtils.setMandatoryBackground(f);
				f.setToolTipText("Muss ausgefüllt werden");
				hidePopup();
			} else if (fieldValidation.hasErrors()) {
				ValidationComponentUtils.setErrorBackground(f);
				String message = fieldValidation.getMessagesText().replaceFirst(f.getName() + " ", "");
				f.setToolTipText(message);
				if (f.hasFocus())
					showPopup(f, message);
			} else {
				f.setBackground(Color.WHITE);
				f.setToolTipText(null);
				hidePopup();
			}
		}
		return validation.hasErrors() ? null : c;
	}

	protected void hidePopup() {
		synchronized (popup) {
			currentComponent = null;
			popup.setVisible(false);
			popupMessage.setText("");
		}
	}

	protected void showPopup(JComponent f, String message) {
		synchronized (popup) {
			this.currentComponent = f;
			popupMessage.setText(message);
			popup.pack();
			updatePopupPosition();
			popup.setVisible(true);
		}
	}

	/**
	 * Creates the object to be validated
	 * 
	 * @return The object to be validated
	 */
	public abstract T createObject();

	public void validateAll() {
		for (final JTextField f : fields) {
			validateForm(f);
		}
	}

}
