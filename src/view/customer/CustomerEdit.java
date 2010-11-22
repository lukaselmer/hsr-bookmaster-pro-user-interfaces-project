package view.customer;

import java.awt.EventQueue;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import view.BookMasterUiManager;
import view.book_master.SubFrame;
import application.LibraryApp;
import domain.Customer;
import domain.Library;

public class CustomerEdit extends CustomerForm implements SubFrame<Customer> {
	private Customer customer;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
					Library l = LibraryApp.inst();
					new CustomerEdit(new BookMasterUiManager(l), l.getCustomers().get(new Random().nextInt(l.getCustomers().size())));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

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
