package view.book_master;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import view.BookDetail;
import view.ViewUtil;
import view.book.BookNew;
import view.customer.CustomerEdit;
import view.customer.CustomerNew;
import application.LibraryApp;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import domain.Book;
import domain.Customer;
import domain.Library;
import domain.Loan;

public class BookMaster implements Observer {

	private static final int INDEX_OF_BOOKS_TAB = 0, INDEX_OF_LOANS_TAB = 1, INDEX_OF_CUSTOMERS_TAB = 2;
	private List<SubFrame<Book>> bookDetailFrames = new ArrayList<SubFrame<Book>>();
	private List<SubFrame<Customer>> customerDetailFrames = new ArrayList<SubFrame<Customer>>();
	private JButton btnShowSelectedBooks;
	private JButton btnShowSelectedCustomers;
	private JButton btnShowSelectedLoans;
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
	protected BookNew newBookFrame;
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

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new BookMaster(LibraryApp.inst());
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @param library
	 */
	public BookMaster(Library library) {
		this.library = library;
		initialize();
		library.addObserver(this);
		updateBooksStatistics();
		updateLoansStatistics();
		updateCustomersStatistics();
		// updateTableObjects();
		frmBookmaster.setLocationByPlatform(true);
		frmBookmaster.setVisible(true);
		// TEMP!!! DEBUG!!!
		// tabbedPane.setSelectedIndex(1);
	}

	// private void updateTableObjects() {
	// tblBooksModel.updateObjects(library.filterBooks(txtFilterBooks.getText(),
	// chckbxAvailibleOnly.isSelected()));
	// tblLoansModel.updateObjects(library.filterLoans(txtFilterLoans.getText(),
	// chckbxOverduesOnly.isSelected()));
	// tblCustomersModel.updateObjects(library.filterCustomers(txtFilterCustomers.getText()));
	// }

	public BookMaster inst() {
		return this;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBookmaster = new JFrame();
		frmBookmaster.setTitle("BookMaster");
		frmBookmaster.setBounds(100, 100, 950, 600);
		frmBookmaster.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmBookmaster.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (JOptionPane.showConfirmDialog(frmBookmaster, "Sind Sie sicher? Alle Unterfenster werden geschlossen.",
						"BookMasterPro Schliessen", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
					frmBookmaster.setVisible(false);
					frmBookmaster.dispose();
					System.exit(0);
				}
			}
		});
		frmBookmaster.getContentPane().setLayout(new BorderLayout(0, 0));
		frmBookmaster.setMinimumSize(new Dimension(650, 400));

		tabbedPane = new JTabbedPane(SwingConstants.TOP);
		frmBookmaster.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		initBooksPanel();
		initLoansPanel();
		initCustomersPanel();

		tabbedPane.setMnemonicAt(INDEX_OF_BOOKS_TAB, KeyEvent.VK_B);
		tabbedPane.setMnemonicAt(INDEX_OF_LOANS_TAB, KeyEvent.VK_A);
		tabbedPane.setMnemonicAt(INDEX_OF_CUSTOMERS_TAB, KeyEvent.VK_K);
	}

	private void initBooksPanel() {
		pnlBooks = new JPanel();
		tabbedPane.addTab("Bücher", null, pnlBooks, null);
		pnlBooks.setLayout(new BorderLayout(0, 0));

		pnlBooks.add(getPnlInventoryStatistics(), BorderLayout.NORTH);

		JPanel pnlBookInventory = new JPanel();
		pnlBooks.add(pnlBookInventory, BorderLayout.CENTER);
		pnlBookInventory.setLayout(new BorderLayout(0, 0));

		pnlBookInventory.add(getPnlBookFilter(), BorderLayout.NORTH);

		scrollTblBooks = new JScrollPane();
		pnlBookInventory.add(scrollTblBooks, BorderLayout.CENTER);

		initTblBooks();
		tblBooks.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseEvent.BUTTON1) {
					Book b = getSelectedBook();
					if (b != null) {
						createOrShowBookDetailFrame(b);
					}
				}
			}
		});
		scrollTblBooks.setViewportView(tblBooks);
	}

	private JPanel getPnlBookFilter() {
		JLabel lblBookTableDescription = new JLabel("Alle Bücher in der Bibliothek sind in der  unterstehenden Tabelle ersichtlich");

		txtFilterBooks = new JTextField();
		txtFilterBooks.setToolTipText("Filtern nach Exemplar-IDs, Buchtitel, Regal, Author oder Verlag");
		txtFilterBooks.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
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

		btnShowSelectedBooks = new JButton("Selektierte Anzeigen");
		btnShowSelectedBooks.setEnabled(false);
		btnShowSelectedBooks.setMnemonic('a');
		btnShowSelectedBooks.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Book> books = getSelectedBooks();
				for (Book b : books) {
					createOrShowBookDetailFrame(b);
				}
			}
		});

		JButton btnAddNewBook = new JButton("Neuer Buchtitel Erfassen");
		btnAddNewBook.setMnemonic('n');
		btnAddNewBook.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// new BookEdit(library, library.getBooks().get((new
				// Random()).nextInt(library.getBooks().size())));
				if (newBookFrame != null && newBookFrame.isValid()) {
					newBookFrame.toFront();
				} else {
					newBookFrame = new BookNew(library);
					newBookFrame.getFrame().addWindowListener(new WindowAdapter() {
						@Override
						public void windowClosed(WindowEvent arg0) {
							Book b = newBookFrame.getSavedObject();
							newBookFrame = null;
							if (b != null)
								createOrShowBookDetailFrame(b);
						}
					});
				}
			}
		});

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
		tabbedPane.addTab("Ausleihen", null, pnlLoans, null);
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
		JLabel lblAlleAusleighen = new JLabel("Alle Ausleihen für jeden Kunden sind in der untenstehenden Tabelle ersichtlich");

		JLabel lblFilterLoans = new JLabel("Filter:");
		lblFilterLoans.setDisplayedMnemonic('f');
		lblFilterLoans.setToolTipText("Filtern nach Status, Exemplar-ID, Buchtitel, Ausgeliehen Bis oder Ausgeliehen An");

		txtFilterLoans = new JTextField();
		txtFilterLoans.setToolTipText("Filtern nach Status, Exemplar-ID, Buchtitel, Ausgeliehen Bis oder Ausgeliehen An");
		lblFilterLoans.setLabelFor(txtFilterLoans);
		txtFilterLoans.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterAndUpdateLoans();
			}
		});
		txtFilterLoans.setColumns(10);

		chckbxOverduesOnly = new JCheckBox("Nur Überfällige");
		chckbxOverduesOnly.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				filterAndUpdateLoans();
			}
		});

		btnShowSelectedLoans = new JButton("Selektierte Anzeigen");
		btnShowSelectedLoans.setEnabled(false);
		btnShowSelectedLoans.setMnemonic('a');

		JButton btnNewLoan = new JButton("Neue Ausleihe Erfassen");
		btnNewLoan.setMnemonic('n');

		FormLayout layout = new FormLayout("5dlu, pref, 2dlu, pref:grow, 2dlu, pref, 5dlu, pref, 2dlu, pref, 5dlu",
				"3dlu, pref, 5dlu, pref, 5dlu, pref, 8dlu");
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(ViewUtil.getSeparator("Erfasste Ausleihen"), cc.xyw(2, 2, 9));
		panel.add(lblAlleAusleighen, cc.xyw(2, 4, 9));
		panel.add(lblFilterLoans, cc.xy(2, 6));
		panel.add(txtFilterLoans, cc.xy(4, 6));
		panel.add(chckbxOverduesOnly, cc.xy(6, 6));
		panel.add(btnShowSelectedLoans, cc.xy(8, 6));
		panel.add(btnNewLoan, cc.xy(10, 6));
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
		tabbedPane.addTab("Kunden", null, pnlCustomers, null);
		pnlCustomers.setLayout(new BorderLayout(0, 0));

		pnlCustomers.add(getPnlCustomersStatistics(), BorderLayout.NORTH);

		JPanel pnlCustomerLoan = new JPanel();
		pnlCustomers.add(pnlCustomerLoan, BorderLayout.CENTER);
		pnlCustomerLoan.setLayout(new BorderLayout(0, 0));

		pnlCustomerLoan.add(getPnlCustomerFilter(), BorderLayout.NORTH);

		scrollTblCustomers = new JScrollPane();
		pnlCustomerLoan.add(scrollTblCustomers, BorderLayout.CENTER);

		initTblCustomers();
		tblCustomers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseEvent.BUTTON1) {
					Customer c = getSelectedCustomer();
					if (c != null) {
						createOrShowCustomerDetailFrame(c);
					}
				}
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
		txtFilterCustomers.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterAndUpdateCustomers();
			}
		});
		txtFilterCustomers.setColumns(10);

		btnShowSelectedCustomers = new JButton("Selektierte Anzeigen");
		btnShowSelectedCustomers.setEnabled(false);
		btnShowSelectedCustomers.setMnemonic('a');
		btnShowSelectedCustomers.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				List<Customer> customers = getSelectedCustomers();
				for (Customer b : customers) {
					createOrShowCustomerDetailFrame(b);
				}
			}
		});

		JButton btnNewClient = new JButton("Neuer Kunde Erfassen");
		btnNewClient.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (newCustomerFrame != null && newCustomerFrame.isValid()) {
					newCustomerFrame.toFront();
				} else {
					newCustomerFrame = new CustomerNew(library);
				}
			}
		});
		btnNewClient.setMnemonic('n');

		FormLayout layout = new FormLayout("5dlu, pref, 2dlu, pref:grow, 5dlu, pref, 2dlu, pref, 5dlu",
				"3dlu, pref, 5dlu, pref, 5dlu, pref, 8dlu");
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(ViewUtil.getSeparator("Erfasste Kunden"), cc.xyw(2, 2, 7));
		panel.add(lblAlleAusleighen, cc.xyw(2, 4, 7));
		panel.add(lblFilterCustomers, cc.xy(2, 6));
		panel.add(txtFilterCustomers, cc.xy(4, 6));
		panel.add(btnShowSelectedCustomers, cc.xy(6, 6));
		panel.add(btnNewClient, cc.xy(8, 6));
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

	protected void filterAndUpdateBooks() {
		tblBooksModel.updateObjects(library.filterBooks(txtFilterBooks.getText(), chckbxAvailibleOnly.isSelected()));
		tblBooks.updateUI();
		scrollTblBooks.updateUI();
	}

	protected void filterAndUpdateLoans() {
		tblLoansModel.updateObjects(library.filterLoans(txtFilterLoans.getText(), chckbxOverduesOnly.isSelected()));
		tblLoans.updateUI();
		scrollTblLoans.updateUI();
	}

	protected void filterAndUpdateCustomers() {
		tblCustomersModel.updateObjects(library.filterCustomers(txtFilterCustomers.getText()));
		tblCustomers.updateUI();
		scrollTblCustomers.updateUI();
	}

	protected void createOrShowBookDetailFrame(Book b) {
		createOrShowSubFrame(bookDetailFrames, b, BookDetail.class);
	}

	protected void createOrShowCustomerDetailFrame(Customer c) {
		createOrShowSubFrame(customerDetailFrames, c, CustomerEdit.class);
	}

	protected <T> void createOrShowSubFrame(final List<SubFrame<T>> list, T object, Class<? extends SubFrame<T>> cls) {
		try {
			Constructor<? extends SubFrame<T>> constructor = cls.getConstructor(Library.class, object.getClass());
			boolean detailOpen = false;
			for (SubFrame<T> bd : list) {
				if (bd.getObject().equals(object)) {
					detailOpen = true;
					bd.toFront();
					break;
				}
			}
			if (!detailOpen) {
				final SubFrame<T> bd = constructor.newInstance(library, object);
				list.add(bd);
				bd.getFrame().addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent arg0) {
						list.remove(bd);
					}
				});
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	private void initTblBooks() {
		tblBooks = new JTable() {
			private static final long serialVersionUID = -6660470510160948438L;

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
				System.out.println(":-((");
				return 0;
			}
		});
		tblBooks.setRowSorter(rowSorter);

		tblBooks.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				btnShowSelectedBooks.setEnabled(tblBooks.getSelectedRowCount() > 0);
			}
		});
		tblBooks.getRowSorter().toggleSortOrder(tblBooksModel.getDefaultSortedColumn());
	}

	private void initTblLoans() {
		tblLoans = new JTable() {
			private static final long serialVersionUID = -2284571437513151450L;

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
				btnShowSelectedLoans.setEnabled(tblLoans.getSelectedRowCount() > 0);
			}
		});
		tblLoans.getRowSorter().toggleSortOrder(tblLoansModel.getDefaultSortedColumn());
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
				btnShowSelectedCustomers.setEnabled(tblCustomers.getSelectedRowCount() > 0);
			}
		});
		tblCustomers.getRowSorter().toggleSortOrder(tblCustomersModel.getDefaultSortedColumn());
	}

	protected Book getSelectedBook() {
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

	protected Book getBookOfRow(int row) {
		row = tblBooks.convertRowIndexToModel(row);
		return (Book) tblBooks.getModel().getValueAt(row, -1);
	}

	protected Customer getCustomerOfRow(int row) {
		row = tblCustomers.convertRowIndexToModel(row);
		return (Customer) tblCustomers.getModel().getValueAt(row, -1);
	}

	@Override
	public void update(Observable observable, Object o) {
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
			throw new RuntimeException("Unexpected observed object: " + observable.getClass());
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
}
