package view.customer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import view.BookMasterUiManager;
import view.book_master.SubFrame;
import domain.Customer;

public class CustomerEdit extends CustomerForm implements SubFrame<Customer> {
	private Customer customer;

	/**
	 * The view to edit a customer
	 * 
	 * @param uimanager
	 * @param customer
	 */
	public CustomerEdit(BookMasterUiManager uimanager, Customer customer) {
		super(uimanager);
		this.customer = customer;
		txtName.setText(customer.getName());
		txtSurname.setText(customer.getSurname());
		txtStreet.setText(customer.getStreet());
		txtCity.setText(customer.getCity());
		txtZip.setText("" + customer.getZip().intValue());
		formValidator.validateAll();
	}

	@Override
	protected String getWindowTitle() {
		return "Kunde Bearbeiten";
	}

	@Override
	protected String getSaveButtonTitle() {
		return "Änderungen Speichern";
	}

	@Override
	protected void saveCustomer(Customer c) {
		customer.updateValues(c);
		JOptionPane.showMessageDialog(frmCustomerForm, "Kunde wurde erfolgreich gespeichert.", "Hinweis", JOptionPane.INFORMATION_MESSAGE);
	}

	public Customer getCustomer() {
		return customer;
	}

	public JFrame getFrame() {
		return frmCustomerForm;
	}

	@Override
	public Customer getObject() {
		return getCustomer();
	}
}
