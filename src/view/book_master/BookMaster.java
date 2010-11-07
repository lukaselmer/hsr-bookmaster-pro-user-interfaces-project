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
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import view.BookDetail;
import view.book.BookEdit;
import view.book.BookNew;
import view.customer.CustomerEdit;
import view.customer.CustomerNew;
import application.LibraryApp;
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

		JPanel pnlInventoryStatistics = new JPanel();
		pnlInventoryStatistics.setBorder(new TitledBorder(null, "InventarStatistik", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		pnlBooks.add(pnlInventoryStatistics, BorderLayout.NORTH);

		JLabel lblBooksAmount = new JLabel("Anzahl Bücher:");

		lblBooksAmountNum = new JLabel("");

		JLabel lblExemplarAmount = new JLabel("Anzahl Exemplare:");

		lblExemplarAmountNum = new JLabel("");
		GroupLayout gl_pnlInventoryStatistics = new GroupLayout(pnlInventoryStatistics);
		gl_pnlInventoryStatistics.setHorizontalGroup(gl_pnlInventoryStatistics.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlInventoryStatistics.createSequentialGroup().addContainerGap().addComponent(lblBooksAmount)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblBooksAmountNum).addGap(21)
						.addComponent(lblExemplarAmount).addPreferredGap(ComponentPlacement.RELATED).addComponent(lblExemplarAmountNum)
						.addGap(178)));
		gl_pnlInventoryStatistics.setVerticalGroup(gl_pnlInventoryStatistics.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlInventoryStatistics
						.createSequentialGroup()
						.addGap(5)
						.addGroup(
								gl_pnlInventoryStatistics.createParallelGroup(Alignment.BASELINE).addComponent(lblBooksAmountNum)
										.addComponent(lblExemplarAmountNum).addComponent(lblExemplarAmount).addComponent(lblBooksAmount))));
		pnlInventoryStatistics.setLayout(gl_pnlInventoryStatistics);

		JPanel pnlBookInventory = new JPanel();
		pnlBooks.add(pnlBookInventory, BorderLayout.CENTER);
		pnlBookInventory.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Buch Inventar", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlBookInventory.add(panel_1, BorderLayout.NORTH);

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
				new BookEdit(library, library.getBooks().get((new Random()).nextInt(library.getBooks().size())));
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
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_1
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel_1
										.createParallelGroup(Alignment.LEADING)
										.addComponent(lblBookTableDescription)
										.addGroup(
												gl_panel_1.createSequentialGroup().addComponent(lblFilterBooks).addGap(12)
														.addComponent(txtFilterBooks, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(chckbxAvailibleOnly)
														.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnShowSelectedBooks)
														.addGap(12).addComponent(btnAddNewBook))).addGap(6)));
		gl_panel_1
				.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(
						gl_panel_1
								.createSequentialGroup()
								.addComponent(lblBookTableDescription)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(
										gl_panel_1
												.createParallelGroup(Alignment.BASELINE, false)
												.addGroup(
														gl_panel_1
																.createSequentialGroup()
																.addGap(1)
																.addComponent(txtFilterBooks, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addComponent(lblFilterBooks).addComponent(chckbxAvailibleOnly)
												.addComponent(btnShowSelectedBooks).addComponent(btnAddNewBook))));
		panel_1.setLayout(gl_panel_1);

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

	private void initLoansPanel() {
		pnlLoans = new JPanel();
		tabbedPane.addTab("Ausleihen", null, pnlLoans, null);
		pnlLoans.setLayout(new BorderLayout(0, 0));

		JPanel pnlLoanStatistics = new JPanel();
		pnlLoanStatistics.setBorder(new TitledBorder(null, "Ausleihe Statistik", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0,
				0, 0)));
		pnlLoans.add(pnlLoanStatistics, BorderLayout.NORTH);

		JLabel lblLoansAmount = new JLabel("Anzahl Ausleihen:");

		lblLoansAmountNum = new JLabel("0");

		JLabel lblCurrentlyLoaned = new JLabel("Aktuell Ausgeliehen:");

		lblCurrentlyLoanedNum = new JLabel("0");

		JLabel lblOverdueAmount = new JLabel("Überfällige Ausgeliehen:");

		lblOverdueAmountNum = new JLabel("0");
		GroupLayout gl_pnlLoanStatistics = new GroupLayout(pnlLoanStatistics);
		gl_pnlLoanStatistics.setHorizontalGroup(gl_pnlLoanStatistics.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlLoanStatistics.createSequentialGroup().addGap(12).addComponent(lblLoansAmount).addGap(7)
						.addComponent(lblLoansAmountNum).addGap(21).addComponent(lblCurrentlyLoaned).addGap(7)
						.addComponent(lblCurrentlyLoanedNum).addGap(18)
						.addComponent(lblOverdueAmount, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE).addGap(7)
						.addComponent(lblOverdueAmountNum)));
		gl_pnlLoanStatistics.setVerticalGroup(gl_pnlLoanStatistics.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlLoanStatistics
						.createSequentialGroup()
						.addGap(5)
						.addGroup(
								gl_pnlLoanStatistics.createParallelGroup(Alignment.LEADING).addComponent(lblLoansAmount)
										.addComponent(lblLoansAmountNum).addComponent(lblCurrentlyLoaned)
										.addComponent(lblCurrentlyLoanedNum).addComponent(lblOverdueAmount)
										.addComponent(lblOverdueAmountNum))));
		pnlLoanStatistics.setLayout(gl_pnlLoanStatistics);

		JPanel pnlLoan = new JPanel();
		pnlLoans.add(pnlLoan, BorderLayout.CENTER);
		pnlLoan.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Erfasste Ausleihen", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlLoan.add(panel_3, BorderLayout.NORTH);

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

		JButton btnNeueAusleiheErfassen = new JButton("Neue Ausleihe Erfassen");
		btnNeueAusleiheErfassen.setMnemonic('n');

		JLabel lblAlleAusleighen = new JLabel("Alle Ausleihen für jeden Kunden sind in der untenstehenden Tabelle ersichtlich");
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_3
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel_3
										.createParallelGroup(Alignment.LEADING)
										.addGroup(
												gl_panel_3.createSequentialGroup().addComponent(lblFilterLoans).addGap(12)
														.addComponent(txtFilterLoans, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
														.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(chckbxOverduesOnly)
														.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnShowSelectedLoans)
														.addGap(12).addComponent(btnNeueAusleiheErfassen).addGap(6))
										.addGroup(
												gl_panel_3.createSequentialGroup().addComponent(lblAlleAusleighen)
														.addContainerGap(491, Short.MAX_VALUE)))));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_3
						.createSequentialGroup()
						.addComponent(lblAlleAusleighen)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel_3
										.createParallelGroup(Alignment.LEADING, false)
										.addGroup(
												gl_panel_3
														.createSequentialGroup()
														.addGap(1)
														.addComponent(txtFilterLoans, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGroup(
												gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(lblFilterLoans)
														.addComponent(chckbxOverduesOnly).addComponent(btnShowSelectedLoans)
														.addComponent(btnNeueAusleiheErfassen)))));
		panel_3.setLayout(gl_panel_3);

		scrollTblLoans = new JScrollPane();
		pnlLoan.add(scrollTblLoans, BorderLayout.CENTER);

		initTblLoans();
		scrollTblLoans.setViewportView(tblLoans);
	}

	private void initCustomersPanel() {
		pnlCustomers = new JPanel();
		tabbedPane.addTab("Kunden", null, pnlCustomers, null);
		pnlCustomers.setLayout(new BorderLayout(0, 0));

		JPanel pnlCustomersStatistics = new JPanel();
		pnlCustomersStatistics.setBorder(new TitledBorder(null, "Kunden Statistik", TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		pnlCustomers.add(pnlCustomersStatistics, BorderLayout.NORTH);

		JLabel lblCustomersAmount = new JLabel("Anzahl Kunden:");

		lblCustomersAmountNum = new JLabel("0");
		GroupLayout gl_pnlCustomersStatistics = new GroupLayout(pnlCustomersStatistics);
		gl_pnlCustomersStatistics.setHorizontalGroup(gl_pnlCustomersStatistics.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlCustomersStatistics.createSequentialGroup().addGap(12).addComponent(lblCustomersAmount).addGap(7)
						.addComponent(lblCustomersAmountNum).addGap(305)));
		gl_pnlCustomersStatistics.setVerticalGroup(gl_pnlCustomersStatistics.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlCustomersStatistics
						.createSequentialGroup()
						.addGap(5)
						.addGroup(
								gl_pnlCustomersStatistics.createParallelGroup(Alignment.LEADING).addComponent(lblCustomersAmount)
										.addComponent(lblCustomersAmountNum))));
		pnlCustomersStatistics.setLayout(gl_pnlCustomersStatistics);

		JPanel pnlCustomerLoan = new JPanel();
		pnlCustomers.add(pnlCustomerLoan, BorderLayout.CENTER);
		pnlCustomerLoan.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Erfasste Ausleihen", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlCustomerLoan.add(panel_3, BorderLayout.NORTH);

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

		JLabel lblAlleAusleighen = new JLabel("Alle Kunden sind in der untenstehenden Tabelle ersichtlich");
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_3
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel_3
										.createParallelGroup(Alignment.LEADING)
										.addGroup(
												gl_panel_3.createSequentialGroup().addComponent(lblFilterCustomers).addGap(12)
														.addComponent(txtFilterCustomers, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
														.addGap(12).addComponent(btnShowSelectedCustomers).addGap(12)
														.addComponent(btnNewClient).addGap(6))
										.addGroup(
												gl_panel_3.createSequentialGroup().addComponent(lblAlleAusleighen)
														.addContainerGap(491, Short.MAX_VALUE)))));
		gl_panel_3.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_3
						.createSequentialGroup()
						.addComponent(lblAlleAusleighen)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_panel_3
										.createParallelGroup(Alignment.LEADING, false)
										.addGroup(
												gl_panel_3
														.createSequentialGroup()
														.addGap(1)
														.addComponent(txtFilterCustomers, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addGroup(
												gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(lblFilterCustomers)
														.addComponent(btnShowSelectedCustomers).addComponent(btnNewClient)))));
		panel_3.setLayout(gl_panel_3);

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
		tblBooks.getRowSorter().toggleSortOrder(2);
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
		tblLoans.getRowSorter().toggleSortOrder(2);
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
		tblCustomers.getRowSorter().toggleSortOrder(1);
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
