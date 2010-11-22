package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTitledSeparator;

import sun.misc.Compare;
import sun.misc.Sort;
import validators.FormValidator;
import validators.SearchResult;
import validators.SearchResultValidator;
import view.book_master.SubFrame;
import view.loan_detail.LoanDetailTableModel;
import application.LibraryApp;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.view.ValidationComponentUtils;

import domain.Book;
import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Loan;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoanDetail implements SubFrame<Customer> {

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
	private JXTitledSeparator sprCustomer;
	protected FormValidator<SearchResult<Copy>> formValidator;
	private JButton btnLendNewCopy;
	private JLabel lblTitle;
	private JLabel lblAuthor;
	private JLabel lblPublisher;
	private JTextField txtBookTitle;
	private JTextField txtBookAuthor;
	private JTextField txtBookPublisher;
	private CellConstraints cc;
	private Object[] customers;
	private JLabel lblNumberOfLoans;
	private JScrollPane scrollPane;
	private JPanel pnlNewLoan;
	private JLabel lblCopyId;
	private JPanel pnlLoan;
	private JPanel pnlReturnLoan;
	private JButton btnReturnLoan;
	private BookMasterUiManager uimanager;

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
					new LoanDetail(new BookMasterUiManager(l), l.getCustomers().get(r.nextInt(l.getCustomers().size())));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public LoanDetail(BookMasterUiManager uimanager, Customer customer) {
		this.uimanager = uimanager;
		this.library = uimanager.getLibrary();
		this.customer = customer;
		initialize();
		frmLoanDetail.setLocationByPlatform(true);
		frmLoanDetail.setVisible(true);
		updateCustomerInformation();
	}

	public LoanDetail(BookMasterUiManager uimanager, Loan loan) {
		this(uimanager, loan == null ? null : loan.getCustomer());
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLoanDetail = new JFrame();
		frmLoanDetail.setTitle("Ausleihe Detail");
		frmLoanDetail.setBounds(100, 100, 600, 450);
		frmLoanDetail.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmLoanDetail.getContentPane().setLayout(new BorderLayout(0, 0));
		frmLoanDetail.setMinimumSize(new Dimension(400, 300));
		cc = new CellConstraints();

		initCustomerPanel();
		initLoanPanel();
		initAddNewLoanPanel();
	}

	private void initCustomerPanel() {
		FormLayout layoutCustomer = new FormLayout("5dlu, pref, 5dlu, pref, 5dlu, pref:grow, 5dlu",
				"3dlu, pref, 10dlu, pref, 5dlu, pref, 5dlu");
		pnlCustomer = new JPanel(layoutCustomer);
		frmLoanDetail.getContentPane().add(pnlCustomer, BorderLayout.NORTH);

		pnlCustomer.add(ViewUtil.getSeparator("Kunden Information"), cc.xyw(2, 2, 5));

		lblCustomer = new JLabel("Kunde:");
		pnlCustomer.add(lblCustomer, cc.xy(2, 4));

		customers = library.getCustomers().toArray();
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

		lblNumberOfLoans = new JLabel("Anzahl Ausleihen:");
		pnlCustomer.add(lblNumberOfLoans, cc.xy(2, 6));

		lblNumber = new JLabel();
		pnlCustomer.add(lblNumber, cc.xy(4, 6));
	}

	private void initLoanPanel() {
		pnlLoan = new JPanel();
		pnlLoan.setLayout(new BorderLayout(0, 0));
		frmLoanDetail.getContentPane().add(pnlLoan, BorderLayout.CENTER);

		initReturnLoanPanel();
		initLoanTable();
	}

	private void initReturnLoanPanel() {
		FormLayout layout = new FormLayout("5dlu, pref:grow, 5dlu", "pref, 5dlu, pref, 5dlu");
		pnlReturnLoan = new JPanel(layout);
		pnlLoan.add(pnlReturnLoan, BorderLayout.NORTH);
		sprCustomer = ViewUtil.getSeparator("");
		pnlReturnLoan.add(sprCustomer, cc.xy(2, 1));
		btnReturnLoan = new JButton("Ausgewählte Zurückgeben");
		btnReturnLoan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Loan> list = getSelectedLoans();
				for (Loan l : list) {
					// TODO: Meldung anzeigen
					// library.getLoans().remove(l);
					l.returnCopy();
				}
				updateLoanInformation();
			}
		});
		btnReturnLoan.setEnabled(false);
		pnlReturnLoan.add(btnReturnLoan, cc.xy(2, 3, "right, bottom"));
	}

	private void initLoanTable() {
		scrollPane = new JScrollPane();
		pnlLoan.add(scrollPane, BorderLayout.CENTER);

		loanTableModel = new LoanDetailTableModel(library, customer);
		tblLoans = new JTable() {

			private static final long serialVersionUID = -3462714108092442892L;

			@Override
			public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);
				if (!isCellSelected(row, column)) {
					Color col = colorForRow(row);
					c.setBackground(col != null ? col : UIManager.getColor("Table.background"));
					c.setForeground(UIManager.getColor("Table.foreground"));
				} else {
					c.setBackground(UIManager.getColor("Table.selectionBackground"));
					c.setForeground(UIManager.getColor("Table.selectionForeground"));
				}
				return c;
			}

			private Color colorForRow(int row) {
				Loan l = (Loan) getModel().getValueAt(convertRowIndexToModel(row), -1);
				return l.isOverdue() ? Color.ORANGE : null;
			};
		};
		tblLoans.setColumnSelectionAllowed(false);
		tblLoans.setRowSelectionAllowed(true);
		tblLoans.setModel(loanTableModel);
		scrollPane.setViewportView(tblLoans);
		tblLoans.getColumn("" + LoanDetailTableModel.ColumnName.STATUS).setMinWidth(50);
		tblLoans.getColumn("" + LoanDetailTableModel.ColumnName.STATUS).setMaxWidth(50);
		tblLoans.getColumn("" + LoanDetailTableModel.ColumnName.INVENTORY_NUMBER).setMinWidth(80);
		tblLoans.getColumn("" + LoanDetailTableModel.ColumnName.INVENTORY_NUMBER).setMaxWidth(80);
		tblLoans.getColumn("" + LoanDetailTableModel.ColumnName.LOAN_UNTIL).setMinWidth(100);
		tblLoans.getColumn("" + LoanDetailTableModel.ColumnName.LOAN_UNTIL).setMaxWidth(100);
		tblLoans.setColumnSelectionAllowed(false);
		tblLoans.setRowSelectionAllowed(true);
		TableRowSorter<LoanDetailTableModel> rowSorter = new TableRowSorter<LoanDetailTableModel>(loanTableModel);
		rowSorter.setComparator(1, new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {
				try {
					Date d1 = DateFormat.getDateInstance().parse(s1);
					Date d2 = DateFormat.getDateInstance().parse(s2);
					return d1.compareTo(d2);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
		rowSorter.setComparator(2, new Comparator<Long>() {
			@Override
			public int compare(Long l1, Long l2) {
				return l1.compareTo(l2);
			}

		});
		tblLoans.setRowSorter(rowSorter);

		tblLoans.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				btnReturnLoan.setEnabled(tblLoans.getSelectedRowCount() > 0);
			}
		});
	}

	private void initAddNewLoanPanel() {
		FormLayout newLoanLayout = new FormLayout("5dlu, pref, 5dlu, pref:grow, 5dlu, pref, 5dlu",
				"5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 10dlu");
		pnlNewLoan = new JPanel(newLoanLayout);
		frmLoanDetail.getContentPane().add(pnlNewLoan, BorderLayout.SOUTH);

		pnlNewLoan.add(ViewUtil.getSeparator("Exemplar ausleihen"), cc.xyw(2, 2, 5));

		lblCopyId = new JLabel("Exemplar-ID:");
		pnlNewLoan.add(lblCopyId, cc.xy(2, 4));

		txtCopyId = new JTextField();

		lblCopyId.setDisplayedMnemonic('e');
		lblCopyId.setLabelFor(txtCopyId);
		txtCopyId.setName("Exemplar.Exemplar-ID");
		ValidationComponentUtils.setMandatory(txtCopyId, true);
		pnlNewLoan.add(txtCopyId, cc.xy(4, 4));

		btnLendNewCopy = new JButton("Exemplar Ausleihen");
		btnLendNewCopy.setEnabled(false);
		btnLendNewCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Copy c = formValidator.validateForm(null).getObject();
				if (c == null)
					throw new RuntimeException("Bad state");
				library.createAndAddLoan(customer, c);
				updateCustomerInformation();
				txtBookTitle.setText("");
				txtBookAuthor.setText("");
				txtBookPublisher.setText("");
			}
		});
		pnlNewLoan.add(btnLendNewCopy, cc.xy(6, 4));

		lblTitle = new JLabel("Titel:");
		pnlNewLoan.add(lblTitle, cc.xy(2, 6));

		txtBookTitle = new JTextField();
		txtBookTitle.setEditable(false);
		pnlNewLoan.add(txtBookTitle, cc.xyw(4, 6, 3));

		lblAuthor = new JLabel("Autor:");
		pnlNewLoan.add(lblAuthor, cc.xy(2, 8));

		txtBookAuthor = new JTextField();
		txtBookAuthor.setEditable(false);
		pnlNewLoan.add(txtBookAuthor, cc.xyw(4, 8, 3));

		lblPublisher = new JLabel("Verlag:");
		pnlNewLoan.add(lblPublisher, cc.xy(2, 10));

		txtBookPublisher = new JTextField();
		txtBookPublisher.setEditable(false);
		pnlNewLoan.add(txtBookPublisher, cc.xyw(4, 10, 3));

		JTextField[] fields = { txtCopyId };
		formValidator = new FormValidator<SearchResult<Copy>>(frmLoanDetail, fields, new SearchResultValidator(), btnLendNewCopy) {
			@Override
			public SearchResult<Copy> createObject() {
				String searchString = txtCopyId.getText();
				Copy c = library.findByCopyId(searchString);
				return new SearchResult<Copy>(c, searchString);
			}
		};

		txtCopyId.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent arg0) {
				SearchResult<Copy> s = formValidator.validateForm(null);
				if (s == null) {
					txtBookTitle.setText("");
					txtBookAuthor.setText("");
					txtBookPublisher.setText("");
				} else {
					Copy c = s.getObject();
					if (c == null)
						throw new RuntimeException("Bad state");
					txtBookTitle.setText(c.getBook().getName());
					txtBookAuthor.setText(c.getBook().getAuthor());
					txtBookPublisher.setText(c.getBook().getPublisher());
				}
			}
		});
	}

	protected void updateCustomerInformation() {
		if (cmbCustomer != null && lblNumber != null) {
			customer = (Customer) cmbCustomer.getSelectedItem();
			lblNumber.setText("" + library.getCustomerLoans(customer).size());
			sprCustomer.setTitle("Ausleihen von " + customer.getName() + " " + customer.getSurname());
			updateLoanInformation();
		}
	}

	protected void updateLoanInformation() {
		loanTableModel.updateLoans(library.getCustomerLoans(customer));
		btnLendNewCopy.setEnabled(library.getCurrentLoans().size() < 3);
		txtCopyId.setEnabled(library.getCustomerLoans(customer).size() < 3);
		txtCopyId.setText("" + (txtCopyId.isEnabled() ? "" : "Maximale Anzahl Ausleihen erreicht"));
	}

	protected List<Loan> getSelectedLoans() {
		List<Loan> list = new ArrayList<Loan>();
		for (int row : tblLoans.getSelectedRows()) {
			list.add(getLoanOfRow(row));
		}
		return list;
	}

	protected Loan getLoanOfRow(int row) {
		return (Loan) tblLoans.getValueAt(row, -1);
	}

	@Override
	public Customer getObject() {
		return customer;
	}

	@Override
	public void toFront() {
		frmLoanDetail.toFront();
	}

	public void updateLoan(Loan l) {
		cmbCustomer.setSelectedItem(l.getCustomer());
		// updateCustomerInformation

	}

	@Override
	public boolean isValid() {
		return frmLoanDetail.isValid();
	}

	@Override
	public void addWindowListener(WindowAdapter windowAdapter) {
		frmLoanDetail.addWindowListener(windowAdapter);
	}
}
