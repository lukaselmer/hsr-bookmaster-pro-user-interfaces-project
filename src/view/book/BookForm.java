package view.book;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import validators.BookValidator;
import validators.FormValidator;
import view.ViewUtil;
import application.LibraryApp;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.view.ValidationComponentUtils;

import domain.Book;
import domain.Library;
import domain.Shelf;

public abstract class BookForm {

	protected JFrame frmBookForm;
	protected Library library;
	protected JTextField txtAuthor;
	protected JTextField txtName;
	protected JTextField txtPublisher;
	protected JButton btnSave;
	protected FormValidator<Book> formValidator;
	private JLabel lblName;
	private JComboBox cmbShelf;
	private JButton btnCancel;
	private JLabel lblAuthor;
	private JLabel lblPublisher;
	private JLabel lblShelf;
	protected Book savedObject;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
					new BookNew(LibraryApp.inst());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected abstract String getWindowTitle();

	protected abstract String getSaveButtonTitle();

	protected abstract void saveBook(Book c);

	public JFrame getFrame() {
		return frmBookForm;
	}

	/**
	 * Create the application.
	 * 
	 * @param bookMaster
	 * 
	 * @param library
	 * @wbp.parser.entryPoint
	 */
	public BookForm(Library library) {
		this.library = library;
		try {
			initialize();
		} catch (ParseException e) {
			e.printStackTrace();
			frmBookForm.dispose();
		}
		frmBookForm.setLocationByPlatform(true);
		frmBookForm.setVisible(true);
	}

	public boolean isValid() {
		return frmBookForm.isValid();
	}

	public void toFront() {
		frmBookForm.setState(JFrame.NORMAL);
		frmBookForm.toFront();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws ParseException
	 */
	private void initialize() throws ParseException {
		frmBookForm = new JFrame();
		frmBookForm.setTitle(getWindowTitle());
		frmBookForm.setBounds(100, 100, 450, 215);
		frmBookForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmBookForm.setSize(new Dimension(500, 222));
		frmBookForm.setMinimumSize(new Dimension(350, 222));
		// frmBookForm.setMaximumSize(new Dimension(650, 322));
		// frmBookForm.setResizable(false);

		initComponents();

		FormLayout layout = new FormLayout("5dlu, pref, 5dlu, pref:grow, 5dlu",
				"4dlu, pref, 4dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu");
		layout.setRowGroups(new int[][] { { 2, 4, 6, 8, 10 } });
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(ViewUtil.getSeparator(getWindowTitle()), cc.xyw(2, 2, 3));
		panel.add(lblName, cc.xy(2, 4));
		panel.add(txtName, cc.xy(4, 4));
		panel.add(lblAuthor, cc.xy(2, 6));
		panel.add(txtAuthor, cc.xy(4, 6));
		panel.add(lblPublisher, cc.xy(2, 8));
		panel.add(txtPublisher, cc.xy(4, 8));
		panel.add(lblShelf, cc.xy(2, 10));
		panel.add(cmbShelf, cc.xy(4, 10));
		panel.add(getButtonsPanel(), cc.xyw(2, 12, 3));

		frmBookForm.getContentPane().add(panel, BorderLayout.CENTER);

		JTextField[] fields = { txtName, txtPublisher, txtAuthor };
		formValidator = new FormValidator<Book>(frmBookForm, fields, new BookValidator(), btnSave) {
			@Override
			public Book createObject() {
				return new Book(txtName.getText(), txtAuthor.getText(), txtPublisher.getText(), (Shelf) cmbShelf.getSelectedItem());
			}
		};
	}

	private Component getButtonsPanel() {
		FormLayout layout = new FormLayout("pref:grow, 5dlu, pref:grow", "pref");
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(btnSave, cc.xy(1, 1));
		panel.add(btnCancel, cc.xy(3, 1));
		return panel;
	}

	private void initComponents() {
		// Name
		txtName = new JTextField();
		txtName.setColumns(10);
		txtName.setName("Buch.Buchtitel");
		ValidationComponentUtils.setMandatory(txtName, true);

		lblName = new JLabel("Titel:");
		lblName.setDisplayedMnemonic('v');
		lblName.setLabelFor(txtName);

		// Author
		txtAuthor = new JTextField();
		txtAuthor.setColumns(10);
		txtAuthor.setName("Buch.Autor");
		ValidationComponentUtils.setMandatory(txtAuthor, true);

		lblAuthor = new JLabel("Autor:");
		lblAuthor.setDisplayedMnemonic('n');
		lblAuthor.setLabelFor(txtAuthor);

		// Publisher
		txtPublisher = new JTextField();
		txtPublisher.setColumns(10);
		txtPublisher.setName("Buch.Verlag");
		ValidationComponentUtils.setMandatory(txtPublisher, true);

		lblPublisher = new JLabel("Verlag:");
		lblPublisher.setDisplayedMnemonic('s');
		lblPublisher.setLabelFor(txtPublisher);

		// Shelf cmbBox
		cmbShelf = new JComboBox(Shelf.values());
		cmbShelf.setName("Buch.Regal");

		lblShelf = new JLabel("Regal:");
		lblShelf.setDisplayedMnemonic('p');
		lblShelf.setLabelFor(cmbShelf);

		// Save button
		btnSave = new JButton(getSaveButtonTitle());
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savedObject = formValidator.validateForm(null);
				if (savedObject == null)
					throw new RuntimeException("Bad state");
				saveBook(savedObject);
				frmBookForm.dispose();
			}
		});
		btnSave.setEnabled(false);

		// Cancel button
		btnCancel = new JButton("Abbrechen");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmBookForm.dispose();
			}
		});
	}
	
	public Book getSavedObject(){
		return savedObject;
	}
}
