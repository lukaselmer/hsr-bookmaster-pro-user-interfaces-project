package view;

import java.awt.EventQueue;
import java.awt.Frame;

import javax.swing.JFrame;

import domain.Customer;
import domain.Library;

import application.LibraryApp;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.MaskFormatter;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.text.ParseException;

public class NewCustomerFrame {

	private JFrame frmNeuerKunde;
	private Library library;
	private Customer customer;
	private JTextField txtSurname;
	private JTextField txtName;
	private JTextField txtStreet;
	private JFormattedTextField txtZip;
	private JTextField txtPlace;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
					Library l = LibraryApp.inst();
					/*
					 * , l.getCustomers().get(new
					 * Random().nextInt(l.getCustomers().size()))
					 */
					NewCustomerFrame window = new NewCustomerFrame(l);
					// window.frmNeuerKunde.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @param library
	 */
	public NewCustomerFrame(Library library) {
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

		JLabel lblPlace = new JLabel("Ort:");
		lblPlace.setDisplayedMnemonic('o');

		txtName = new JTextField();
		lblName.setLabelFor(txtName);
		txtName.setColumns(10);

		txtSurname = new JTextField();
		lblSurname.setLabelFor(txtSurname);
		txtSurname.setColumns(10);

		txtStreet = new JTextField();
		lblStreet.setLabelFor(txtStreet);
		txtStreet.setColumns(10);

		MaskFormatter formatter = new MaskFormatter("####");
		txtZip = new JFormattedTextField(formatter);
		lblZip.setLabelFor(txtZip);
		txtZip.setColumns(10);

		txtPlace = new JTextField();
		lblPlace.setLabelFor(txtPlace);
		txtPlace.setColumns(10);

		JTextField[] fields = { txtName, txtPlace, txtStreet, txtSurname, txtZip };
		for (final JTextField f : fields) {
			f.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					validateForm(f, e);
				}
			});
		}

		JButton btnSave = new JButton("Kunde Erstellen");
		btnSave.setEnabled(false);

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
												Short.MAX_VALUE).addComponent(lblStreet).addComponent(lblZip).addComponent(lblPlace)
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
										.addComponent(txtPlace, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE))
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
										.addComponent(lblPlace)
										.addComponent(txtPlace, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.UNRELATED)
						.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE).addComponent(btnSave).addComponent(btnCancel))
						.addContainerGap(58, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
	}

	protected void validateForm(JTextField f, KeyEvent e) {
		// Customer c = new Customer(txtName.getText(), txtSurname.getText(),
		// txtStreet.getText(), txtPlace.getText(),
		// Integer.parseInt("" + (Long) txtZip.getValue()));
		System.out.println(txtZip.isValid());
	}
}
