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

public class BookMaster implements Observer {

	private JFrame frmBookmaster;
	private JTable tblBooks;
	private JTextField txtSearch;
	private Library library;
	private BookMasterTableModel tblBooksModel;
	private List<BookDetail> bookDetailFrames = new ArrayList<BookDetail>();
	private JCheckBox chckbxAvailibleOnly;
	private JScrollPane scrollTblBooks;
	private JLabel lblBooksAmountNum;
	private JLabel lblExemplarAmountNum;

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
		frmBookmaster.setBounds(100, 100, 650, 477);
		frmBookmaster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBookmaster.getContentPane().setLayout(new BorderLayout(0, 0));
		frmBookmaster.setMinimumSize(new Dimension(650, 400));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmBookmaster.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel pnlBooks = new JPanel();
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
		panel_1.setBorder(new TitledBorder(null, "Buch Inventar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_1, BorderLayout.NORTH);

		JLabel lblBookTableDescription = new JLabel("Alle Bücher in der Bibliothek sind in der  unterstehenden Tabelle");

		txtSearch = new JTextField();
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				searchAndUpdateBooks();
			}
		});
		txtSearch.setColumns(10);

		JLabel lblSearch = new JLabel("Suche:");

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

		JButton btnAddNewBook = new JButton("Neues Buch Hinzufügen");
		btnAddNewBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < 200; ++i) {
					library.createAndAddBook("bububu");
				}
				// for (Book b : getSelectedBooks()) {
				// library.createAndAddBook("bububu");
				// }
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
												gl_panel_1.createSequentialGroup().addComponent(lblSearch).addGap(12)
														.addComponent(txtSearch, GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
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
														.addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)).addComponent(lblSearch)
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

		JPanel pnlLoans = new JPanel();
		tabbedPane.addTab("Ausleihen", null, pnlLoans, null);
	}

	protected List<Book> getSelectedBooks() {
		List<Book> lst = new ArrayList<Book>();
		for (int row : tblBooks.getSelectedRows()) {
			lst.add(getBookOfRow(row));
		}
		return lst;
	}

	protected void searchAndUpdateBooks() {
		tblBooksModel.updateBooks(library.searchBooks(txtSearch.getText(), chckbxAvailibleOnly.isSelected()));
		tblBooks.updateUI();
		scrollTblBooks.updateUI();
	}

	protected BookDetail createBookDetailFrame(Book b) {
		BookDetail bd = new BookDetail(library, b);
		bookDetailFrames.add(bd);
		return bd;
	}

	private void initTblBooks() {
		tblBooks.setColumnSelectionAllowed(false);
		tblBooks.setRowSelectionAllowed(true);
		tblBooksModel = new BookMasterTableModel(library);
		tblBooks.setModel(tblBooksModel);
		tblBooks.getColumn("Verfügbar").setMaxWidth(90);
		tblBooks.getColumn("Verfügbar").setMinWidth(90);
		tblBooks.getColumn("Verfügbar").setResizable(false);
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
	public void update(Observable arg0, Object arg1) {
		searchAndUpdateBooks();
		setBookAndCopiesAmount();
	}

	private void setBookAndCopiesAmount() {
		lblBooksAmountNum.setText("" + library.getBooks().size());
		lblExemplarAmountNum.setText("" + library.getCopies().size());
	}
}
