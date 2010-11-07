package view.customer;

import java.awt.EventQueue;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import application.LibraryApp;
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
