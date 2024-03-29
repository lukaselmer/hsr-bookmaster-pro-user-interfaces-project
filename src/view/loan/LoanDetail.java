package view.loan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.JXTitledSeparator;

import sun.misc.Compare;
import sun.misc.Sort;
import validators.FormValidator;
import validators.SearchCopyForLoanValidator;
import validators.SearchResult;
import view.BookMasterActions;
import view.BookMasterUiManager;
import view.DocumentListenerAdapter;
import view.ViewUtil;
import view.book_master.SubFrame;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.view.ValidationComponentUtils;

import domain.Copy;
import domain.Customer;
import domain.Library;
import domain.Loan;

public class LoanDetail implements SubFrame<Customer>, Observer {

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
	private final Action actClose = new BookMasterActions.ActClose() {
		private static final long serialVersionUID = -3204995704840266054L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			library.deleteObserver(LoanDetail.this);
			frmLoanDetail.dispose();
		}
	};
	private Action actLendNewCopy = new ActLendNewCopy();
	private JMenuBar menuBar;
	private final Action actReturnLoan = new ActReturnLoan();
	private JButton btnLoanIsLost;
	private final Action actLoanIsLost = new ActLoanIsLost();
	private JMenu mnFile;
	private JMenu mnEdit;
	private JMenuItem mntClose;
	private JMenuItem mntReturnLoan;
	private JMenuItem mntLendNewCopy;
	private JMenuItem mntMarkCopyAsLost;

	/**
	 * The view for the loan details -> User can loan available copies in this
	 * view
	 * 
	 * @param uimanager
	 * @param customer
	 */
	public LoanDetail(BookMasterUiManager uimanager, Customer customer) {
		this.uimanager = uimanager;
		this.library = uimanager.getLibrary();
		this.customer = customer;
		initialize();
		frmLoanDetail.setLocationByPlatform(true);
		frmLoanDetail.setVisible(true);
		updateCustomerInformation();
		updateLoanInformation();
		library.addObserver(this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLoanDetail = new JFrame();
		ViewUtil.setIconImages(frmLoanDetail);
		frmLoanDetail.setTitle("Ausleihe Detail");
		frmLoanDetail.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frmLoanDetail.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				actClose.actionPerformed(null);
			}
		});
		frmLoanDetail.getContentPane().setLayout(new BorderLayout(0, 0));
		frmLoanDetail.setMinimumSize(new Dimension(650, 450));
		cc = new CellConstraints();

		initMenu();
		initCustomerPanel();
		initLoanPanel();
		initAddNewLoanPanel();
	}

	private void initMenu() {
		menuBar = new JMenuBar();
		frmLoanDetail.setJMenuBar(menuBar);
		mnFile = new JMenu("Datei");
		mnEdit = new JMenu("Bearbeiten");
		menuBar.add(mnFile);
		menuBar.add(mnEdit);
		mntClose = new JMenuItem(actClose);
		mntMarkCopyAsLost = new JMenuItem(actLoanIsLost);
		mntReturnLoan = new JMenuItem(actReturnLoan);
		mntLendNewCopy = new JMenuItem(actLendNewCopy);
		mnFile.add(mntClose);
		mnEdit.add(mntMarkCopyAsLost);
		mnEdit.add(mntReturnLoan);
		mnEdit.add(mntLendNewCopy);
		menuBar.add(ViewUtil.getHelpMenu(frmLoanDetail));
	}

	private void initCustomerPanel() {
		FormLayout layoutCustomer = new FormLayout("5dlu, pref, 5dlu, pref:grow, 5dlu", "3dlu, pref, 10dlu, pref, 5dlu, pref, 5dlu");
		pnlCustomer = new JPanel(layoutCustomer);
		frmLoanDetail.getContentPane().add(pnlCustomer, BorderLayout.NORTH);

		pnlCustomer.add(ViewUtil.getSeparator("Kunden Information"), cc.xyw(2, 2, 3));

		lblCustomer = new JLabel("Kunde:");
		pnlCustomer.add(lblCustomer, cc.xy(2, 4));

		customers = getCustomerArray();
		cmbCustomer = new JComboBox(customers);
		cmbCustomer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateCustomerInformation();
			}
		});

		if (customer != null) {
			cmbCustomer.setSelectedItem(customer);
		} else {
			cmbCustomer.setSelectedIndex(0);
			customer = (Customer) cmbCustomer.getItemAt(0);
		}

		cmbCustomer.setRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 6545912658140960348L;

			@Override
			public Component getListCellRendererComponent(JList list, Object customerObject, int index, boolean isSelected,
					boolean cellHasFocus) {
				Customer c = (Customer) customerObject;
				int loanAmount = library.getCustomerLoans(c).size();
				String loanAmountDescription = loanAmount == 1 ? "1 Ausleihe" : ((loanAmount == 0 ? "keine" : loanAmount) + " Ausleihen");
				String value = (c == null ? "" : (c.toString() + " (" + loanAmountDescription + ")"));
				return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			}
		});

		pnlCustomer.add(cmbCustomer, cc.xy(4, 4));
	}

	private void initLoanPanel() {
		pnlLoan = new JPanel();
		pnlLoan.setLayout(new BorderLayout(0, 0));
		frmLoanDetail.getContentPane().add(pnlLoan, BorderLayout.CENTER);

		initReturnLoanPanel();
		initLoanTable();
	}

	private void initReturnLoanPanel() {
		FormLayout layout = new FormLayout("5dlu, pref, 2dlu, pref, pref:grow, pref, 5dlu, pref, 5dlu", "pref, 5dlu, pref, 5dlu");
		pnlReturnLoan = new JPanel(layout);
		pnlLoan.add(pnlReturnLoan, BorderLayout.NORTH);
		sprCustomer = ViewUtil.getSeparator("");
		pnlReturnLoan.add(sprCustomer, cc.xyw(2, 1, 7));

		lblNumberOfLoans = new JLabel("Anzahl Ausleihen:");
		pnlReturnLoan.add(lblNumberOfLoans, cc.xy(2, 3));

		lblNumber = new JLabel();
		pnlReturnLoan.add(lblNumber, cc.xy(4, 3));

		btnLoanIsLost = new JButton(actLoanIsLost);
		actLoanIsLost.setEnabled(false);
		pnlReturnLoan.add(btnLoanIsLost, cc.xy(6, 3));

		btnReturnLoan = new JButton(actReturnLoan);
		actReturnLoan.setEnabled(false);
		pnlReturnLoan.add(btnReturnLoan, cc.xy(8, 3));
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
				actReturnLoan.setEnabled(tblLoans.getSelectedRowCount() > 0);
				actLoanIsLost.setEnabled(tblLoans.getSelectedRowCount() > 0);
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
		txtCopyId.setAction(new BookMasterActions.ActEnterPressed() {
			private static final long serialVersionUID = -8435326848168118754L;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (actLendNewCopy.isEnabled()) {
					actLendNewCopy.actionPerformed(e);
				}
			}
		});

		lblCopyId.setDisplayedMnemonic('e');
		lblCopyId.setLabelFor(txtCopyId);
		txtCopyId.setName("Exemplar.Exemplar-ID");
		ValidationComponentUtils.setMandatory(txtCopyId, true);
		pnlNewLoan.add(txtCopyId, cc.xy(4, 4));

		btnLendNewCopy = new JButton(actLendNewCopy);
		actLendNewCopy.setEnabled(false);
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
		formValidator = new FormValidator<SearchResult<Copy>>(frmLoanDetail, fields, new SearchCopyForLoanValidator(), btnLendNewCopy,
				actLendNewCopy) {
			@Override
			public SearchResult<Copy> createObject() {
				String searchString = txtCopyId.getText();
				Copy c = library.findByCopyId(searchString);
				return new SearchResult<Copy>(c, searchString);
			}
		};

		txtCopyId.getDocument().addDocumentListener(new DocumentListenerAdapter() {
			@Override
			public void documentUpdate(DocumentEvent e) {
				SearchResult<Copy> s = formValidator.validateForm(null);
				if (s == null) {
					txtBookTitle.setText("");
					txtBookAuthor.setText("");
					txtBookPublisher.setText("");
				} else {
					Copy c = s.getObject();
					if (c == null)
						assert false; // Execution should never reach this point
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
			if (customer != null) {
				lblNumber.setText("" + library.getCustomerLoans(customer).size());
				sprCustomer.setTitle("Ausleihen von " + customer.getName() + " " + customer.getSurname());
				updateLoanInformation();
			}
		}
	}

	protected void updateLoanInformation() {
		loanTableModel.updateObjects(customer);
		lblNumber.setText("" + library.getCustomerLoans(customer).size());
		actLendNewCopy.setEnabled(library.getCustomerLoans(customer).size() < 3 && !(library.hasCustomerOverdueBooks(customer)));
		txtCopyId.setEnabled(library.getCustomerLoans(customer).size() < 3 && !(library.hasCustomerOverdueBooks(customer)));
		if (txtCopyId.isEnabled()) {
			txtCopyId.setText("");
			txtCopyId.setToolTipText("muss ausgefüllt werden");
			ValidationComponentUtils.setMandatoryBackground(txtCopyId);
			formValidator.enablePopups();
			formValidator.validateForm(null);
		} else {
			formValidator.disablePopups();
			txtCopyId.setToolTipText(null);
			btnLendNewCopy.setToolTipText(null);
			txtCopyId.setText(library.hasCustomerOverdueBooks(customer) ? "Überfällige Ausleihe" : "Maximale Anzahl Ausleihen erreicht");
			ValidationComponentUtils.setErrorBackground(txtCopyId);
			formValidator.validateForm(null);
		}
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

	public void updateCustomer(Customer c) {
		cmbCustomer.setSelectedItem(c);
	}

	@Override
	public boolean isVisible() {
		return frmLoanDetail.isVisible();
	}

	@Override
	public void addWindowListener(WindowAdapter windowAdapter) {
		frmLoanDetail.addWindowListener(windowAdapter);
	}

	private Object[] getCustomerArray() {
		Object[] customers;
		customers = library.getCustomers().toArray();
		Sort.quicksort(customers, new Compare() {
			@Override
			public int doCompare(Object o1, Object o2) {
				Customer s1 = (Customer) o1, s2 = (Customer) o2;
				return s1.toString().compareTo(s2.toString());
			}
		});
		return customers;
	}

	@Override
	public void update(Observable o, Object arg) {
		customers = getCustomerArray();
		Customer c = customer;
		cmbCustomer.setModel(new DefaultComboBoxModel(customers));
		cmbCustomer.setSelectedIndex(0);
		customer = (Customer) cmbCustomer.getItemAt(0);
		if (c != null) {
			cmbCustomer.setSelectedItem(c);
			customer = c;
		}
		cmbCustomer.setSelectedItem(customer);
	}

	private class ActReturnLoan extends AbstractAction {
		private static final long serialVersionUID = 5728087231118105568L;

		public ActReturnLoan() {
			putValue(NAME, "Selektierte Zurückgeben...");
			putValue(MNEMONIC_KEY, KeyEvent.VK_Z);
			putValue(SHORT_DESCRIPTION, "Gibt selektierte Bücher zurück");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK));
			putValue(SMALL_ICON, new ImageIcon("data/icons/return.png"));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			List<Loan> list = getSelectedLoans();
			if (JOptionPane.showConfirmDialog(frmLoanDetail, "Sind Sie sicher, dass Sie " + list.size() + " Ausleihe"
					+ (list.size() > 1 ? "n" : "") + " zurückgeben wollen?", "Selektierte Ausleihen zurückgeben",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				for (Loan l : list) {
					l.returnCopy();
				}
				uimanager.openLoansReportFrame(list);
				updateLoanInformation();
			}
		}
	}

	private class ActLendNewCopy extends AbstractAction {
		private static final long serialVersionUID = 7524200258063461521L;

		public ActLendNewCopy() {
			putValue(NAME, "Exemplar Ausleihen");
			putValue(MNEMONIC_KEY, KeyEvent.VK_A);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
			putValue(SHORT_DESCRIPTION, "Leiht angegebenes Buch aus");
			putValue(SMALL_ICON, new ImageIcon("data/icons/loan_it.png"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEnabled()) {
				return;
			}
			Copy c = formValidator.validateForm(null).getObject();
			if (c == null)
				assert false; // Execution should never reach this point
			library.createAndAddLoan(customer, c);
			updateCustomerInformation();
			txtBookTitle.setText("");
			txtBookAuthor.setText("");
			txtBookPublisher.setText("");
		}
	}

	private class ActLoanIsLost extends AbstractAction {
		private static final long serialVersionUID = 5015049695818151480L;

		public ActLoanIsLost() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_V);
			putValue(NAME, "Selektierte Als Verloren Markieren");
			putValue(SHORT_DESCRIPTION, "Markiert selektierte Ausleihen als verloren und entfernt diese");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK));
			putValue(SMALL_ICON, new ImageIcon("data/icons/lightning.png"));
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			List<Loan> list = getSelectedLoans();
			if (JOptionPane.showConfirmDialog(frmLoanDetail,
					"Sind Sie sicher, dass Sie " + list.size() + " Ausleihe" + (list.size() > 1 ? "n" : "")
							+ " als verloren markieren wollen? Das Exemplar und die Ausleihe wird dadurch entfernt.",
					"Selektierte Ausleihen als verloren markieren", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				for (Loan l : list) {
					library.loanLost(l);
				}
				updateLoanInformation();
			}
		}
	}
}
