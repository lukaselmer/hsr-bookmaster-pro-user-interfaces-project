package view;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import domain.Library;
import domain.Loan;

import application.LibraryApp;
import org.jdesktop.beansbinding.BeanProperty;

import java.util.ArrayList;
import java.util.List;
import domain.Book;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;
import domain.Shelf;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BookMaster {

	private JFrame frmBookmaster;
	private JTable tblBooks;
	private JTextField txtSearch;
	private Library library;
	private BookMasterTableModel tblBooksModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookMaster window = new BookMaster(LibraryApp.inst());
				} catch (Exception e) {
					e.printStackTrace();
				}
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

		JLabel lblBooksAmountNum = new JLabel("777");

		JLabel lblExemplarAmount = new JLabel("Anzahl Exemplare:");

		JLabel lblExemplarAmountNum = new JLabel("777");
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
				tblBooksModel.updateBooks(library.searchBooksByTitle(txtSearch.getText()));
				tblBooks.repaint();
			}
		});
		txtSearch.setColumns(10);

		JLabel lblSearch = new JLabel("Suche:");

		JCheckBox chckbxAvailibleOnly = new JCheckBox("Nur Verfügbare");

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

		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, BorderLayout.CENTER);

		tblBooks = new JTable();
		initTblBooks();
		tblBooks.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2) {
					Book b = getBookOfRow(tblBooks.getSelectedRow());
					createBookDetailFrame(b);
				}
			}
		});
		scrollPane_1.setViewportView(tblBooks);

		JPanel pnlLoans = new JPanel();
		tabbedPane.addTab("Ausleihen", null, pnlLoans, null);
	}

	protected BookDetail createBookDetailFrame(Book b) {
		return new BookDetail(library, b);
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

	protected List<Book> getSelectedBooks() {
		List<Book> lst = new ArrayList<Book>();
		for (int row : tblBooks.getSelectedRows()) {
			lst.add(getBookOfRow(row));
		}
		return lst;
	}

	protected Book getBookOfRow(int row) {
		return (Book) tblBooks.getModel().getValueAt(row, -1);
	}
}
