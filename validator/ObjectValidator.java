package validator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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

import domain.Customer;

public abstract class ObjectValidator<T> {

	protected JDialog popup;
	protected JLabel popupMessage;
	// private JTextField[] fields;
	protected Validator<T> validator;
	private JComponent btnSave;

	public ObjectValidator(JFrame frmNeuerKunde, JTextField[] fields, Validator<T> validator, JButton btnSave) {
		this.validator = validator;
		// this.fields = fields;
		this.btnSave = btnSave;

		popup = new JDialog(frmNeuerKunde);
		popupMessage = new JLabel("");
		popup.getContentPane().setLayout(new FlowLayout());
		popup.setUndecorated(true);
		popup.getContentPane().setBackground(new Color(243, 255, 159));
		JLabel image = new JLabel(ValidationResultViewFactory.getErrorIcon());
		popup.getContentPane().add(image);
		popup.getContentPane().add(popupMessage);
		popup.setFocusableWindowState(false);

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
					hidePopup();
				}
				@Override
				public void focusGained(FocusEvent e) {
					validateForm(f);
				}
			});
			if(ValidationComponentUtils.isMandatory(f) && f.getText().length() == 0){
				ValidationComponentUtils.setMandatoryBackground(f);
				f.setToolTipText("Muss ausgefüllt werden");
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
			if(ValidationComponentUtils.isMandatory(f) && f.getText().length() == 0){
				ValidationComponentUtils.setMandatoryBackground(f);
				f.setToolTipText("Muss ausgefüllt werden");
				hidePopup();
			}
			else if (fieldValidation.hasErrors()) {
				ValidationComponentUtils.setErrorBackground(f);
				String message = fieldValidation.getMessagesText().replaceFirst(f.getName() + " ", "");
				f.setToolTipText(message);
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
		popup.setVisible(false);
		popupMessage.setText("");
	}

	protected void showPopup(JComponent f, String message) {
		popupMessage.setText(message);
		popup.setSize(0, 0);
		popup.setLocationRelativeTo(f);
		Point point = popup.getLocation();
		Dimension cDim = f.getSize();
		popup.setLocation(point.x - (int) cDim.getWidth() / 2, (point.y + (int) cDim.getHeight() / 2) + 2);
		popup.pack();
		popup.setVisible(true);
	}

	/**
	 * Creates the object to be validated
	 * 
	 * @return The object to be validated
	 */
	public abstract T createObject();

}
