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

public class CustomerNew {

	private JFrame frmNeuerKunde;
	private Library library;
	private JTextField txtSurname;
	private JTextField txtName;
	private JTextField txtStreet;
	private JTextField txtZip;
	private JTextField txtCity;
	private JButton btnSave;
	private FormValidator<Customer> formValidator;

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

	/**
	 * Create the application.
	 * 
	 * @param bookMaster
	 * 
	 * @param library
	 */
	public CustomerNew(Library library) {
		this.library = library;
		try {
			initialize();
		} catch (ParseException e) {
			e.printStackTrace();
			frmNeuerKunde.dispose();
		}
		frmNeuerKunde.setLocationByPlatform(true);
		frmNeuerKunde.setVisible(true);
	}

	public boolean isValid() {
		return frmNeuerKunde.isValid();
	}

	public void toFront() {
		frmNeuerKunde.setState(JFrame.NORMAL);
		frmNeuerKunde.toFront();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws ParseException
	 */
	private void initialize() throws ParseException {
		frmNeuerKunde = new JFrame();
		frmNeuerKunde.setTitle("Neuer Kunde");
		frmNeuerKunde.setBounds(100, 100, 450, 256);
		frmNeuerKunde.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmNeuerKunde.setResizable(false);

		JPanel panel_1 = new JPanel();
		frmNeuerKunde.getContentPane().add(panel_1, BorderLayout.NORTH);

		JLabel lblNeuerKunde = new JLabel("Neuer Kunde");
		panel_1.add(lblNeuerKunde);
		lblNeuerKunde.setFont(new Font("Tahoma", Font.BOLD, 14));

		JPanel panel = new JPanel();
		frmNeuerKunde.getContentPane().add(panel, BorderLayout.CENTER);

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

		btnSave = new JButton("Kunde Erstellen");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Customer c = formValidator.validateForm(null);
				if (c == null) {
					throw new RuntimeException("Bad state");
				}
				library.addCustomer(c);
				JOptionPane.showMessageDialog(frmNeuerKunde, "Kunde wurde erfolgreich erstellt und der Kundentabelle hinzugefügt.",
						"Hinweis", JOptionPane.INFORMATION_MESSAGE);
				frmNeuerKunde.dispose();
			}
		});
		btnSave.setEnabled(false);

		JTextField[] fields = { txtName, txtCity, txtStreet, txtSurname, txtZip };
		formValidator = new FormValidator<Customer>(frmNeuerKunde, fields, new CustomerValidator(), btnSave) {
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
				frmNeuerKunde.dispose();
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
