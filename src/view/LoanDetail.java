package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.jdesktop.swingx.JXTitledSeparator;

import sun.misc.Compare;
import sun.misc.Sort;
import validators.FormValidator;
import validators.LoanValidator;
import view.loan_detail.LoanDetailTableModel;
import application.LibraryApp;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import domain.Copy;
import domain.Customer;
import domain.Library;

public class LoanDetail {

	private JFrame frmLoanDetail;
	private Library library;
	private Customer customer;
	private JPanel pnlCustomer;
	private JLabel lblCustomer;
	private JTable tblLoans;
	private JLabel lblNumber;
	private LoanDetailTableModel loanTableModel;
	private JComboBox cmbCustomer;
	private JTextField txtCopyId;
	private JXTitledSeparator customerSeparator;
	protected FormValidator<Copy> formValidator;

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
		frmLoanDetail.setLocationByPlatform(true); 
		frmLoanDetail.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLoanDetail = new JFrame();
		frmLoanDetail.setTitle("Ausleihe Detail");
		frmLoanDetail.setBounds(100, 100, 500, 300);
		frmLoanDetail.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmLoanDetail.getContentPane().setLayout(new BorderLayout(0, 0));
		frmLoanDetail.setMinimumSize(new Dimension(400, 300));

		CellConstraints cc = new CellConstraints();

		// Panel Customer
		FormLayout layoutCustomer = new FormLayout("5dlu, pref, 5dlu, pref, 5dlu, pref:grow, 5dlu",
				"3dlu, pref, 10dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu");
		pnlCustomer = new JPanel(layoutCustomer);
		frmLoanDetail.getContentPane().add(pnlCustomer, BorderLayout.NORTH);

		pnlCustomer.add(ViewUtil.getSeparator("Kunden Information"), cc.xyw(2, 2, 5));

		lblCustomer = new JLabel("Kunde:");
		pnlCustomer.add(lblCustomer, cc.xy(2, 4));

		Object[] customers = library.getCustomers().toArray();
		Sort.quicksort(customers, new Compare() {

			@Override
			public int doCompare(Object o1, Object o2) {
				Customer s1 = (Customer) o1, s2 = (Customer) o2;
				return s1.toString().compareTo(s2.toString());
			}
		});
		cmbCustomer = new JComboBox(customers);
		cmbCustomer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateCustomerInformation();
			}
		});

		cmbCustomer.setSelectedItem(customer);
		pnlCustomer.add(cmbCustomer, cc.xyw(4, 4, 3));

		JLabel lblNumberOfLoans = new JLabel("Anzahl Ausleihen:");
		pnlCustomer.add(lblNumberOfLoans, cc.xy(2, 6));

		lblNumber = new JLabel();
		pnlCustomer.add(lblNumber, cc.xy(4, 6));

		customerSeparator = ViewUtil.getSeparator("");
		pnlCustomer.add(customerSeparator, cc.xyw(2, 8, 5));

		// JTable
		JScrollPane scrollPane = new JScrollPane();
		frmLoanDetail.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		loanTableModel = new LoanDetailTableModel(library, customer);
		tblLoans = new JTable(loanTableModel);
		tblLoans.setColumnSelectionAllowed(false);
		tblLoans.setRowSelectionAllowed(true);
		scrollPane.setViewportView(tblLoans);
		tblLoans.getColumn("" + LoanDetailTableModel.ColumnName.INVENTORY_NUMBER).setMinWidth(80);
		tblLoans.getColumn("" + LoanDetailTableModel.ColumnName.INVENTORY_NUMBER).setMaxWidth(80);
		
		//Panel Loan
		FormLayout newLoanLayout = new FormLayout("5dlu, pref, 5dlu, pref:grow, 5dlu, pref, 5dlu", "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu");
		JPanel pnlNewLoan = new JPanel(newLoanLayout);
		frmLoanDetail.getContentPane().add(pnlNewLoan, BorderLayout.SOUTH);

		pnlNewLoan.add(ViewUtil.getSeparator("Exemplar ausleihen"), cc.xyw(2, 2, 5));

		JLabel lblCopyId = new JLabel("Exemplar-ID:");
		pnlNewLoan.add(lblCopyId, cc.xy(2, 4));

		txtCopyId = new JTextField();
		pnlNewLoan.add(txtCopyId, cc.xy(4, 4));

		JButton btnLendNewCopy = new JButton("Exemplar ausleihen");
		btnLendNewCopy.setEnabled(false);
		pnlNewLoan.add(btnLendNewCopy, cc.xy(6, 4));
		
		JLabel lblCopyInformation = new JLabel("Test");
		pnlNewLoan.add(lblCopyInformation, cc.xy(2, 6));
		
		JTextField[] fields = {txtCopyId};
		formValidator = new FormValidator<Copy>(frmLoanDetail, fields, new LoanValidator(), btnLendNewCopy) {
			
			@Override
			public Copy createObject() {
				Integer copyId = null;
				try {
					copyId = Integer.parseInt(txtCopyId.toString());
				} catch (NumberFormatException ex){}
				return null;
			}
		};
		

		updateCustomerInformation();
	}

	protected void updateCustomerInformation() {
		if (cmbCustomer != null && lblNumber != null) {
			customer = (Customer) cmbCustomer.getSelectedItem();
			lblNumber.setText("" + library.getCustomerLoans(customer).size());
			customerSeparator.setTitle("Ausleihen von " + customer.getName() + " " + customer.getSurname());
			loanTableModel.updateLoans(library.getCustomerLoans(customer));
			txtCopyId.setEnabled(library.getCustomerLoans(customer).size() < 3);
			txtCopyId.setText("" + (txtCopyId.isEnabled() ? "" : "Maximale Anzahl Ausleihen erreicht"));
		}
	}

}
