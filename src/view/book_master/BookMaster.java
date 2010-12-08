package view.book_master;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.jdesktop.swingx.autocomplete.ComboBoxCellEditor;

import view.BookMasterActions;
import view.BookMasterUiManager;
import view.DocumentListenerAdapter;
import view.DoubleClickMouseAdapter;
import view.ViewUtil;
import view.customer.CustomerNew;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import domain.Book;
import domain.Customer;
import domain.Library;
import domain.Loan;
import domain.Shelf;

public class BookMaster implements Observer {
	private static final int INDEX_OF_BOOKS_TAB = 0, INDEX_OF_LOANS_TAB = 1, INDEX_OF_CUSTOMERS_TAB = 2;
	private JButton btnShowSelectedBooks;
	private JButton btnShowSelectedCustomers;
	private JButton btnLoanForSelectedCustomer;
	private JButton btnShowSelectedLoans;
	private JButton btnReturnSelectedLoans;
	private JCheckBox chckbxAvailibleOnly;
	private JCheckBox chckbxOverduesOnly;
	private JFrame frmBookmaster;
	private JLabel lblBooksAmountNum;
	private JLabel lblCurrentlyLoanedNum;
	private JLabel lblCustomersAmountNum;
	private JLabel lblExemplarAmountNum;
	private JLabel lblLoansAmountNum;
	private JLabel lblOverdueAmountNum;
	private Library library;
	protected CustomerNew newCustomerFrame;
	private JPanel pnlBooks;
	private JPanel pnlCustomers;
	private JPanel pnlLoans;
	private JScrollPane scrollTblBooks;
	private JScrollPane scrollTblCustomers;
	private JScrollPane scrollTblLoans;
	private JTabbedPane tabbedPane;
	private JTable tblBooks;
	private BookMasterTableModelBook tblBooksModel;
	private JTable tblCustomers;
	private BookMasterTableModelCustomer tblCustomersModel;
	private JTable tblLoans;
	private BookMasterTableModelLoan tblLoansModel;
	private JTextField txtFilterBooks;
	private JTextField txtFilterCustomers;
	private JTextField txtFilterLoans;
	private BookMasterUiManager uimanager;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenu mnBooks;
	private JMenu mnLoans;
	private JMenuItem mntClose;
	private JMenuItem mntShowSelectedBooks;
	private JMenuItem mntNewBook;
	private final Action actNewBook = new SwingAction();
	private final Action actClose = new BookMasterActions.ActClose() {
		
		private static final long serialVersionUID = -1552611178018631110L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (JOptionPane.showConfirmDialog(frmBookmaster, "Sind Sie sicher? Alle Unterfenster werden geschlossen.",
					"BookMasterPro Schliessen", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				frmBookmaster.setVisible(false);
				library.deleteObserver(BookMaster.this);
				frmBookmaster.dispose();
				System.exit(0);

			}
		}
	};
	private JMenuItem mnNewLoan;
	private final Action actNewLoan = new ActNewLoan();
	private JMenuItem mntNewCustomer;
	private final Action actNewCustomer = new ActNewCustomer();
	private final Action actShowSelectedBooks = new ActShowSelectedBooks();
	private JMenu mnClients;
	private final Action actOverduesOnly = new ActOverduesOnly();
	private JMenuItem mntShowSelectedLoans;
	private JMenuItem mntReturnSelectedLoans;
	private final Action actShowSelectedLoans = new ActShowSelectedLoans();
	private final Action actReturnSelectedLoans = new ActReturnSelectedLoans();
	private JMenuItem mntShowSelectedCustomers;
	private JMenuItem mntLoansForSelectedCustomer;
	private final Action actShowSelectedCustomers = new ActShowSelectedCustomers();
	private final Action actLoanForSelectedCustomer = new ActLoanForSelectedCustomer();

	/**
	 * The main view for the BookMaster
	 * 
	 * @param library
	 */
	public BookMaster(Library library) {
		uimanager = new BookMasterUiManager(library);
		this.library = uimanager.getLibrary();
		initialize();
		library.addObserver(this);
		updateBooksStatistics();
		updateLoansStatistics();
		updateCustomersStatistics();
		frmBookmaster.setLocationByPlatform(true);
		frmBookmaster.setVisible(true);
		txtFilterBooks.requestFocusInWindow();
	}

	private void initMenu() {
		menuBar = new JMenuBar();
		frmBookmaster.setJMenuBar(menuBar);

		mnFile = new JMenu("Datei");
		mnFile.setMnemonic('d');
		menuBar.add(mnFile);
		mntClose = new JMenuItem(actClose);
		mnFile.add(mntClose);

		mnBooks = new JMenu("Bücher");
		menuBar.add(mnBooks);
		mntShowSelectedBooks = new JMenuItem(actShowSelectedBooks);
		mnBooks.add(mntShowSelectedBooks);
		mntNewBook = new JMenuItem(actNewBook);
		mnBooks.add(mntNewBook);
		
		mnLoans = new JMenu("Ausleihen");
		menuBar.add(mnLoans);
		mntReturnSelectedLoans = new JMenuItem(actReturnSelectedLoans);
		mnLoans.add(mntReturnSelectedLoans);
		mntShowSelectedLoans = new JMenuItem(actShowSelectedLoans);
		mnLoans.add(mntShowSelectedLoans);
		mnNewLoan = new JMenuItem(actNewLoan);
		mnLoans.add(mnNewLoan);

		mnClients = new JMenu("Kunden");
		menuBar.add(mnClients);
		mntLoansForSelectedCustomer = new JMenuItem(actLoanForSelectedCustomer);
		mnClients.add(mntLoansForSelectedCustomer);
		mntShowSelectedCustomers = new JMenuItem(actShowSelectedCustomers);
		mnClients.add(mntShowSelectedCustomers);
		mntNewCustomer = new JMenuItem(actNewCustomer);
		mnClients.add(mntNewCustomer);		

		menuBar.add(ViewUtil.getHelpMenu(frmBookmaster));
	}

	public BookMaster inst() {
		return this;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBookmaster = new JFrame();
		frmBookmaster.setTitle("BookMasterPro");
		frmBookmaster.setBounds(100, 100, 950, 600);
		frmBookmaster.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmBookmaster.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				actClose.actionPerformed(null);
			}
		});
		frmBookmaster.getContentPane().setLayout(new BorderLayout(0, 0));
		frmBookmaster.setMinimumSize(new Dimension(775, 400));
		initMenu();

		tabbedPane = new JTabbedPane(SwingConstants.TOP) {
			private static final long serialVersionUID = -316298348154524977L;

			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2 = (Graphics2D) g;
				g2.drawImage(new ImageIcon("data/icons/logo2.png").getImage(), this.getWidth() - 227, 3, this);
				// Demo: 2D Wolke zeichnen
				// g2.setColor(new Color(100, 240, 240, 130));
				// g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
				// java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
				// java.awt.geom.Ellipse2D cloudPuff1 = new
				// java.awt.geom.Ellipse2D.Double(0, 40, 250, 70);
				// java.awt.geom.Ellipse2D cloudPuff2 = new
				// java.awt.geom.Ellipse2D.Double(25, 0, 100, 90);
				// java.awt.geom.Ellipse2D cloudPuff3 = new
				// java.awt.geom.Ellipse2D.Double(95, 10, 80, 60);
				// java.awt.geom.Ellipse2D cloudPuff4 = new
				// java.awt.geom.Ellipse2D.Double(145, 30, 80, 60);
				// java.awt.geom.Area cloud = new
				// java.awt.geom.Area(cloudPuff1);
				// cloud.add(new java.awt.geom.Area(cloudPuff2));
				// cloud.add(new java.awt.geom.Area(cloudPuff3));
				// cloud.add(new java.awt.geom.Area(cloudPuff4));
				// java.awt.geom.AffineTransform t = new
				// java.awt.geom.AffineTransform();
				// t.translate(this.getWidth() - 350, 10);
				// t.scale(0.4, 0.4);
				// cloud.transform(t);
				// g2.fill(cloud);
			};
		};
		frmBookmaster.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		initBooksPanel();
		initLoansPanel();
		initCustomersPanel();

		tabbedPane.setMnemonicAt(INDEX_OF_BOOKS_TAB, KeyEvent.VK_B);
		tabbedPane.setMnemonicAt(INDEX_OF_LOANS_TAB, KeyEvent.VK_U);
		tabbedPane.setMnemonicAt(INDEX_OF_CUSTOMERS_TAB, KeyEvent.VK_K);

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				switch (tabbedPane.getSelectedIndex()) {
				case INDEX_OF_BOOKS_TAB:
					txtFilterBooks.requestFocusInWindow();
					break;
				case INDEX_OF_LOANS_TAB:
					txtFilterLoans.requestFocusInWindow();
					break;
				case INDEX_OF_CUSTOMERS_TAB:
					txtFilterCustomers.requestFocusInWindow();
					break;
				default:
					assert false; // Execution should never reach this point:
									// Undefined tab!");
				}
			}
		});
	}

	private void initBooksPanel() {
		pnlBooks = new JPanel();
		tabbedPane.addTab("Bücher   ", new ImageIcon("data/icons/book.png"), pnlBooks, "Bücher anzeigen");

		pnlBooks.setLayout(new BorderLayout(0, 0));

		pnlBooks.add(getPnlInventoryStatistics(), BorderLayout.NORTH);

		JPanel pnlBookInventory = new JPanel();
		pnlBooks.add(pnlBookInventory, BorderLayout.CENTER);
		pnlBookInventory.setLayout(new BorderLayout(0, 0));

		pnlBookInventory.add(getPnlBookFilter(), BorderLayout.NORTH);

		scrollTblBooks = new JScrollPane();
		pnlBookInventory.add(scrollTblBooks, BorderLayout.CENTER);

		initTblBooks();
		scrollTblBooks.setViewportView(tblBooks);
	}

	private JPanel getPnlBookFilter() {
		JLabel lblBookTableDescription = new JLabel("Alle Bücher in der Bibliothek sind in der unterstehenden Tabelle ersichtlich");

		txtFilterBooks = new JTextField();
		txtFilterBooks.setToolTipText("Filtern nach Exemplar-IDs, Buchtitel, Regal, Author oder Verlag");
		txtFilterBooks.getDocument().addDocumentListener(new DocumentListenerAdapter() {
			@Override
			public void documentUpdate(DocumentEvent e) {
				filterAndUpdateBooks();
			}
		});
		txtFilterBooks.setColumns(10);

		JLabel lblFilterBooks = new JLabel("Filter:");
		lblFilterBooks.setDisplayedMnemonic('f');
		lblFilterBooks.setLabelFor(txtFilterBooks);
		lblFilterBooks.setToolTipText("Filtern nach Exemplar-IDs, Buchtitel, Regal, Author oder Verlag");

		chckbxAvailibleOnly = new JCheckBox("Nur Verfügbare");
		chckbxAvailibleOnly.setMnemonic('v');
		chckbxAvailibleOnly.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				filterAndUpdateBooks();
			}
		});

		btnShowSelectedBooks = new JButton(actShowSelectedBooks);
		actShowSelectedBooks.setEnabled(false);

		JButton btnAddNewBook = new JButton(actNewBook);

		FormLayout layout = new FormLayout("5dlu, pref, 2dlu, pref:grow, 2dlu, pref, 5dlu, pref, 2dlu, pref, 5dlu",
				"3dlu, pref, 5dlu, pref, 5dlu, pref, 8dlu");
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(ViewUtil.getSeparator("Buch Inventar"), cc.xyw(2, 2, 9));
		panel.add(lblBookTableDescription, cc.xyw(2, 4, 9));
		panel.add(lblFilterBooks, cc.xy(2, 6));
		panel.add(txtFilterBooks, cc.xy(4, 6));
		panel.add(chckbxAvailibleOnly, cc.xy(6, 6));
		panel.add(btnShowSelectedBooks, cc.xy(8, 6));
		panel.add(btnAddNewBook, cc.xy(10, 6));
		return panel;
	}

	private Component getPnlInventoryStatistics() {
		JLabel lblBooksAmount = new JLabel("Anzahl Bücher:");
		lblBooksAmountNum = new JLabel("");
		JLabel lblExemplarAmount = new JLabel("Anzahl Exemplare:");
		lblExemplarAmountNum = new JLabel("");

		FormLayout layout = new FormLayout("5dlu, pref, 2dlu, pref, 5dlu, pref, 2dlu, pref:grow, 5dlu", "3dlu, pref, 5dlu, pref, 5dlu");
		JPanel pnlInventoryStatistics = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		pnlInventoryStatistics.add(ViewUtil.getSeparator("Inventar Statistik"), cc.xyw(2, 2, 7));
		pnlInventoryStatistics.add(lblBooksAmount, cc.xy(2, 4));
		pnlInventoryStatistics.add(lblBooksAmountNum, cc.xy(4, 4));
		pnlInventoryStatistics.add(lblExemplarAmount, cc.xy(6, 4));
		pnlInventoryStatistics.add(lblExemplarAmountNum, cc.xy(8, 4));
		return pnlInventoryStatistics;
	}

	private void initLoansPanel() {
		pnlLoans = new JPanel();
		tabbedPane.addTab("Ausleihen   ", new ImageIcon("data/icons/loan.png"), pnlLoans, "Ausleihen anzeigen");
		pnlLoans.setLayout(new BorderLayout(0, 0));

		pnlLoans.add(getPnlLoanStatistics(), BorderLayout.NORTH);

		JPanel pnlLoan = new JPanel();
		pnlLoans.add(pnlLoan, BorderLayout.CENTER);
		pnlLoan.setLayout(new BorderLayout(0, 0));

		// JPanel panel_3 = new JPanel();
		// panel_3.setBorder(new TitledBorder(null, "Erfasste Ausleihen",
		// TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		// pnlLoan.add(panel_3, BorderLayout.NORTH);
		pnlLoan.add(getPnlLoanFilter(), BorderLayout.NORTH);

		scrollTblLoans = new JScrollPane();
		pnlLoan.add(scrollTblLoans, BorderLayout.CENTER);

		initTblLoans();
		scrollTblLoans.setViewportView(tblLoans);
	}

	private Component getPnlLoanFilter() {
		JLabel lblAlleAusleighen = new JLabel(
				"Alle zurzeit ausgeliehenen Exemplare für alle Kunden sind in der untenstehenden Tabelle ersichtlich");

		JLabel lblFilterLoans = new JLabel("Filter:");
		lblFilterLoans.setDisplayedMnemonic('f');
		lblFilterLoans.setToolTipText("Filtern nach Status, Exemplar-ID, Buchtitel, Ausgeliehen Bis oder Ausgeliehen An");

		txtFilterLoans = new JTextField();
		txtFilterLoans.setToolTipText("Filtern nach Status, Exemplar-ID, Buchtitel, Ausgeliehen Bis oder Ausgeliehen An");
		lblFilterLoans.setLabelFor(txtFilterLoans);
		txtFilterLoans.getDocument().addDocumentListener(new DocumentListenerAdapter() {
			@Override
			public void documentUpdate(DocumentEvent e) {
				filterAndUpdateLoans();
			}
		});
		txtFilterLoans.setColumns(10);

		chckbxOverduesOnly = new JCheckBox(actOverduesOnly);

		btnReturnSelectedLoans = new JButton(actReturnSelectedLoans);
		actReturnSelectedLoans.setEnabled(false);

		btnShowSelectedLoans = new JButton(actShowSelectedLoans);
		actShowSelectedLoans.setEnabled(false);

		JButton btnNewLoan = new JButton(actNewLoan);

		FormLayout layout = new FormLayout("5dlu, pref, 2dlu, pref:grow, 2dlu, pref, 5dlu, pref, 2dlu, pref, 2dlu, pref, 5dlu",
				"3dlu, pref, 5dlu, pref, 5dlu, pref, 8dlu");
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(ViewUtil.getSeparator("Aktuelle Ausleihen"), cc.xyw(2, 2, 11));
		panel.add(lblAlleAusleighen, cc.xyw(2, 4, 9));
		panel.add(lblFilterLoans, cc.xy(2, 6));
		panel.add(txtFilterLoans, cc.xy(4, 6));
		panel.add(chckbxOverduesOnly, cc.xy(6, 6));
		panel.add(btnReturnSelectedLoans, cc.xy(8, 6));
		panel.add(btnShowSelectedLoans, cc.xy(10, 6));
		panel.add(btnNewLoan, cc.xy(12, 6));
		return panel;
	}

	private Component getPnlLoanStatistics() {
		JLabel lblLoansAmount = new JLabel("Anzahl Ausleihen:");
		lblLoansAmountNum = new JLabel("0");
		JLabel lblCurrentlyLoaned = new JLabel("Aktuell Ausgeliehen:");
		lblCurrentlyLoanedNum = new JLabel("0");
		JLabel lblOverdueAmount = new JLabel("Überfällige Ausgeliehen:");
		lblOverdueAmountNum = new JLabel("0");

		FormLayout layout = new FormLayout("5dlu, pref, 2dlu, pref, 5dlu, pref, 2dlu, pref, 5dlu, pref, 2dlu, pref:grow, 5dlu",
				"3dlu, pref, 5dlu, pref, 5dlu");
		JPanel pnlLoanStatistics = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		pnlLoanStatistics.add(ViewUtil.getSeparator("Ausleihe Statistik"), cc.xyw(2, 2, 11));
		pnlLoanStatistics.add(lblLoansAmount, cc.xy(2, 4));
		pnlLoanStatistics.add(lblLoansAmountNum, cc.xy(4, 4));
		pnlLoanStatistics.add(lblCurrentlyLoaned, cc.xy(6, 4));
		pnlLoanStatistics.add(lblCurrentlyLoanedNum, cc.xy(8, 4));
		pnlLoanStatistics.add(lblOverdueAmount, cc.xy(10, 4));
		pnlLoanStatistics.add(lblOverdueAmountNum, cc.xy(12, 4));
		return pnlLoanStatistics;
	}

	private void initCustomersPanel() {
		pnlCustomers = new JPanel();
		tabbedPane.addTab("Kunden   ", new ImageIcon("data/icons/customer.png"), pnlCustomers, "Kunden anzeigen");
		pnlCustomers.setLayout(new BorderLayout(0, 0));

		pnlCustomers.add(getPnlCustomersStatistics(), BorderLayout.NORTH);

		JPanel pnlCustomerLoan = new JPanel();
		pnlCustomers.add(pnlCustomerLoan, BorderLayout.CENTER);
		pnlCustomerLoan.setLayout(new BorderLayout(0, 0));

		pnlCustomerLoan.add(getPnlCustomerFilter(), BorderLayout.NORTH);

		scrollTblCustomers = new JScrollPane();
		pnlCustomerLoan.add(scrollTblCustomers, BorderLayout.CENTER);

		initTblCustomers();
		tblCustomers.addMouseListener(new DoubleClickMouseAdapter() {
			@Override
			public void leftDoubleClick(MouseEvent mouseEvent) {
				actLoanForSelectedCustomer.actionPerformed(null);
			}
		});
		scrollTblCustomers.setViewportView(tblCustomers);
	}

	private Component getPnlCustomerFilter() {
		JLabel lblAlleAusleighen = new JLabel("Alle Kunden sind in der untenstehenden Tabelle ersichtlich");

		JLabel lblFilterCustomers = new JLabel("Filter:");
		lblFilterCustomers.setDisplayedMnemonic('f');
		lblFilterCustomers.setToolTipText("Filtern nach Vorname, Nachname, Strasse, Stadt oder PLZ");

		txtFilterCustomers = new JTextField();
		lblFilterCustomers.setLabelFor(txtFilterCustomers);
		txtFilterCustomers.getDocument().addDocumentListener(new DocumentListenerAdapter() {
			@Override
			public void documentUpdate(DocumentEvent e) {
				filterAndUpdateCustomers();
			}
		});
		txtFilterCustomers.setColumns(10);

		btnShowSelectedCustomers = new JButton(actShowSelectedCustomers);
		actShowSelectedCustomers.setEnabled(false);

		btnLoanForSelectedCustomer = new JButton(actLoanForSelectedCustomer);
		actLoanForSelectedCustomer.setEnabled(false);

		JButton btnNewClient = new JButton(actNewCustomer);

		FormLayout layout = new FormLayout("5dlu, pref, 2dlu, pref:grow, 5dlu, pref, 2dlu, pref, 2dlu, pref, 5dlu",
				"3dlu, pref, 5dlu, pref, 5dlu, pref, 8dlu");
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(ViewUtil.getSeparator("Erfasste Kunden"), cc.xyw(2, 2, 9));
		panel.add(lblAlleAusleighen, cc.xyw(2, 4, 7));
		panel.add(lblFilterCustomers, cc.xy(2, 6));
		panel.add(txtFilterCustomers, cc.xy(4, 6));
		panel.add(btnLoanForSelectedCustomer, cc.xy(6, 6));
		panel.add(btnShowSelectedCustomers, cc.xy(8, 6));
		panel.add(btnNewClient, cc.xy(10, 6));
		return panel;
	}

	private Component getPnlCustomersStatistics() {
		JLabel lblCustomersAmount = new JLabel("Anzahl Kunden:");
		lblCustomersAmountNum = new JLabel("0");

		FormLayout layout = new FormLayout("5dlu, pref, 2dlu, pref:grow, 5dlu", "3dlu, pref, 5dlu, pref, 5dlu");
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(ViewUtil.getSeparator("Kunden Statistik"), cc.xyw(2, 2, 3));
		panel.add(lblCustomersAmount, cc.xy(2, 4));
		panel.add(lblCustomersAmountNum, cc.xy(4, 4));
		return panel;
	}

	protected List<Book> getSelectedBooks() {
		List<Book> lst = new ArrayList<Book>();
		for (int row : tblBooks.getSelectedRows()) {
			lst.add(getBookOfRow(row));
		}
		return lst;
	}

	protected List<Customer> getSelectedCustomers() {
		List<Customer> lst = new ArrayList<Customer>();
		for (int row : tblCustomers.getSelectedRows()) {
			lst.add(getCustomerOfRow(row));
		}
		return lst;
	}

	protected List<Loan> getSelectedLoans() {
		List<Loan> lst = new ArrayList<Loan>();
		for (int row : tblLoans.getSelectedRows()) {
			lst.add(getLoanOfRow(row));
		}
		return lst;
	}

	protected void filterAndUpdateBooks() {
		tblBooksModel.updateObjects(library.filterBooks(txtFilterBooks.getText(), chckbxAvailibleOnly.isSelected()));
		scrollTblBooks.updateUI();
	}

	protected void filterAndUpdateLoans() {
		tblLoansModel.updateObjects(library.filterLoans(txtFilterLoans.getText(), chckbxOverduesOnly.isSelected()));
		scrollTblLoans.updateUI();
	}

	protected void filterAndUpdateCustomers() {
		tblCustomersModel.updateObjects(library.filterCustomers(txtFilterCustomers.getText()));
		scrollTblCustomers.updateUI();
	}

	protected void createOrShowBookDetailFrame(Book b) {
		uimanager.openBookDetailWindow(b);
	}

	protected void createOrShowCustomerDetailFrame(Customer c) {
		uimanager.openCustomerEditWindow(c);
	}

	protected void createOrShowLoanDetailFrame(Loan l) {
		uimanager.openLoanWindow(l == null ? null : l.getCustomer());
	}

	private void initTblBooks() {
		tblBooks = new JTable() {
			private static final long serialVersionUID = -6660470510160948438L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
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
				Book b = (Book) getModel().getValueAt(convertRowIndexToModel(row), -1);
				return library.getAvailibleCopiesOfBook(b).size() == 0 ? Color.ORANGE : null;
			};
		};

		tblBooks.setColumnSelectionAllowed(false);
		tblBooks.setRowSelectionAllowed(true);
		tblBooksModel = new BookMasterTableModelBook(library);
		tblBooks.setModel(tblBooksModel);
		tblBooks.getColumn("" + BookMasterTableModelBook.ColumnName.STATUS).setMaxWidth(90);
		tblBooks.getColumn("" + BookMasterTableModelBook.ColumnName.STATUS).setMinWidth(90);
		tblBooks.getColumn("" + BookMasterTableModelBook.ColumnName.STATUS).setResizable(false);
		tblBooks.getColumn("" + BookMasterTableModelBook.ColumnName.SHELF).setMaxWidth(40);
		tblBooks.getColumn("" + BookMasterTableModelBook.ColumnName.SHELF).setMinWidth(40);
		tblBooks.getColumn("" + BookMasterTableModelBook.ColumnName.SHELF).setResizable(false);

		TableRowSorter<BookMasterTableModelBook> rowSorter = new TableRowSorter<BookMasterTableModelBook>(tblBooksModel);
		rowSorter.setComparator(0, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				if (s1.equals("nicht verfügbar")) {
					return 1;
				}
				if (s2.equals("nicht verfügbar")) {
					return -1;
				}
				if (s1.startsWith("ab ") && s2.startsWith("ab ")) {
					s1 = s1.substring(3, 13);
					s2 = s2.substring(3, 13);
					try {
						Date d1 = DateFormat.getDateInstance().parse(s1);
						Date d2 = DateFormat.getDateInstance().parse(s2);
						return d1.compareTo(d2);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if (s1.startsWith("ab ") || s2.startsWith("ab ")) {
					return s1.startsWith("ab ") ? 1 : -1;
				} else {
					Integer i1 = Integer.parseInt(s1.split(" ")[0]);
					Integer i2 = Integer.parseInt(s2.split(" ")[0]);
					return i1.compareTo(i2);
				}
				assert (false);
				return 0;
			}
		});
		tblBooks.setRowSorter(rowSorter);
		tblBooks.getRowSorter().toggleSortOrder(tblBooksModel.getDefaultSortedColumn());

		tblBooks.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				actShowSelectedBooks.setEnabled(tblBooks.getSelectedRowCount() > 0);
			}
		});

		tblBooks.addMouseListener(new DoubleClickMouseAdapter() {
			@Override
			public void leftDoubleClick(MouseEvent mouseEvent) {
				Book b = getSelectedBook();
				if (b != null) {
					createOrShowBookDetailFrame(b);
				}
			}
		});

		TableColumn shelfColumn = tblBooks.getColumn("" + BookMasterTableModelBook.ColumnName.SHELF);
		final JComboBox comboBox = new JComboBox(Shelf.values());
		shelfColumn.setCellEditor(new ComboBoxCellEditor(comboBox) {
			private static final long serialVersionUID = -3380337751981718537L;

			@Override
			public boolean stopCellEditing() {
				Book b = getSelectedBook();
				if (b == null || comboBox == null)
					return true;
				comboBox.getSelectedItem();
				b.setShelf((Shelf) comboBox.getSelectedItem());
				return super.stopCellEditing();
			}

		});
	}

	private void initTblLoans() {
		tblLoans = new JTable() {
			private static final long serialVersionUID = -2284571437513151450L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
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
		tblLoansModel = new BookMasterTableModelLoan(library);
		tblLoans.setModel(tblLoansModel);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.STATUS).setMaxWidth(50);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.STATUS).setMinWidth(50);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.STATUS).setResizable(false);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.ID).setMaxWidth(80);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.ID).setMinWidth(80);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.ID).setResizable(false);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.TITLE).setPreferredWidth(800);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.LOAN_TO).setMinWidth(200);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.LOAN_TO).setMaxWidth(700);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.LOAN_TO).setPreferredWidth(100);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.LOAN_UNTIL).setMaxWidth(95);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.LOAN_UNTIL).setMinWidth(95);
		tblLoans.getColumn("" + BookMasterTableModelLoan.ColumnName.LOAN_UNTIL).setResizable(false);

		TableRowSorter<TableModel> rowSorter = new TableRowSorter<TableModel>(tblLoansModel);
		rowSorter.setComparator(1, new Comparator<Long>() {
			@Override
			public int compare(Long l1, Long l2) {
				return l1.compareTo(l2);

			}
		});
		rowSorter.setComparator(3, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				try {
					Date d1 = DateFormat.getDateInstance().parse(s1);
					Date d2 = DateFormat.getDateInstance().parse(s2);
					return d1.compareTo(d2);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return s1.compareTo(s2);

			}
		});
		tblLoans.setRowSorter(rowSorter);

		tblLoans.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				actShowSelectedLoans.setEnabled(tblLoans.getSelectedRowCount() > 0);
				actReturnSelectedLoans.setEnabled(tblLoans.getSelectedRowCount() > 0);
			}
		});
		tblLoans.getRowSorter().toggleSortOrder(tblLoansModel.getDefaultSortedColumn());

		tblLoans.addMouseListener(new DoubleClickMouseAdapter() {
			@Override
			public void leftDoubleClick(MouseEvent mouseEvent) {
				Loan l = getSelectedLoan();
				if (l != null) {
					createOrShowLoanDetailFrame(l);
				}
			}
		});
	}

	private void initTblCustomers() {
		tblCustomers = new JTable();
		tblCustomers.setColumnSelectionAllowed(false);
		tblCustomers.setRowSelectionAllowed(true);
		tblCustomersModel = new BookMasterTableModelCustomer(library);
		tblCustomers.setModel(tblCustomersModel);
		tblCustomers.setAutoCreateRowSorter(true);

		tblCustomers.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				actShowSelectedCustomers.setEnabled(tblCustomers.getSelectedRowCount() > 0);
				actLoanForSelectedCustomer.setEnabled(tblCustomers.getSelectedRowCount() > 0);
			}
		});
		tblCustomers.getRowSorter().toggleSortOrder(tblCustomersModel.getDefaultSortedColumn());
	}

	protected Book getSelectedBook() {
		if (tblBooks == null) {
			return null;
		}
		int row = tblBooks.getSelectedRow();
		if (row != -1) {
			return getBookOfRow(row);
		}
		return null;
	}

	protected Customer getSelectedCustomer() {
		int row = tblCustomers.getSelectedRow();
		if (row != -1) {
			return getCustomerOfRow(row);
		}
		return null;
	}

	protected Loan getSelectedLoan() {
		int row = tblLoans.getSelectedRow();
		if (row != -1) {
			return getLoanOfRow(row);
		}
		return null;
	}

	protected Book getBookOfRow(int row) {
		row = tblBooks.convertRowIndexToModel(row);
		tblBooks.getModel().getValueAt(row, -1);
		return (Book) tblBooks.getModel().getValueAt(row, -1);
	}

	protected Customer getCustomerOfRow(int row) {
		row = tblCustomers.convertRowIndexToModel(row);
		return (Customer) tblCustomers.getModel().getValueAt(row, -1);
	}

	protected Loan getLoanOfRow(int row) {
		row = tblLoans.convertRowIndexToModel(row);
		return (Loan) tblLoans.getModel().getValueAt(row, -1);
	}

	@Override
	public void update(Observable observable, Object o) {
		// System.out.println(observable);
		// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
		if (observable instanceof Library) {
			// update books
			filterAndUpdateBooks();
			updateBooksStatistics();
			// update loans
			filterAndUpdateLoans();
			updateLoansStatistics();
			// update customers
			filterAndUpdateCustomers();
			updateCustomersStatistics();
		} else {
			assert false; // Execution should never reach this point: Unexpected
							// observed object: observable.getClass();
		}
	}

	private void updateBooksStatistics() {
		lblBooksAmountNum.setText("" + library.getBooks().size());
		lblExemplarAmountNum.setText("" + library.getCopies().size());
	}

	private void updateLoansStatistics() {
		lblLoansAmountNum.setText("" + library.getLoans().size());
		lblCurrentlyLoanedNum.setText("" + library.getCurrentLoans().size());
		lblOverdueAmountNum.setText("" + library.getCurrentLoans(true).size());
	}

	private void updateCustomersStatistics() {
		lblCustomersAmountNum.setText("" + library.getCustomers().size());
		// lblCurrentlyCustomeredNum.setText("" +
		// library.getCurrentCustomers().size());
		// lblOverdueAmountNum.setText("" +
		// library.getCurrentCustomers(true).size());
	}

	private class SwingAction extends AbstractAction {
		private static final long serialVersionUID = -7602741039227249620L;

		public SwingAction() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_N);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK));
			putValue(NAME, "Neuer Buchtitel Erfassen...");
			putValue(SHORT_DESCRIPTION, "Erfasst einen neuen Buchtitel");
		}

		public void actionPerformed(ActionEvent e) {
			uimanager.openBookNewFrame();
		}
	}

	private class ActNewLoan extends AbstractAction {
		private static final long serialVersionUID = 2238469619665846547L;

		public ActNewLoan() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_N);
			putValue(NAME, "Neue Ausleihe Erfassen...");
			putValue(SHORT_DESCRIPTION, "Erfasst eine neue Ausleihe");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
		}

		public void actionPerformed(ActionEvent e) {
			createOrShowLoanDetailFrame(null);
		}
	}

	private class ActNewCustomer extends AbstractAction {
		private static final long serialVersionUID = 6502846542854627207L;

		public ActNewCustomer() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_N);
			putValue(NAME, "Neuer Kunde Erfassen...");
			putValue(SHORT_DESCRIPTION, "Erfasst einen neuen Kunden");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK));
		}

		public void actionPerformed(ActionEvent e) {
			uimanager.openCustomerNewWindow();
		}
	}

	private class ActShowSelectedBooks extends AbstractAction {
		private static final long serialVersionUID = -7977864894081464478L;

		public ActShowSelectedBooks() {
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
			putValue(MNEMONIC_KEY, KeyEvent.VK_A);
			putValue(NAME, "Selektierte Bücher Anzeigen...");
			putValue(SHORT_DESCRIPTION, "Zeigt die selektierten Bücher an");
		}

		public void actionPerformed(ActionEvent e) {
			List<Book> books = getSelectedBooks();
			for (Book b : books) {
				createOrShowBookDetailFrame(b);
			}
		}
	}

	private class ActOverduesOnly extends AbstractAction {
		private static final long serialVersionUID = 7824114454977199514L;

		public ActOverduesOnly() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_U);
			putValue(NAME, "Nur Überfällige");
			putValue(SHORT_DESCRIPTION, "Filtert die Tabelle so, dass nur überfällige Ausleihen angezeigt werden.");
		}

		public void actionPerformed(ActionEvent e) {
			filterAndUpdateLoans();
		}
	}

	private class ActShowSelectedLoans extends AbstractAction {
		/**
		 * 
		 */
		private static final long serialVersionUID = -1143944733937267268L;

		public ActShowSelectedLoans() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_A);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK));
			putValue(NAME, "Selektierte Anzeigen...");
			putValue(SHORT_DESCRIPTION, "Zeigt Details zu den selektierten Ausleihen an");
		}

		public void actionPerformed(ActionEvent e) {
			List<Loan> loans = getSelectedLoans();
			for (Loan l : loans) {
				createOrShowLoanDetailFrame(l);
			}
		}
	}

	private class ActReturnSelectedLoans extends AbstractAction {
		private static final long serialVersionUID = -4887278482096384896L;

		public ActReturnSelectedLoans() {
			putValue(NAME, "Selektierte Zurückgeben");
			putValue(MNEMONIC_KEY, KeyEvent.VK_R);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
			putValue(SHORT_DESCRIPTION, "Gibt die selektieren Ausleihen zurück");
		}

		public void actionPerformed(ActionEvent e) {
			List<Loan> loans = getSelectedLoans();
			if (JOptionPane.showConfirmDialog(frmBookmaster, "Sind Sie sicher, dass Sie " + loans.size() + " Ausleihe"
					+ (loans.size() > 1 ? "n" : "") + " zurückgeben wollen?", "Selektierte Ausleihen zurückgeben",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
				for (Loan l : loans) {
					l.returnCopy();
				}
				uimanager.openLoansReportFrame(loans);
			}
		}
	}

	private class ActShowSelectedCustomers extends AbstractAction {
		private static final long serialVersionUID = 5151723828817846179L;

		public ActShowSelectedCustomers() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_A);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
			putValue(NAME, "Selektierte Bearbeiten...");
			putValue(SHORT_DESCRIPTION, "Bearbeitet die ausgewählten Kunden");
		}

		public void actionPerformed(ActionEvent e) {
			List<Customer> customers = getSelectedCustomers();
			for (Customer b : customers) {
				createOrShowCustomerDetailFrame(b);
			}
		}
	}

	private class ActLoanForSelectedCustomer extends AbstractAction {
		private static final long serialVersionUID = -4233519605516483378L;

		public ActLoanForSelectedCustomer() {
			putValue(NAME, "Ausleihe Detail Für Kunden Anzeigen...");
			putValue(MNEMONIC_KEY, KeyEvent.VK_E);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
			putValue(SHORT_DESCRIPTION, "Zeigt Details zu den Ausleihen des selekierten Kunden");
		}

		public void actionPerformed(ActionEvent e) {
			Customer customer = getSelectedCustomer();
			if (customer != null)
				uimanager.openLoanWindow(customer);
		}
	}
}
