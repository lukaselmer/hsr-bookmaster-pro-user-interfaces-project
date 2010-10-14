package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.AbstractListModel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.UnsupportedLookAndFeelException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import application.LibraryApp;

import domain.Book;
import domain.IllegalLoanOperationException;
import domain.Library;

public class BookDetail {

	private JFrame frmBuchDetailansicht;
	private JTextField txtTitle;
	private JTextField txtAuthor;
	private JTextField txtPublisher;
	private Library library;
	private Book book;

	/**
	 * Launch the application.
	 * 
	 * @throws UnsupportedLookAndFeelException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Library l = LibraryApp.inst();
				try {
					Random r = new Random();
					BookDetail window = new BookDetail(l, l.getBooks().get(r.nextInt(l.getBooks().size())));
					window.frmBuchDetailansicht.setVisible(true);
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
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBuchDetailansicht = new JFrame();
		frmBuchDetailansicht.setTitle("Buch Detailansicht");
		frmBuchDetailansicht.setBounds(100, 100, 450, 300);
		frmBuchDetailansicht.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBuchDetailansicht.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel pnlBookInformation = new JPanel();
		pnlBookInformation.setBorder(new TitledBorder(null, "Buch Informationen:", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(
				0, 0, 0)));
		frmBuchDetailansicht.getContentPane().add(pnlBookInformation, BorderLayout.NORTH);

		JLabel lblTitel = new JLabel("Titel:");

		JLabel lblAuthor = new JLabel("Author:");

		JLabel lblPublisher = new JLabel("Verlag:");

		JLabel lblShelf = new JLabel("Regal:");

		txtTitle = new JTextField(book.getName());
		txtTitle.setEditable(false);
		txtTitle.setColumns(10);

		txtAuthor = new JTextField(book.getAuthor());
		txtAuthor.setEditable(false);
		txtAuthor.setColumns(10);

		txtPublisher = new JTextField(book.getPublisher());
		txtPublisher.setEditable(false);
		txtPublisher.setColumns(10);

		JComboBox cmbShelf = new JComboBox(book.getShelf().values());
		GroupLayout gl_pnlBookInformation = new GroupLayout(pnlBookInformation);
		gl_pnlBookInformation.setHorizontalGroup(gl_pnlBookInformation.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlBookInformation
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_pnlBookInformation
										.createParallelGroup(Alignment.LEADING)
										.addComponent(lblPublisher)
										.addComponent(lblShelf)
										.addGroup(
												gl_pnlBookInformation.createSequentialGroup().addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(lblTitel)).addComponent(lblAuthor))
						.addGap(21)
						.addGroup(
								gl_pnlBookInformation.createParallelGroup(Alignment.LEADING)
										.addComponent(txtTitle, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
										.addComponent(txtAuthor, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
										.addComponent(txtPublisher, GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
										.addComponent(cmbShelf, 0, 339, Short.MAX_VALUE)).addContainerGap()));
		gl_pnlBookInformation.setVerticalGroup(gl_pnlBookInformation.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlBookInformation
						.createSequentialGroup()
						.addGroup(
								gl_pnlBookInformation
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(txtTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(lblTitel))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_pnlBookInformation
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(txtAuthor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(lblAuthor))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_pnlBookInformation
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblPublisher)
										.addComponent(txtPublisher, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(
								gl_pnlBookInformation
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(lblShelf)
										.addComponent(cmbShelf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		pnlBookInformation.setLayout(gl_pnlBookInformation);

		JPanel pnlCopies = new JPanel();
		pnlCopies
				.setBorder(new TitledBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
						TitledBorder.TOP, null, new Color(0, 0, 0)), "Exemplare", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(
						0, 0, 0)));
		frmBuchDetailansicht.getContentPane().add(pnlCopies, BorderLayout.CENTER);

		JLabel lblQuantity = new JLabel("Anzahl: ");

		JButton btnRemoveSelected = new JButton("Ausgew\u00E4hlte Entfernen");

		JButton btnAddCopy = new JButton("Exemplar Hinzuf\u00FCgen");

		JScrollPane scrBooks = new JScrollPane();

		JLabel lblQuantityNumber = new JLabel("" + library.getCopiesOfBook(book).size());
		GroupLayout gl_pnlCopies = new GroupLayout(pnlCopies);
		gl_pnlCopies.setHorizontalGroup(gl_pnlCopies.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlCopies
						.createSequentialGroup()
						.addGroup(
								gl_pnlCopies
										.createParallelGroup(Alignment.LEADING)
										.addGroup(
												gl_pnlCopies.createSequentialGroup().addContainerGap().addComponent(lblQuantity)
														.addPreferredGap(ComponentPlacement.RELATED).addComponent(lblQuantityNumber)
														.addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
														.addComponent(btnRemoveSelected).addPreferredGap(ComponentPlacement.RELATED)
														.addComponent(btnAddCopy))
										.addGroup(
												gl_pnlCopies.createSequentialGroup().addGap(10)
														.addComponent(scrBooks, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_pnlCopies.setVerticalGroup(gl_pnlCopies.createParallelGroup(Alignment.LEADING).addGroup(
				gl_pnlCopies
						.createSequentialGroup()
						.addGroup(
								gl_pnlCopies.createParallelGroup(Alignment.BASELINE).addComponent(btnRemoveSelected)
										.addComponent(btnAddCopy).addComponent(lblQuantityNumber).addComponent(lblQuantity))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(scrBooks, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
						.addGap(1)));

		JList lstBooks = new JList();

		lstBooks.setModel(new AbstractListModel() {
			public int getSize() {
				return library.getCopiesOfBook(book).size();
			}

			public Object getElementAt(int index) {
				return library.getCopiesOfBook(book).get(index);
			}
		});

		scrBooks.setViewportView(lstBooks);
		pnlCopies.setLayout(gl_pnlCopies);
	}
}
