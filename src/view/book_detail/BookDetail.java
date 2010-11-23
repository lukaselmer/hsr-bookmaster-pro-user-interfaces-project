package view.book_detail;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import view.BookMasterActions;
import view.BookMasterUiManager;
import view.ViewUtil;
import view.book.BookEdit;
import view.book_master.SubFrame;
import application.LibraryApp;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import domain.Book;
import domain.Copy;
import domain.Library;

public class BookDetail implements SubFrame<Book>, Observer {

	private JFrame frmBookDetailView;
	private JTextField txtTitle;
	private JTextField txtAuthor;
	private JTextField txtPublisher;
	private Library library;
	private Book book;
	private JTable tblCopies;
	private CellConstraints cc;
	private JButton btnEditBook;
	protected BookEdit editBookFrame;
	private BookDetailTableModel bookTableModel;
	private JTextField txtShelf;
	private JPanel pnlBookInformation;
	private JLabel lblTitel;
	private JLabel lblAuthor;
	private JLabel lblPublisher;
	private JLabel lblShelf;
	private JLabel lblNumber;
	private JLabel lblNumberOfCopies;
	private JPanel pnlCopies;
	private JPanel pnlCopyInformation;
	private JButton btnRemoveSelected;
	private JButton btnAddCopy;
	private JScrollPane scrCopies;
	private BookMasterUiManager uimanager;
	private JMenuBar menuBar;
	private JMenuItem mnRemoveSelected;
	private final Action actClose = new BookMasterActions.ActClose() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			frmBookDetailView.dispose();
		}
	};
	private final Action actEditBook = new ActEditBook();
	private final Action actRemoveSelected = new ActRemoveSelected();
	private final Action actAddCopy = new ActAddCopy();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Library l = LibraryApp.inst();
				try {
					// Random r = new Random();
					// new BookDetail(new BookMasterUiManager(l),
					// l.getBooks().get(r.nextInt(l.getBooks().size())));
					new BookDetail(new BookMasterUiManager(l), l.getBooks().get(0));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * 
	 * @param book
	 * @param l
	 */
	public BookDetail(BookMasterUiManager uimanager, Book book) {
		this.uimanager = uimanager;
		this.library = uimanager.getLibrary();
		this.book = book;
		initialize();
		frmBookDetailView.setLocationByPlatform(true);
		frmBookDetailView.setVisible(true);
		book.addObserver(this);
	}

	public JFrame getFrame() {
		return frmBookDetailView;
	}

	public Book getBook() {
		return book;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBookDetailView = new JFrame();
		frmBookDetailView.setTitle("Buch Detailansicht");
		frmBookDetailView.setBounds(100, 100, 500, 400);
		frmBookDetailView.setMinimumSize(new Dimension(500, 400));
		frmBookDetailView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmBookDetailView.getContentPane().setLayout(new BorderLayout(0, 0));
		cc = new CellConstraints();
		initMenu();
		initBookPanel();
		initCopiesPanel();
	}

	private void initMenu() {
		menuBar = new JMenuBar();
		frmBookDetailView.setJMenuBar(menuBar);
		JMenu mnFile = new JMenu("Datei");
		JMenu mnEdit = new JMenu("Bearbeiten");
		menuBar.add(mnFile);
		menuBar.add(mnEdit);

		JMenuItem mnClose = new JMenuItem(actClose);
		mnFile.add(mnClose);

		JMenuItem mnEditBook = new JMenuItem(actEditBook);
		mnRemoveSelected = new JMenuItem(actRemoveSelected);
		
		JMenuItem mnAddCopy = new JMenuItem(actAddCopy);

		mnEdit.add(mnEditBook);
		mnEdit.add(mnRemoveSelected);
		mnEdit.add(mnAddCopy);
	}

	private void initBookPanel() {
		FormLayout bookInformationLayout = new FormLayout("5dlu, pref, 5dlu, pref:grow, 5dlu",
				"3dlu, pref, 10dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu");
		pnlBookInformation = new JPanel(bookInformationLayout);
		frmBookDetailView.getContentPane().add(pnlBookInformation, BorderLayout.NORTH);

		initBookPanelParameters();

		pnlBookInformation.add(lblTitel, cc.xy(2, 4));
		pnlBookInformation.add(lblAuthor, cc.xy(2, 6));
		pnlBookInformation.add(lblPublisher, cc.xy(2, 8));
		pnlBookInformation.add(lblShelf, cc.xy(2, 10));
		pnlBookInformation.add(txtTitle, cc.xy(4, 4));
		pnlBookInformation.add(txtAuthor, cc.xy(4, 6));
		pnlBookInformation.add(txtPublisher, cc.xy(4, 8));
		pnlBookInformation.add(txtShelf, cc.xy(4, 10));
		pnlBookInformation.add(btnEditBook, cc.xy(4, 12, "right, bottom"));

	}

	private void initBookPanelParameters() {
		pnlBookInformation.add(ViewUtil.getSeparator("Buch Information"), cc.xyw(2, 2, 3));
		lblTitel = new JLabel("Titel:");
		lblAuthor = new JLabel("Author:");
		lblPublisher = new JLabel("Verlag:");
		lblShelf = new JLabel("Regal:");

		txtTitle = ViewUtil.getTextField(book.getName());
		txtTitle.setCaretPosition(1);
		txtAuthor = ViewUtil.getTextField(book.getAuthor());
		txtPublisher = ViewUtil.getTextField(book.getPublisher());
		txtShelf = ViewUtil.getTextField(book.getShelf().toString());

		btnEditBook = new JButton(actEditBook);
	}

	private void initCopiesPanel() {
		pnlCopies = new JPanel();
		frmBookDetailView.getContentPane().add(pnlCopies, BorderLayout.CENTER);
		pnlCopies.setLayout(new BorderLayout(0, 0));

		FormLayout copyInformationLayout = new FormLayout("5dlu, pref, 5dlu, pref, pref:grow, pref, 5dlu, pref, 5dlu",
				"5dlu, pref, 5dlu, pref, 5dlu");
		pnlCopyInformation = new JPanel(copyInformationLayout);
		pnlCopies.add(pnlCopyInformation, BorderLayout.NORTH);
		initCopiesPanelParameters();

		pnlCopyInformation.add(ViewUtil.getSeparator("Exemplar"), cc.xyw(2, 2, 7));
		pnlCopyInformation.add(lblNumberOfCopies, cc.xy(2, 4));
		pnlCopyInformation.add(lblNumber, cc.xy(4, 4));
		pnlCopyInformation.add(btnRemoveSelected, cc.xy(6, 4));
		pnlCopyInformation.add(btnAddCopy, cc.xy(8, 4));

		initBooksTable();
	}

	private void initBooksTable() {
		scrCopies = new JScrollPane();
		pnlCopies.add(scrCopies, BorderLayout.CENTER);
		bookTableModel = new BookDetailTableModel(library, book);
		tblCopies = new JTable(bookTableModel);
		tblCopies.getColumn("" + BookDetailTableModel.ColumnName.INVENTORY_NUMBER).setMinWidth(80);
		tblCopies.getColumn("" + BookDetailTableModel.ColumnName.INVENTORY_NUMBER).setMaxWidth(80);
		tblCopies.setColumnSelectionAllowed(false);
		tblCopies.setRowSelectionAllowed(true);
		TableRowSorter<BookDetailTableModel> rowSorter = new TableRowSorter<BookDetailTableModel>(bookTableModel);
		rowSorter.setComparator(0, new Comparator<Long>() {

			@Override
			public int compare(Long l1, Long l2) {
				return l1.compareTo(l2);
			}

		});
		rowSorter.setComparator(1, new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {
				if (s1.startsWith("Ausgeliehen bis ") && s2.startsWith("Ausgeliehen bis ")) {
					s1 = s1.substring(16, 26);
					s2 = s2.substring(16, 26);
					try {
						Date d1 = DateFormat.getDateInstance().parse(s1);
						Date d2 = DateFormat.getDateInstance().parse(s2);
						return d1.compareTo(d2);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if (s1.startsWith("Ausgeliehen bis ") || s2.startsWith("Ausgeliehen bis ")) {
					return s1.startsWith("Ausgeliehen bis ") ? 1 : -1;
				}
				return 0;
			}
		});
		tblCopies.setRowSorter(rowSorter);
		scrCopies.setViewportView(tblCopies);

		tblCopies.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				actRemoveSelected.setEnabled(tblCopies.getSelectedRowCount() > 0);
			}
		});
	}

	private void initCopiesPanelParameters() {
		lblNumberOfCopies = new JLabel("Anzahl Exemplare:");
		lblNumber = new JLabel("" + library.getCopiesOfBook(book).size());

		btnRemoveSelected = new JButton(actRemoveSelected);
		actRemoveSelected.setEnabled(false);

		btnAddCopy = new JButton(actAddCopy);
	}

	protected void addNewCopy() {
		library.createAndAddCopy(book);
		lblNumber.setText("" + library.getCopiesOfBook(book).size());
		bookTableModel.updateCopies(library.getCopiesOfBook(book));
	}

	protected void removeSelectedCopies() {
			List<Copy> copies = getSelectedCopies();
			for (Copy c : copies) {
				library.removeCopy(c);
			}
			updateBookInformation();
	}

	protected void updateBookInformation() {
		lblNumber.setText("" + library.getCopiesOfBook(book).size());
		bookTableModel.updateCopies(library.getCopiesOfBook(book));
	}

	protected List<Copy> getSelectedCopies() {
		List<Copy> list = new ArrayList<Copy>();
		for (int row : tblCopies.getSelectedRows()) {
			list.add(getCopyOfRow(row));
		}
		return list;
	}

	protected Copy getCopyOfRow(int row) {
		// row = tblCopies.convertRowIndexToModel(row);
		return (Copy) tblCopies.getValueAt(row, -1);
	}

	public void toFront() {
		getFrame().setState(JFrame.NORMAL);
		getFrame().toFront();
	}

	@Override
	public Book getObject() {
		return getBook();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		txtTitle.setText(book.getName());
		txtAuthor.setText(book.getAuthor());
		txtPublisher.setText(book.getPublisher());
		txtShelf.setText(book.getShelf().toString());
	}

	@Override
	public boolean isValid() {
		return frmBookDetailView.isValid();
	}

	@Override
	public void addWindowListener(WindowAdapter windowAdapter) {
		frmBookDetailView.addWindowListener(windowAdapter);
	}
	
	private class ActEditBook extends AbstractAction{
		private static final long serialVersionUID = 3182075129242820202L;
		
		ActEditBook(){
			putValue(MNEMONIC_KEY, KeyEvent.VK_B);
			putValue(NAME, "Buch Bearbeiten...");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_B, ActionEvent.ALT_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			uimanager.openBookEditWindow(book);
		}
	}
	
	private class ActRemoveSelected extends AbstractAction{
		private static final long serialVersionUID = 4185674812266173535L;

		public ActRemoveSelected() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_E);
			putValue(NAME, "Selektierte Entfernen");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			removeSelectedCopies();
			updateBookInformation();
		}
	}
	
	private class ActAddCopy extends AbstractAction{
		private static final long serialVersionUID = 6762488117389436253L;
		
		public ActAddCopy(){
			putValue(MNEMONIC_KEY, KeyEvent.VK_H);
			putValue(NAME, "Exemplar Hinzufügen");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			addNewCopy();	
		}
	}
}
