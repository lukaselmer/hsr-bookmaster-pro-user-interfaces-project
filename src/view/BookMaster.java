package view;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import view.book_master.BookMasterTableModelBook;
import view.book_master.BookMasterTableModelCustomer;
import view.book_master.BookMasterTableModelLoan;
import view.customer.CustomerNew;
import application.LibraryApp;
import domain.Book;
import domain.Library;
import domain.Loan;

public class BookMaster implements Observer {

	private static final int INDEX_OF_BOOKS_TAB = 0, INDEX_OF_LOANS_TAB = 1, INDEX_OF_CUSTOMERS_TAB = 2;
	private JFrame frmBookmaster;
	private JTable tblBooks;
	private JTextField txtSearchBooks;
	private Library library;
	private BookMasterTableModelBook tblBooksModel;
	private BookMasterTableModelLoan tblLoansModel;
	private List<BookDetail> bookDetailFrames = new ArrayList<BookDetail>();
	private JCheckBox chckbxAvailibleOnly;
	private JScrollPane scrollTblBooks;
	private JLabel lblBooksAmountNum;
	private JLabel lblExemplarAmountNum;
	private JTextField txtSearchLoans;
	private JTable tblLoans;
	private JPanel pnlBooks;
	private JPanel pnlLoans;
	private JTabbedPane tabbedPane;
	private JCheckBox chckbxOverduesOnly;
	private JScrollPane scrollTblLoans;
	private JLabel lblLoansAmountNum;
	private JLabel lblCurrentlyLoanedNum;
	private JLabel lblOverdueAmountNum;
	private JPanel pnlCustomers;
	private JLabel lblCustomersAmountNum;
	private JTextField txtSearchCustomers;
	private JScrollPane scrollTblCustomers;
	private JTable tblCustomers;
	private BookMasterTableModelCustomer tblCustomersModel;
	protected CustomerNew newCustomerFrame;
	private JButton btnShowSelectedBooks;
	private JButton btnShowSelectedLoans;
	private JButton btnShowSelectedCustomers;

	/**
	 * Launch the application.
	 */
	// public static void main(String[] args) throws Exception {
	// UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
	// EventQueue.invokeLater(new Runnable() {
	// public void run() {
	// new BookMaster(LibraryApp.inst());
	// }
	// });
	// }

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
		frmBookmaster.setLocationByPlatform(true);
		frmBookmaster.setVisible(true);
		// TEMP!!! DEBUG!!!
		// tabbedPane.setSelectedIndex(1);
	}

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
		frmBookmaster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBookmaster.getContentPane().setLayout(new BorderLayout(0, 0));
		frmBookmaster.setMinimumSize(new Dimension(650, 400));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
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

		JPanel panel = new JPanel();
		pnlBooks.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Buch Inventar", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.add(panel_1, BorderLayout.NORTH);

		JLabel lblBookTableDescription = new JLabel("Alle Bücher in der Bibliothek sind in der  unterstehenden Tabelle ersichtlich");

		txtSearchBooks = new JTextField();
		txtSearchBooks.setToolTipText("Suchen nach Exemplar-IDs, Buchtitel, Regal, Author oder Verlag");
		txtSearchBooks.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchAndUpdateBooks();
			}
		});
		txtSearchBooks.setColumns(10);

		JLabel lblSearchBooks = new JLabel("Suche:");
		lblSearchBooks.setDisplayedMnemonic('s');
		lblSearchBooks.setLabelFor(txtSearchBooks);
		lblSearchBooks.setToolTipText("Suchen nach Exemplar-IDs, Buchtitel, Regal, Author oder Verlag");

		chckbxAvailibleOnly = new JCheckBox("Nur Verfügbare");
		chckbxAvailibleOnly.setMnemonic('v');
		chckbxAvailibleOnly.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				searchAndUpdateBooks();
			}
		});

		btnShowSelectedBooks = new JButton("Selektierte Anzeigen");
		btnShowSelectedBooks.setEnabled(false);
		btnShowSelectedBooks.setMnemonic('a');
		btnShowSelectedBooks.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Book> books = getSelectedBooks();
				for (Book b : books) {
					createBookDetailFrame(b);
				}
			}
		});

		JButton btnAddNewBook = new JButton("Neuer Buchtitel Erfassen");
		btnAddNewBook.setMnemonic('n');
		btnAddNewBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// TODO implement this
				for (int i = 0; i < 200; ++i) {
					library.createAndAddBook("bububu");
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
												gl_panel_1.createSequentialGroup().addComponent(lblSearchBooks).addGap(12)
														.addComponent(txtSearchBooks, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
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
																.addComponent(txtSearchBooks, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
												.addComponent(lblSearchBooks).addComponent(chckbxAvailibleOnly)
												.addComponent(btnShowSelectedBooks).addComponent(btnAddNewBook))));
		panel_1.setLayout(gl_panel_1);

		scrollTblBooks = new JScrollPane();
		panel.add(scrollTblBooks, BorderLayout.CENTER);

		initTblBooks();
		tblBooks.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton() == MouseEvent.BUTTON1) {
					Book b = getSelectedBook();
					if (b != null) {
						createBookDetailFrame(b);
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

		JPanel panel_2 = new JPanel();
		pnlLoans.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Erfasste Ausleihen", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.add(panel_3, BorderLayout.NORTH);

		JLabel lblSearchLoans = new JLabel("Suche:");
		lblSearchLoans.setDisplayedMnemonic('s');
		lblSearchLoans.setToolTipText("Suchen nach Status, Exemplar-ID, Buchtitel, Ausgeliehen Bis oder Ausgeliehen An");

		txtSearchLoans = new JTextField();
		txtSearchLoans.setToolTipText("Suchen nach Status, Exemplar-ID, Buchtitel, Ausgeliehen Bis oder Ausgeliehen An");
		lblSearchLoans.setLabelFor(txtSearchLoans);
		txtSearchLoans.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchAndUpdateLoans();
			}
		});
		txtSearchLoans.setColumns(10);

		chckbxOverduesOnly = new JCheckBox("Nur Überfällige");
		chckbxOverduesOnly.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				searchAndUpdateLoans();
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
												gl_panel_3.createSequentialGroup().addComponent(lblSearchLoans).addGap(12)
														.addComponent(txtSearchLoans, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
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
														.addComponent(txtSearchLoans, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addGroup(
												gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(lblSearchLoans)
														.addComponent(chckbxOverduesOnly).addComponent(btnShowSelectedLoans)
														.addComponent(btnNeueAusleiheErfassen)))));
		panel_3.setLayout(gl_panel_3);

		scrollTblLoans = new JScrollPane();
		panel_2.add(scrollTblLoans, BorderLayout.CENTER);

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

		JPanel panel_2 = new JPanel();
		pnlCustomers.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Erfasste Ausleihen", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.add(panel_3, BorderLayout.NORTH);

		JLabel lblSearchCustomers = new JLabel("Suche:");
		lblSearchCustomers.setDisplayedMnemonic('s');
		lblSearchCustomers.setToolTipText("Suchen nach Vorname, Nachname, Strasse, Stadt oder PLZ");

		txtSearchCustomers = new JTextField();
		lblSearchCustomers.setLabelFor(txtSearchCustomers);
		txtSearchCustomers.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchAndUpdateCustomers();
			}
		});
		txtSearchCustomers.setColumns(10);

		btnShowSelectedCustomers = new JButton("Selektierte Anzeigen");
		btnShowSelectedCustomers.setEnabled(false);
		btnShowSelectedCustomers.setMnemonic('a');

		JButton btnNewClient = new JButton("Neuer Kunde Erfassen");
		btnNewClient.addActionListener(new ActionListener() {

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
												gl_panel_3.createSequentialGroup().addComponent(lblSearchCustomers).addGap(12)
														.addComponent(txtSearchCustomers, GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
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
														.addComponent(txtSearchCustomers, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
										.addGroup(
												gl_panel_3.createParallelGroup(Alignment.BASELINE).addComponent(lblSearchCustomers)
														.addComponent(btnShowSelectedCustomers).addComponent(btnNewClient)))));
		panel_3.setLayout(gl_panel_3);

		scrollTblCustomers = new JScrollPane();
		panel_2.add(scrollTblCustomers, BorderLayout.CENTER);

		initTblCustomers();
		scrollTblCustomers.setViewportView(tblCustomers);
	}

	protected List<Book> getSelectedBooks() {
		List<Book> lst = new ArrayList<Book>();
		for (int row : tblBooks.getSelectedRows()) {
			lst.add(getBookOfRow(row));
		}
		return lst;
	}

	protected void searchAndUpdateBooks() {
		tblBooksModel.updateBooks(library.searchBooks(txtSearchBooks.getText(), chckbxAvailibleOnly.isSelected()));
		tblBooks.updateUI();
		scrollTblBooks.updateUI();
	}

	protected void searchAndUpdateLoans() {
		tblLoansModel.updateLoans(library.searchLoans(txtSearchLoans.getText(), chckbxOverduesOnly.isSelected()));
		tblLoans.updateUI();
		scrollTblLoans.updateUI();
	}

	protected void searchAndUpdateCustomers() {
		tblCustomersModel.updateCustomers(library.searchCustomers(txtSearchCustomers.getText()));
		tblCustomers.updateUI();
		scrollTblCustomers.updateUI();
	}

	protected void createBookDetailFrame(Book b) {
		boolean bookDetailOpen = false;
		for (BookDetail bd : bookDetailFrames) {
			if (bd.getBook().equals(b)) {
				bookDetailOpen = true;
				bd.toFront();
				break;
			}
		}
		if (!bookDetailOpen) {
			BookDetail bd = new BookDetail(library, b);
			bookDetailFrames.add(bd);
			bd.addObserver(this);
		}
	}

	private void initTblBooks() {
		tblBooks = new JTable() {
			private static final long serialVersionUID = -6660470510160948438L;

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
						Date d1 = SimpleDateFormat.getDateInstance().parse(s1);
						Date d2 = SimpleDateFormat.getDateInstance().parse(s2);
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
	}

	private void initTblLoans() {
		tblLoans = new JTable() {
			private static final long serialVersionUID = -2284571437513151450L;

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
					Date d1 = SimpleDateFormat.getDateInstance().parse(s1);
					Date d2 = SimpleDateFormat.getDateInstance().parse(s2);
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
	}

	protected Book getSelectedBook() {
		int row = tblBooks.getSelectedRow();
		if (row != -1) {
			return getBookOfRow(row);
		}
		return null;
	}

	protected Book getBookOfRow(int row) {
		row = tblBooks.convertRowIndexToModel(row);
		return (Book) tblBooks.getModel().getValueAt(row, -1);
	}

	@Override
	public void update(Observable observable, Object o) {
		if (observable instanceof BookDetail) {
			BookDetail bd = (BookDetail) observable;
			if (!bd.getFrame().isValid()) {
				bookDetailFrames.remove(bd);
			}
		} else if (observable instanceof Library) {
			// update books
			searchAndUpdateBooks();
			updateBooksStatistics();
			// update loans
			searchAndUpdateLoans();
			updateLoansStatistics();
			// update customers
			searchAndUpdateCustomers();
			updateCustomersStatistics();
			/*
			 * int index = tabbedPane.getSelectedIndex(); switch (index) { case
			 * INDEX_OF_BOOKS_TAB: break; case INDEX_OF_LOANS_TAB: break; case
			 * INDEX_OF_CUSTOMERS_TAB: break; default: throw new
			 * RuntimeException("Invalid Tab ID: " + index); }
			 */
		} else {
			throw new RuntimeException("Unexpected observed object!");
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
