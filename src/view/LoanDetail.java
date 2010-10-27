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

import sun.misc.Compare;
import sun.misc.Sort;
import view.loan_detail.LoanDetailTableModel;

public class LoanDetail {

	private JFrame frmAusleiheDetail;
	private JTextField txtReturnDate;
	private JTextField txtCopyID;
	private JTextField txtIdentifier;
	private Library library;
	private Customer customer;
	private JTable table;

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
		frmAusleiheDetail.setLocationByPlatform(true);
		frmAusleiheDetail.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAusleiheDetail = new JFrame();
		frmAusleiheDetail.setTitle("Ausleihe Detail");
		frmAusleiheDetail.setBounds(100, 100, 450, 400);
		frmAusleiheDetail.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAusleiheDetail.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel pnlLoanInformation = new JPanel();
		pnlLoanInformation.setBorder(new TitledBorder(null, "Ausleihe Informationen", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		frmAusleiheDetail.getContentPane().add(pnlLoanInformation, BorderLayout.NORTH);

		JLabel lblIdentifier = new JLabel("Kennung:");

		JLabel lblCustomer = new JLabel("Kunde:");

		JLabel lblReturnDate = new JLabel("Zurück am:");
		Object[] customers = library.getCustomers().toArray();
		Sort.quicksort(customers, new Compare() {
			
			@Override
			public int doCompare(Object o1, Object o2) {
				Customer s1 = (Customer) o1, s2 = (Customer) o2; 
				return s1.toString().compareTo(s2.toString());
			}
		});
		JComboBox cmbCustomer = new JComboBox(customers);
		cmbCustomer.setSelectedItem(customer);
		
		txtReturnDate = new JTextField();
		txtReturnDate.setEditable(false);
		txtReturnDate.setColumns(10);

		txtIdentifier = new JTextField("" + library.getCustomers().indexOf(customer));
		txtIdentifier.setEditable(false);
		txtIdentifier.setColumns(10);
		GroupLayout gl_pnlLoanInformation = new GroupLayout(pnlLoanInformation);
		gl_pnlLoanInformation.setHorizontalGroup(
			gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLoanInformation.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING)
						.addComponent(lblReturnDate)
						.addComponent(lblIdentifier)
						.addComponent(lblCustomer))
					.addGap(23)
					.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING)
							.addComponent(cmbCustomer, Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(txtReturnDate, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
						.addComponent(txtIdentifier, GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_pnlLoanInformation.setVerticalGroup(
			gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLoanInformation.createSequentialGroup()
					.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIdentifier)
						.addComponent(txtIdentifier, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCustomer)
						.addComponent(cmbCustomer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblReturnDate)
						.addComponent(txtReturnDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlLoanInformation.setLayout(gl_pnlLoanInformation);

		JPanel pnlLoanAndLoanInformation = new JPanel();
		frmAusleiheDetail.getContentPane().add(pnlLoanAndLoanInformation, BorderLayout.CENTER);
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
		gl_pnlLoanNewCopy.setHorizontalGroup(
			gl_pnlLoanNewCopy.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLoanNewCopy.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCopyID)
					.addGap(18)
					.addComponent(txtCopyID, GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
					.addGap(12)
					.addComponent(btnExemplarAusleihen, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_pnlLoanNewCopy.setVerticalGroup(
			gl_pnlLoanNewCopy.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLoanNewCopy.createSequentialGroup()
					.addGroup(gl_pnlLoanNewCopy.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCopyID)
						.addComponent(btnExemplarAusleihen)
						.addComponent(txtCopyID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlLoanNewCopy.setLayout(gl_pnlLoanNewCopy);
		
		JPanel pnlLoanInformationTable = new JPanel();
		pnlLoanAndLoanInformation.add(pnlLoanInformationTable, BorderLayout.CENTER);
		pnlLoanInformationTable.setLayout(new BorderLayout(0, 0));
		
		JPanel pnlNumberOfLoans = new JPanel();
		pnlNumberOfLoans.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Ausleihen von " + customer.getName() + " " + customer.getSurname(), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlLoanInformationTable.add(pnlNumberOfLoans, BorderLayout.NORTH);
		
		JLabel lblNumberOfLoans = new JLabel("Anzahl Ausleihen:");
		
		JLabel lblNumber = new JLabel("" + library.getCustomerLoans(customer).size());
		GroupLayout gl_pnlNumberOfLoans = new GroupLayout(pnlNumberOfLoans);
		gl_pnlNumberOfLoans.setHorizontalGroup(
			gl_pnlNumberOfLoans.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlNumberOfLoans.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNumberOfLoans)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNumber)
					.addContainerGap(284, Short.MAX_VALUE))
		);
		gl_pnlNumberOfLoans.setVerticalGroup(
			gl_pnlNumberOfLoans.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlNumberOfLoans.createSequentialGroup()
					.addGroup(gl_pnlNumberOfLoans.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNumberOfLoans)
						.addComponent(lblNumber))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlNumberOfLoans.setLayout(gl_pnlNumberOfLoans);
		
		JScrollPane scrollPane = new JScrollPane();
		pnlLoanInformationTable.add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		LoanDetailTableModel tblLoansModel = new LoanDetailTableModel(library, customer);
		table.setModel(tblLoansModel);
		table.getColumn("" + LoanDetailTableModel.ColumnName.INVENTORY_NUMBER).setMaxWidth(75);
		table.getColumn("" + LoanDetailTableModel.ColumnName.INVENTORY_NUMBER).setMinWidth(75);
		table.getColumn("" + LoanDetailTableModel.ColumnName.INVENTORY_NUMBER).setResizable(false);
		table.getColumn("" + LoanDetailTableModel.ColumnName.TITLE).setMinWidth(180);
		
	}
}
