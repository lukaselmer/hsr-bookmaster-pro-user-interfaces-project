package view.customer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.Observable;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import validators.CustomerValidator;
import validators.FormValidator;
import application.LibraryApp;

import com.jgoodies.validation.view.ValidationComponentUtils;

import domain.Customer;
import domain.Library;

public abstract class CustomerForm {

	protected JFrame frmCustomerForm;
	protected Library library;
	protected JTextField txtSurname;
	protected JTextField txtName;
	protected JTextField txtStreet;
	protected JTextField txtZip;
	protected JTextField txtCity;
	protected JButton btnSave;
	protected FormValidator<Customer> formValidator;

	/**
	 * Launch the application.
	 */
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

	protected abstract String getWindowTitle();

	protected abstract String getSaveButtonTitle();

	protected abstract void saveCustomer(Customer c);

	/**
	 * Create the application.
	 * 
	 * @param bookMaster
	 * 
	 * @param library
	 * @wbp.parser.entryPoint
	 */
	public CustomerForm(Library library) {
		this.library = library;
		try {
			initialize();
		} catch (ParseException e) {
			e.printStackTrace();
			frmCustomerForm.dispose();
		}
		frmCustomerForm.setLocationByPlatform(true);
		frmCustomerForm.setVisible(true);
	}

	public boolean isValid() {
		return frmCustomerForm.isValid();
	}

	public void toFront() {
		frmCustomerForm.setState(JFrame.NORMAL);
		frmCustomerForm.toFront();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws ParseException
	 */
	private void initialize() throws ParseException {
		frmCustomerForm = new JFrame();
		frmCustomerForm.setTitle(getWindowTitle());
		frmCustomerForm.setBounds(100, 100, 450, 256);
		frmCustomerForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmCustomerForm.setResizable(false);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, getWindowTitle(), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmCustomerForm.getContentPane().add(panel, BorderLayout.CENTER);

		JLabel lblName = new JLabel("Vorname:");
		lblName.setDisplayedMnemonic('v');

		JLabel lblSurname = new JLabel("Nachname:");
		lblSurname.setDisplayedMnemonic('n');

		JLabel lblStreet = new JLabel("Strasse:");
		lblStreet.setDisplayedMnemonic('s');

		JLabel lblZip = new JLabel("PLZ:");
		lblZip.setDisplayedMnemonic('p');

		JLabel lblCity = new JLabel("Ort:");
		lblCity.setDisplayedMnemonic('o');

		txtName = new JTextField();
		lblName.setLabelFor(txtName);
		txtName.setColumns(10);
		txtName.setName("Kunde.Vorname");
		ValidationComponentUtils.setMandatory(txtName, true);

		txtSurname = new JTextField();
		lblSurname.setLabelFor(txtSurname);
		txtSurname.setColumns(10);
		txtSurname.setName("Kunde.Nachname");
		ValidationComponentUtils.setMandatory(txtSurname, true);

		txtStreet = new JTextField();
		lblStreet.setLabelFor(txtStreet);
		txtStreet.setColumns(10);
		txtStreet.setName("Kunde.Strasse");
		ValidationComponentUtils.setMandatory(txtStreet, true);

		// MaskFormatter formatter = new MaskFormatter("####");
		// txtZip = new JFormattedTextField(formatter);
		txtZip = new JTextField();
		lblZip.setLabelFor(txtZip);
		txtZip.setColumns(10);
		txtZip.setName("Kunde.PLZ");
		ValidationComponentUtils.setMandatory(txtZip, true);

		txtCity = new JTextField();
		lblCity.setLabelFor(txtCity);
		txtCity.setColumns(10);
		txtCity.setName("Kunde.Stadt");
		ValidationComponentUtils.setMandatory(txtCity, true);

		btnSave = new JButton(getSaveButtonTitle());
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Customer c = formValidator.validateForm(null);
				if (c == null) {
					throw new RuntimeException("Bad state");
				}
				saveCustomer(c);
				frmCustomerForm.dispose();
			}
		});
		btnSave.setEnabled(false);

		JTextField[] fields = { txtName, txtCity, txtStreet, txtSurname, txtZip };
		formValidator = new FormValidator<Customer>(frmCustomerForm, fields, new CustomerValidator(), btnSave) {
			@Override
			public Customer createObject() {
				Integer zip = null;
				try {
					zip = Integer.parseInt(txtZip.getText());
				} catch (NumberFormatException ex) {
				}
				return new Customer(txtName.getText(), txtSurname.getText(), txtStreet.getText(), txtCity.getText(), zip);
			}
		};

		JButton btnCancel = new JButton("Abbrechen");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmCustomerForm.dispose();
			}
		});

		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel.createParallelGroup(Alignment.LEADING, false)
										.addComponent(lblSurname, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE).addComponent(lblStreet).addComponent(lblZip).addComponent(lblCity)
										.addComponent(lblName))
						.addGap(18)
						.addGroup(
								gl_panel.createParallelGroup(Alignment.LEADING)
										.addGroup(
												Alignment.TRAILING,
												gl_panel.createSequentialGroup()
														.addComponent(btnSave, GroupLayout.DEFAULT_SIZE, 164, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 170,
																GroupLayout.PREFERRED_SIZE))
										.addComponent(txtStreet, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
										.addComponent(txtZip, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
										.addComponent(txtName, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
										.addComponent(txtSurname, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
										.addComponent(txtCity, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblName)
										.addComponent(txtName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblSurname)
										.addComponent(txtSurname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblStreet)
										.addComponent(txtStreet, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblZip)
										.addComponent(txtZip, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblCity)
										.addComponent(txtCity, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(btnSave).addComponent(btnCancel))
						.addContainerGap(58, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
	}
}
