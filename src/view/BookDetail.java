package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import sun.swing.DefaultLookup;
import view.customer.SubFrame;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.sun.corba.se.impl.encoding.CodeSetConversion.BTCConverter;

import application.LibraryApp;
import domain.Book;
import domain.Copy;
import domain.Library;
import domain.Loan;
import domain.Shelf;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTable;

public class BookDetail implements SubFrame<Book> {

	private JFrame frmBuchDetailansicht;
	private JTextField txtTitle;
	private JTextField txtAuthor;
	private JTextField txtPublisher;
	private Library library;
	private Book book;
	private JTable tblCopies;

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
		frmBuchDetailansicht.setLocationByPlatform(true);
		frmBuchDetailansicht.setVisible(true);
	}

	public JFrame getFrame() {
		return frmBuchDetailansicht;
	}

	public Book getBook() {
		return book;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBuchDetailansicht = new JFrame();
		frmBuchDetailansicht.setTitle("Buch Detailansicht");
		frmBuchDetailansicht.setBounds(100, 100, 450, 300);
		frmBuchDetailansicht.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmBuchDetailansicht.getContentPane().setLayout(new BorderLayout(0, 0));

		CellConstraints cc = new CellConstraints();
		
		FormLayout bookInformationLayout = new FormLayout("5dlu, pref, 5dlu, pref:grow, 5dlu", "3dlu, pref, 10dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu");
		JPanel pnlBookInformation = new JPanel(bookInformationLayout);
		frmBuchDetailansicht.getContentPane().add(pnlBookInformation, BorderLayout.NORTH);
		
		pnlBookInformation.add(ViewUtil.getSeparator("Buch Information"), cc.xyw(2, 2, 3));

		JLabel lblTitel = new JLabel("Titel:");
		pnlBookInformation.add(lblTitel, cc.xy(2, 4));

		JLabel lblAuthor = new JLabel("Author:");
		pnlBookInformation.add(lblAuthor, cc.xy(2, 6));

		JLabel lblPublisher = new JLabel("Verlag:");
		pnlBookInformation.add(lblPublisher, cc.xy(2, 8));

		JLabel lblShelf = new JLabel("Regal:");
		pnlBookInformation.add(lblShelf, cc.xy(2, 10));

		txtTitle = new JTextField(book.getName());
		txtTitle.setEditable(false);
		txtTitle.setColumns(10);
		pnlBookInformation.add(txtTitle, cc.xy(4, 4));

		txtAuthor = new JTextField(book.getAuthor());
		txtAuthor.setEditable(false);
		txtAuthor.setColumns(10);
		pnlBookInformation.add(txtAuthor, cc.xy(4, 6));

		txtPublisher = new JTextField(book.getPublisher());
		txtPublisher.setEditable(false);
		txtPublisher.setColumns(10);
		pnlBookInformation.add(txtPublisher, cc.xy(4, 8));

		book.getShelf();
		JComboBox cmbShelf = new JComboBox(Shelf.values());
		cmbShelf.setEnabled(false);
		cmbShelf.setSelectedItem(book.getShelf());
		pnlBookInformation.add(cmbShelf, cc.xy(4, 10));
		
		JPanel pnlCopies = new JPanel();
		frmBuchDetailansicht.getContentPane().add(pnlCopies, BorderLayout.CENTER);
		pnlCopies.setLayout(new BorderLayout(0, 0));
		
		FormLayout copyInformationLayout = new FormLayout("5dlu, pref, 5dlu, pref, pref:grow, pref, 5dlu, pref, 5dlu", "5dlu, pref, 5dlu, pref, 5dlu");
		JPanel pnlCopyInformation = new JPanel(copyInformationLayout);
		pnlCopies.add(pnlCopyInformation, BorderLayout.NORTH);
		
		pnlCopyInformation.add(ViewUtil.getSeparator("Exemplar"), cc.xyw(2, 2, 7));
		
		JLabel lblNumberOfCopies = new JLabel("Anzahl Exemplare:");
		pnlCopyInformation.add(lblNumberOfCopies, cc.xy(2, 4));
		
		JLabel lblNumber = new JLabel("" + library.getCopiesOfBook(book).size());
		pnlCopyInformation.add(lblNumber, cc.xy(4, 4));
		
		JButton btnRemoveSelected = new JButton("Ausgewählte Entfernen");
		pnlCopyInformation.add(btnRemoveSelected, cc.xy(6, 4));
		
		JButton btnAddCopy = new JButton("Exemplar Hinzufügen");
		pnlCopyInformation.add(btnAddCopy, cc.xy(8, 4));
		
		JScrollPane scrCopies = new JScrollPane();
		pnlCopies.add(scrCopies, BorderLayout.CENTER);
		
		tblCopies = new JTable();
		scrCopies.setViewportView(tblCopies);
	}

	public void toFront() {
		getFrame().setState(JFrame.NORMAL);
		getFrame().toFront();
	}

	@Override
	public Book getObject() {
		return getBook();
	}
}
