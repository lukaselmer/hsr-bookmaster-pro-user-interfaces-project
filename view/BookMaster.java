package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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

import application.LibraryApp;
import domain.Book;
import domain.Library;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import view.book_master.BookMasterTableModelBook;

public class BookMaster implements Observer {

	private JFrame frmBookmaster;
	private JTable tblBooks;
	private JTextField txtSearchBooks;
	private Library library;
	private BookMasterTableModelBook tblBooksModel;
	private List<BookDetail> bookDetailFrames = new ArrayList<BookDetail>();
	private JCheckBox chckbxAvailibleOnly;
	private JScrollPane scrollTblBooks;
	private JLabel lblBooksAmountNum;
	private JLabel lblExemplarAmountNum;
	private JTextField txtSearchLoans;
	private JTable tblLoans;
	private JPanel pnlBooks;
	private JPanel pnlLoans;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
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
		setBookAndCopiesAmount();
		frmBookmaster.setLocationByPlatform(true);
		frmBookmaster.setVisible(true);
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

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmBookmaster.getContentPane().add(tabbedPane, BorderLayout.CENTER);

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
		txtSearchBooks.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchAndUpdateBooks();
			}
		});
		txtSearchBooks.setColumns(10);

		JLabel lblSearchBooks = new JLabel("Suche:");

		chckbxAvailibleOnly = new JCheckBox("Nur Verfügbare");
		chckbxAvailibleOnly.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				searchAndUpdateBooks();
			}
		});

		JButton btnShowSelected = new JButton("Selektierte Anzeigen");
		btnShowSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Book> books = getSelectedBooks();
				for (Book b : books) {
					createBookDetailFrame(b);
				}
			}
		});

		JButton btnAddNewBook = new JButton("Neuer Buchtitel Erfassen");
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
														.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnShowSelected)
														.addGap(12).addComponent(btnAddNewBook))).addGap(6)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(
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
														.addComponent(txtSearchBooks, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)).addComponent(lblSearchBooks)
										.addComponent(chckbxAvailibleOnly).addComponent(btnShowSelected).addComponent(btnAddNewBook))));
		panel_1.setLayout(gl_panel_1);

		scrollTblBooks = new JScrollPane();
		panel.add(scrollTblBooks, BorderLayout.CENTER);

		tblBooks = new JTable();
		initTblBooks();
		tblBooks.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					Book b = getSelectedBook();
					if (b != null) {
						createBookDetailFrame(b);
					}
				}
			}
		});
		scrollTblBooks.setViewportView(tblBooks);

		pnlLoans = new JPanel();
		tabbedPane.addTab("Ausleihen", null, pnlLoans, null);
		pnlLoans.setLayout(new BorderLayout(0, 0));

		JPanel pnlLoanStatistics = new JPanel();
		pnlLoanStatistics.setBorder(new TitledBorder(null, "Ausleihe Statistik", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0,
				0, 0)));
		pnlLoans.add(pnlLoanStatistics, BorderLayout.NORTH);

		JLabel lblLoansAmount = new JLabel("Anzahl Ausleihen:");

		JLabel lblLoansAmountNum = new JLabel("0");

		JLabel lblCurrentlyLoaned = new JLabel("Aktuell Ausgeliehen:");

		JLabel lblCurrentlyLoanedNum = new JLabel("0");

		JLabel lblOverdueAmount = new JLabel("Überfällige Ausgeliehen:");

		JLabel lblOverdueAmountNum = new JLabel("0");
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

		txtSearchLoans = new JTextField();
		txtSearchLoans.setColumns(10);

		JCheckBox chckbxOverduesOnly = new JCheckBox("Nur Überfällige");

		JButton button = new JButton("Selektierte Anzeigen");

		JButton btnNeueAusleiheErfassen = new JButton("Neue Ausleihe Erfassen");

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
														.addPreferredGap(ComponentPlacement.UNRELATED).addComponent(button).addGap(12)
														.addComponent(btnNeueAusleiheErfassen).addGap(6))
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
														.addComponent(chckbxOverduesOnly).addComponent(button)
														.addComponent(btnNeueAusleiheErfassen)))));
		panel_3.setLayout(gl_panel_3);

		JScrollPane scrollTblLoans = new JScrollPane();
		panel_2.add(scrollTblLoans, BorderLayout.CENTER);

		tblLoans = new JTable();
		scrollTblLoans.setViewportView(tblLoans);
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

	protected void createBookDetailFrame(Book b) {
		boolean bookDetailOpen = false;
		for (BookDetail bd : bookDetailFrames) {
			if (bd.getBook().equals(b)) {
				bookDetailOpen = true;
				bd.getFrame().toFront();
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
	}

	protected Book getSelectedBook() {
		int row = tblBooks.getSelectedRow();
		if (row != -1) {
			return getBookOfRow(row);
		}
		return null;
	}

	protected Book getBookOfRow(int row) {
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
			searchAndUpdateBooks();
			setBookAndCopiesAmount();
		} else {
			throw new RuntimeException("Unexpected observed object!");
		}
	}

	private void setBookAndCopiesAmount() {
		lblBooksAmountNum.setText("" + library.getBooks().size());
		lblExemplarAmountNum.setText("" + library.getCopies().size());
	}
}
