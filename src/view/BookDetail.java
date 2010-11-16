package view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.TextArea;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.RowSorter;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;

import sun.text.normalizer.UProperty;
import view.book.BookEdit;
import view.book_detail.BookDetailTableModel;
import view.book_master.SubFrame;
import application.LibraryApp;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.CellConstraints.Alignment;
import com.jgoodies.forms.layout.FormLayout;

import domain.Book;
import domain.Copy;
import domain.Library;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.border.MatteBorder;
import java.awt.Color;

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
					new BookDetail(l, l.getBooks().get(r.nextInt(l.getBooks().size())));
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
	public BookDetail(Library l, Book book) {
		this.library = l;
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
		initBookPanel();
		initCopiesPanel();
	}

	private void initBookPanel() {
		FormLayout bookInformationLayout = new FormLayout("5dlu, pref, 5dlu, pref:grow, 5dlu", "3dlu, pref, 10dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu");
		pnlBookInformation = new JPanel(bookInformationLayout);
		frmBookDetailView.getContentPane().add(pnlBookInformation, BorderLayout.NORTH);
		
		pnlBookInformation.add(ViewUtil.getSeparator("Buch Information"), cc.xyw(2, 2, 3));

		lblTitel = new JLabel("Titel:");
		pnlBookInformation.add(lblTitel, cc.xy(2, 4));

		lblAuthor = new JLabel("Author:");
		pnlBookInformation.add(lblAuthor, cc.xy(2, 6));

		lblPublisher = new JLabel("Verlag:");
		pnlBookInformation.add(lblPublisher, cc.xy(2, 8));

		lblShelf = new JLabel("Regal:");
		pnlBookInformation.add(lblShelf, cc.xy(2, 10));

		txtTitle = new JTextField(book.getName());
		txtTitle.setEditable(false);
		pnlBookInformation.add(txtTitle, cc.xy(4, 4));

		txtAuthor = new JTextField(book.getAuthor());
		txtAuthor.setEditable(false);
		pnlBookInformation.add(txtAuthor, cc.xy(4, 6));

		txtPublisher = new JTextField(book.getPublisher());
		txtPublisher.setEditable(false);
		pnlBookInformation.add(txtPublisher, cc.xy(4, 8));

		txtShelf = new JTextField(book.getShelf().toString());
		txtShelf.setEditable(false);
		pnlBookInformation.add(txtShelf, cc.xy(4, 10));
		
		btnEditBook = new JButton("Buch bearbeiten");
		btnEditBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (editBookFrame != null && editBookFrame.isValid()){
					editBookFrame.toFront();
				} else {
					editBookFrame = new BookEdit(library, book);
				}
			}
		});
		pnlBookInformation.add(btnEditBook, cc.xy(4, 12, "right, bottom"));
		
	}
	
	private void initCopiesPanel() {
		JPanel pnlCopies = new JPanel();
		frmBookDetailView.getContentPane().add(pnlCopies, BorderLayout.CENTER);
		pnlCopies.setLayout(new BorderLayout(0, 0));
		
		FormLayout copyInformationLayout = new FormLayout("5dlu, pref, 5dlu, pref, pref:grow, pref, 5dlu, pref, 5dlu", "5dlu, pref, 5dlu, pref, 5dlu");
		JPanel pnlCopyInformation = new JPanel(copyInformationLayout);
		pnlCopies.add(pnlCopyInformation, BorderLayout.NORTH);
		
		pnlCopyInformation.add(ViewUtil.getSeparator("Exemplar"), cc.xyw(2, 2, 7));
		
		JLabel lblNumberOfCopies = new JLabel("Anzahl Exemplare:");
		pnlCopyInformation.add(lblNumberOfCopies, cc.xy(2, 4));
		
		lblNumber = new JLabel("" + library.getCopiesOfBook(book).size());
		pnlCopyInformation.add(lblNumber, cc.xy(4, 4));
		
		final JButton btnRemoveSelected = new JButton("Ausgewählte Entfernen");
		btnRemoveSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				List<Copy> copies = getSelectedCopies();
				for (Copy c : copies){
					library.removeCopy(c);
				}
				updateBookInformation();
			}
		});
		btnRemoveSelected.setEnabled(false);
		pnlCopyInformation.add(btnRemoveSelected, cc.xy(6, 4));
		
		JButton btnAddCopy = new JButton("Exemplar Hinzufügen");
		btnAddCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				library.createAndAddCopy(book);
				lblNumber.setText("" + library.getCopiesOfBook(book).size());
				bookTableModel.updateCopies(library.getCopiesOfBook(book));
			}
		});
		pnlCopyInformation.add(btnAddCopy, cc.xy(8, 4));
		
		JScrollPane scrCopies = new JScrollPane();
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
				if (s1.startsWith("Ausgeliehen bis ") && s2.startsWith("Ausgeliehen bis ")){
					s1 = s1.substring(16, 26);
					s2 = s2.substring(16, 26);
					try {
						Date d1 = DateFormat.getDateInstance().parse(s1);
						Date d2 = DateFormat.getDateInstance().parse(s2);
						return d1.compareTo(d2);
					} catch (ParseException e) {
						e.printStackTrace();
						}
				} else if (s1.startsWith("Ausgeliehen bis ") || s2.startsWith("Ausgeliehen bis ")){
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
				btnRemoveSelected.setEnabled(tblCopies.getSelectedRowCount() > 0);
			}
		});
	}
	
	protected void updateBookInformation() {
		lblNumber.setText("" + library.getCopiesOfBook(book).size());
		bookTableModel.updateCopies(library.getCopiesOfBook(book));
	}

	protected List<Copy> getSelectedCopies(){
		List<Copy> list = new ArrayList<Copy>();
		for (int row : tblCopies.getSelectedRows()){
			list.add(getCopyOfRow(row));
		}
		return list;
	}
	
	protected Copy getCopyOfRow(int row){
//		row = tblCopies.convertRowIndexToModel(row);
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
}
