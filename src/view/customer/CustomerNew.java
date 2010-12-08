package view.customer;

import javax.swing.JOptionPane;

import view.BookMasterUiManager;
import domain.Customer;

public class CustomerNew extends CustomerForm {

	/**
	 * View to create a new customer
	 * @param uimanager
	 */
	public CustomerNew(BookMasterUiManager uimanager) {
		super(uimanager);
	}

	@Override
	protected String getWindowTitle() {
		return "Neuer Kunde";
	}

	@Override
	protected String getSaveButtonTitle() {
		return "Kunde Erfassen";
	}

	@Override
	protected void saveCustomer(Customer c) {
		library.addCustomer(c);
		JOptionPane.showMessageDialog(frmCustomerForm, "Kunde wurde erfolgreich erstellt und der Kundentabelle hinzugefügt.", "Hinweis",
				JOptionPane.INFORMATION_MESSAGE);
	}
}
