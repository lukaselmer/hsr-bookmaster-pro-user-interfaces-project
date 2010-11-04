package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.TextField;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.Component;
import javax.swing.Box;

import org.jdesktop.swingx.JXTitledSeparator;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoanDetail {

	private JFrame frmLoanDetail;
	private Library library;
	private Customer customer;
	private JPanel pnlCustomer;
	private JLabel lblCustomer;
	private JPanel pnlLoans;
	private JTable tblLoans;
	private JPanel pnlLendNewCopy;
	private JLabel lblNumber;
	private LoanDetailTableModel loanTableModel;
	private JComboBox cmbCustomer;
	private JTextField txtCopyId;
	private JXTitledSeparator customerSeparator;

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
		frmLoanDetail.getContentPane().setLayout(new BorderLayout(0, 0));
		frmLoanDetail.setVisible(true);
		frmLoanDetail.setMinimumSize(new Dimension(400, 300));
        CellConstraints cc = new CellConstraints();
		
		Object[] customers = library.getCustomers().toArray();
		Sort.quicksort(customers, new Compare() {
			
			@Override
			public int doCompare(Object o1, Object o2) {
				Customer s1 = (Customer) o1, s2 = (Customer) o2; 
				return s1.toString().compareTo(s2.toString());
			}
		});
		loanTableModel = new LoanDetailTableModel(library, customer);
		
		FormLayout layoutCustomer = new FormLayout("5dlu, right:pref, 5dlu, pref, 5dlu, pref:grow, 5dlu", "3dlu, pref, 10dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu");
		pnlCustomer = new JPanel(layoutCustomer);
		frmLoanDetail.getContentPane().add(pnlCustomer, BorderLayout.NORTH);
		
		
		pnlCustomer.add(ViewUtil.getSeparator("Kunden Information"), cc.xyw(2, 2, 5));
		
		lblCustomer = new JLabel("Kunde:");
		pnlCustomer.add(lblCustomer, cc.xy(2, 4));
		
		
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
		
		JScrollPane scrollPane = new JScrollPane();
		frmLoanDetail.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		tblLoans = new JTable();
		tblLoans.setColumnSelectionAllowed(false);
		tblLoans.setRowSelectionAllowed(true);
		scrollPane.setViewportView(tblLoans);
		tblLoans.setModel(loanTableModel);
		tblLoans.getColumn("" + LoanDetailTableModel.ColumnName.INVENTORY_NUMBER).setMinWidth(80);
		tblLoans.getColumn("" + LoanDetailTableModel.ColumnName.INVENTORY_NUMBER).setMaxWidth(80);
		
		FormLayout newLoanLayout = new FormLayout("5dlu, pref, 5dlu, pref:grow, 5dlu, pref, 5dlu", "5dlu, pref, 5dlu, pref, 5dlu");
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLoanDetail = new JFrame();
		frmLoanDetail.setTitle("Ausleihe Detail");
		frmLoanDetail.setBounds(100, 100, 500, 300);
		frmLoanDetail.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
