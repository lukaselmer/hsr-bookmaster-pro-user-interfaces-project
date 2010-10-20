package view.customer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;

import validators.CustomerValidator;
import validators.FormValidator;
import application.LibraryApp;

import com.jgoodies.validation.view.ValidationComponentUtils;

import domain.Customer;
import domain.Library;

public class CustomerNew extends CustomerForm {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
					new CustomerNew(LibraryApp.inst());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public CustomerNew(Library library) {
		super(library);
	}

	@Override
	protected String getWindowTitle() {
		return "Neuer Kunde";
	}

	@Override
	protected String getSaveButtonTitle() {
		return "Kunde Erstellen";
	}

	@Override
	protected void saveCustomer(Customer c) {
		library.addCustomer(c);
		JOptionPane.showMessageDialog(frmCustomerForm, "Kunde wurde erfolgreich erstellt und der Kundentabelle hinzugefügt.",
				"Hinweis", JOptionPane.INFORMATION_MESSAGE);
	}
}
