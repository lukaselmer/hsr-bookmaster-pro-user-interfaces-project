package view.book;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

import validators.BookValidator;
import validators.FormValidator;
import view.BookMasterActions;
import view.BookMasterUiManager;
import view.ViewUtil;

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
	protected JLabel lblName;
	protected JComboBox cmbShelf;
	protected JButton btnCancel;
	protected JLabel lblAuthor;
	protected JLabel lblPublisher;
	protected JLabel lblShelf;
	protected Book savedObject;
	@SuppressWarnings("unused")
	private BookMasterUiManager uimanager;
	private JMenuBar menuBar;
	private final Action actClose = new BookMasterActions.ActClose() {
		private static final long serialVersionUID = -389975679735011338L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			beforeDispose();
			frmBookForm.dispose();
		}
	};
	private final Action actCancel = new BookMasterActions.ActCancel() {
		private static final long serialVersionUID = -389975679735011338L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			beforeDispose();
			frmBookForm.dispose();
		}
	};
	private final Action actSave = new ActSave();
	private final Action actEnterPressed = new BookMasterActions.ActEnterPressed() {
		private static final long serialVersionUID = -8465336848163118754L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (actSave.isEnabled()) {
				actSave.actionPerformed(arg0);
			}
		}
	};
	private JMenu mnFile;
	private JMenuItem mntSave;
	private JMenuItem mntClose;

	protected abstract String getWindowTitle();

	protected abstract void saveBook(Book c);

	protected abstract String getSaveButtonTitle();

	protected void beforeDispose() {
	}

	public JFrame getFrame() {
		return frmBookForm;
	}

	/**
	 * The abstract book form
	 * 
	 * @param uimanager
	 */
	public BookForm(BookMasterUiManager uimanager) {
		this.uimanager = uimanager;
		this.library = uimanager.getLibrary();
		try {
			initialize();
		} catch (ParseException e) {
			e.printStackTrace();
			beforeDispose();
			frmBookForm.dispose();
		}
		frmBookForm.setLocationByPlatform(true);
		frmBookForm.setVisible(true);
	}

	public boolean isVisible() {
		return frmBookForm.isVisible();
	}

	public void toFront() {
		frmBookForm.setState(Frame.NORMAL);
		frmBookForm.toFront();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws ParseException
	 */
	private void initialize() throws ParseException {
		frmBookForm = new JFrame();
		ViewUtil.setIconImages(frmBookForm);
		frmBookForm.setTitle(getWindowTitle());
		frmBookForm.setBounds(100, 100, 600, 242);
		frmBookForm.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		frmBookForm.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				actClose.actionPerformed(null);
			}
		});
		frmBookForm.setMinimumSize(new Dimension(500, 242));

		initMenu();
		initComponents();

		FormLayout layout = new FormLayout("5dlu, pref, 5dlu, pref:grow, 5dlu",
				"5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu");
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
		formValidator = new FormValidator<Book>(frmBookForm, fields, new BookValidator(), btnSave, actSave) {
			@Override
			public Book createObject() {
				return new Book(txtName.getText(), txtAuthor.getText(), txtPublisher.getText(), (Shelf) cmbShelf.getSelectedItem());
			}
		};
	}

	private void initMenu() {
		menuBar = new JMenuBar();
		frmBookForm.setJMenuBar(menuBar);
		mnFile = new JMenu("Datei");
		menuBar.add(mnFile);
		mntSave = new JMenuItem(actSave);
		mntClose = new JMenuItem(actClose);
		mnFile.add(mntSave);
		mnFile.add(mntClose);
		menuBar.add(ViewUtil.getHelpMenu(frmBookForm));
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
		txtName.setAction(actEnterPressed);
		ValidationComponentUtils.setMandatory(txtName, true);

		lblName = new JLabel("Titel:");
		lblName.setDisplayedMnemonic('v');
		lblName.setLabelFor(txtName);

		// Author
		txtAuthor = new JTextField();
		txtAuthor.setColumns(10);
		txtAuthor.setName("Buch.Autor");
		txtAuthor.setAction(actEnterPressed);
		ValidationComponentUtils.setMandatory(txtAuthor, true);

		lblAuthor = new JLabel("Autor:");
		lblAuthor.setDisplayedMnemonic('n');
		lblAuthor.setLabelFor(txtAuthor);

		// Publisher
		txtPublisher = new JTextField();
		txtPublisher.setColumns(10);
		txtPublisher.setName("Buch.Verlag");
		txtPublisher.setAction(actEnterPressed);
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
		btnSave = new JButton(actSave);
		actSave.setEnabled(false);

		// Cancel button
		btnCancel = new JButton(actCancel);
	}

	public Book getSavedObject() {
		return savedObject;
	}

	public void addWindowListener(WindowAdapter windowAdapter) {
		frmBookForm.addWindowListener(windowAdapter);
	}

	private class ActSave extends AbstractAction {
		private static final long serialVersionUID = 6075552530373572839L;

		public ActSave() {
			putValue(NAME, getSaveButtonTitle());
			putValue(MNEMONIC_KEY, KeyEvent.VK_S);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
			putValue(SHORT_DESCRIPTION, getSaveButtonTitle());
			putValue(SMALL_ICON, new ImageIcon("data/icons/save.png"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (!isEnabled())
				return;
			savedObject = formValidator.validateForm(null);
			if (savedObject == null)
				assert false; // Execution should never reach this point
			saveBook(savedObject);
			beforeDispose();
			frmBookForm.dispose();
		}
	}
}
