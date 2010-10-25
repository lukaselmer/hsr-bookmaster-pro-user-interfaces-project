package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import com.sun.xml.internal.ws.api.pipe.NextAction;

import application.LibraryApp;
import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Loan;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import view.loan_detail.LoanDetailTableModel;

public class LoanDetail {

	private JFrame frame;
	private JTextField txtReturnDate;
	private JTextField txtCopyID;
	private JTextField txtIdentifier;
	private Library library;
	private Customer customer;
	private JTable tblLoans;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Library l = LibraryApp.inst();
				try {
					Random r = new Random();
					new LoanDetail(l, l.getCustomers().get(r.nextInt(l.getCustomers().size())));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoanDetail(Library library, Customer customer) {
		this.library = library;
		this.customer = customer;
		initialize();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 400);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel pnlLoanInformation = new JPanel();
		pnlLoanInformation.setBorder(new TitledBorder(null, "Ausleihe Informationen", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		frame.getContentPane().add(pnlLoanInformation, BorderLayout.NORTH);

		JLabel lblIdentifier = new JLabel("Kennung:");

		JLabel lblCustomer = new JLabel("Kunde:");

		JLabel lblReturnDate = new JLabel("Zurück am:");

		JComboBox cmbCustomer = new JComboBox(library.getCustomers().toArray());
		cmbCustomer.setSelectedItem(customer);
		
		txtReturnDate = new JTextField();
		txtReturnDate.setEditable(false);
		txtReturnDate.setColumns(10);

		txtIdentifier = new JTextField("" + library.getCustomers().indexOf(customer));
		txtIdentifier.setEditable(false);
		txtIdentifier.setColumns(10);
		GroupLayout gl_pnlLoanInformation = new GroupLayout(pnlLoanInformation);
		gl_pnlLoanInformation.setHorizontalGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlLoanInformation
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING).addComponent(lblReturnDate)
										.addComponent(lblIdentifier).addComponent(lblCustomer))
						.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
						.addGroup(
								gl_pnlLoanInformation
										.createParallelGroup(Alignment.LEADING)
										.addGroup(
												gl_pnlLoanInformation
														.createParallelGroup(Alignment.LEADING, false)
														.addComponent(cmbCustomer, Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(txtReturnDate, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 315,
																GroupLayout.PREFERRED_SIZE))
										.addComponent(txtIdentifier, GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)).addContainerGap()));
		gl_pnlLoanInformation.setVerticalGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlLoanInformation
						.createSequentialGroup()
						.addGroup(
								gl_pnlLoanInformation
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblIdentifier)
										.addComponent(txtIdentifier, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_pnlLoanInformation
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblCustomer)
										.addComponent(cmbCustomer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_pnlLoanInformation
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblReturnDate)
										.addComponent(txtReturnDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pnlLoanInformation.setLayout(gl_pnlLoanInformation);

		JPanel pnlLoanAndLoanInformation = new JPanel();
		frame.getContentPane().add(pnlLoanAndLoanInformation, BorderLayout.CENTER);
		pnlLoanAndLoanInformation.setLayout(new BorderLayout(0, 0));

		JPanel pnlLoanNewCopy = new JPanel();
		pnlLoanNewCopy.setBorder(new TitledBorder(null, "Neues Exemplar ausleihen", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		pnlLoanAndLoanInformation.add(pnlLoanNewCopy, BorderLayout.NORTH);

		JLabel lblCopyID = new JLabel("Exemplar-ID:");

		txtCopyID = new JTextField();
		txtCopyID.setColumns(10);

		JButton btnExemplarAusleihen = new JButton("Exemplar Ausleihen");
		GroupLayout gl_pnlLoanNewCopy = new GroupLayout(pnlLoanNewCopy);
		gl_pnlLoanNewCopy.setHorizontalGroup(gl_pnlLoanNewCopy.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlLoanNewCopy.createSequentialGroup().addContainerGap().addComponent(lblCopyID).addGap(18)
						.addComponent(txtCopyID, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE).addGap(12)
						.addComponent(btnExemplarAusleihen, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE).addContainerGap()));
		gl_pnlLoanNewCopy.setVerticalGroup(gl_pnlLoanNewCopy.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlLoanNewCopy
						.createSequentialGroup()
						.addGroup(
								gl_pnlLoanNewCopy
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblCopyID)
										.addComponent(btnExemplarAusleihen)
										.addComponent(txtCopyID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pnlLoanNewCopy.setLayout(gl_pnlLoanNewCopy);

		JPanel pnlLoanDetailInformation = new JPanel();
		pnlLoanDetailInformation.setBorder(new TitledBorder(null, "Ausleihen von " + customer.getName() + " " + customer.getSurname(), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0,
				0, 0)));
		pnlLoanAndLoanInformation.add(pnlLoanDetailInformation, BorderLayout.CENTER);
		
		JLabel numberOfLoans = new JLabel("Anzahl Ausleihen:");
		
		JLabel number = new JLabel("" + library.getCustomerLoans(customer).size());
		
		JScrollPane scrollPnLoans = new JScrollPane();
		
		tblLoans = new JTable();
		scrollPnLoans.setViewportView(tblLoans);
		LoanDetailTableModel tblLoansModel = new LoanDetailTableModel(library, customer);
		tblLoans.setModel(tblLoansModel);
		GroupLayout gl_pnlLoanDetailInformation = new GroupLayout(pnlLoanDetailInformation);
		gl_pnlLoanDetailInformation.setHorizontalGroup(
			gl_pnlLoanDetailInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLoanDetailInformation.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlLoanDetailInformation.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlLoanDetailInformation.createSequentialGroup()
							.addComponent(numberOfLoans)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(number))
						.addComponent(scrollPnLoans, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_pnlLoanDetailInformation.setVerticalGroup(
			gl_pnlLoanDetailInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLoanDetailInformation.createSequentialGroup()
					.addGroup(gl_pnlLoanDetailInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(numberOfLoans)
						.addComponent(number))
					.addGap(18)
					.addComponent(scrollPnLoans, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)
					.addGap(193))
		);
		pnlLoanDetailInformation.setLayout(gl_pnlLoanDetailInformation);
	}
}
