package view.customer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.text.ParseException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;

import org.jdesktop.swingx.JXTitledSeparator;

import validators.CustomerValidator;
import validators.FormValidator;
import view.BookMasterActions;
import view.BookMasterUiManager;
import view.ViewUtil;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.view.ValidationComponentUtils;

import domain.Customer;
import domain.Library;

public abstract class CustomerForm {

	protected JFrame frmCustomerForm;
	protected Library library;
	protected JTextField txtSurname;
	protected JTextField txtName;
	protected JTextField txtStreet;
	protected JTextField txtZip;
	protected JTextField txtCity;
	protected JButton btnSave;
	protected FormValidator<Customer> formValidator;
	@SuppressWarnings("unused")
	private BookMasterUiManager uimanager;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntUndoAndClose;
	private JMenuItem mntClose;
	private final Action actSave = new ActSave();
	private final Action actClose = new BookMasterActions.ActClose() {
		private static final long serialVersionUID = 5235844072589710482L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			frmCustomerForm.dispose();
		}
	};
	private final Action actEnterPressed = new BookMasterActions.ActEnterPressed() {
		private static final long serialVersionUID = 8489606741555435162L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (actSave.isEnabled())
				actSave.actionPerformed(arg0);
		}
	};
	private JLabel lblName;
	private JLabel lblSurname;
	private JLabel lblStreet;
	private JLabel lblZip;
	private JLabel lblCity;
	private JButton btnCancel;

	protected abstract String getWindowTitle();

	protected abstract String getSaveButtonTitle();

	protected abstract void saveCustomer(Customer c);

	/**
	 * The abstract customer form
	 * 
	 * @param uimanager
	 */
	public CustomerForm(BookMasterUiManager uimanager) {
		this.uimanager = uimanager;
		this.library = uimanager.getLibrary();
		try {
			initialize();
		} catch (ParseException e) {
			e.printStackTrace();
			frmCustomerForm.dispose();
		}
		frmCustomerForm.setLocationByPlatform(true);
		frmCustomerForm.setVisible(true);
	}

	private void initMenu() {
		menuBar = new JMenuBar();
		frmCustomerForm.setJMenuBar(menuBar);
		mnFile = new JMenu("Datei");
		menuBar.add(mnFile);
		mntUndoAndClose = new JMenuItem(actSave);
		mnFile.add(mntUndoAndClose);
		mntClose = new JMenuItem(actClose);
		mnFile.add(mntClose);
		menuBar.add(ViewUtil.getHelpMenu(frmCustomerForm));
	}

	public boolean isVisible() {
		return frmCustomerForm.isVisible();
	}

	public void toFront() {
		frmCustomerForm.setState(Frame.NORMAL);
		frmCustomerForm.toFront();
	}

	public void addWindowListener(WindowAdapter windowAdapter) {
		frmCustomerForm.addWindowListener(windowAdapter);
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws ParseException
	 */
	private void initialize() throws ParseException {
		frmCustomerForm = new JFrame();
		frmCustomerForm.setTitle(getWindowTitle());
		frmCustomerForm.setBounds(100, 100, 450, 268);
		frmCustomerForm.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frmCustomerForm.setResizable(false);

		initMenu();
		initComponents();

		FormLayout layout = new FormLayout("5dlu, pref, 5dlu, pref:grow, 5dlu",
				"5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu");
		JPanel panel = new JPanel(layout);
		frmCustomerForm.getContentPane().add(panel, BorderLayout.CENTER);
		CellConstraints cc = new CellConstraints();
		panel.add(ViewUtil.getSeparator(getWindowTitle()), cc.xyw(2, 2, 3));
		panel.add(lblName, cc.xy(2, 4));
		panel.add(txtName, cc.xy(4, 4));
		panel.add(lblSurname, cc.xy(2, 6));
		panel.add(txtSurname, cc.xy(4, 6));
		panel.add(lblStreet, cc.xy(2, 8));
		panel.add(txtStreet, cc.xy(4, 8));
		panel.add(lblZip, cc.xy(2, 10));
		panel.add(txtZip, cc.xy(4, 10));
		panel.add(lblCity, cc.xy(2, 12));
		panel.add(txtCity, cc.xy(4, 12));
		panel.add(getButtonsPanel(), cc.xyw(2, 14, 3));
	}

	private void initComponents() {

		lblName = new JLabel("Vorname:");
		lblName.setDisplayedMnemonic('v');

		lblSurname = new JLabel("Nachname:");
		lblSurname.setDisplayedMnemonic('n');

		lblStreet = new JLabel("Strasse:");
		lblStreet.setDisplayedMnemonic('s');

		lblZip = new JLabel("PLZ:");
		lblZip.setDisplayedMnemonic('p');

		lblCity = new JLabel("Ort:");
		lblCity.setDisplayedMnemonic('o');

		txtName = new JTextField();
		lblName.setLabelFor(txtName);
		txtName.setColumns(10);
		txtName.setName("Kunde.Vorname");
		txtName.setAction(actEnterPressed);
		ValidationComponentUtils.setMandatory(txtName, true);

		txtSurname = new JTextField();
		lblSurname.setLabelFor(txtSurname);
		txtSurname.setColumns(10);
		txtSurname.setName("Kunde.Nachname");
		txtSurname.setAction(actEnterPressed);
		ValidationComponentUtils.setMandatory(txtSurname, true);

		txtStreet = new JTextField();
		lblStreet.setLabelFor(txtStreet);
		txtStreet.setColumns(10);
		txtStreet.setName("Kunde.Strasse");
		txtStreet.setAction(actEnterPressed);
		ValidationComponentUtils.setMandatory(txtStreet, true);

		txtZip = new JTextField();
		lblZip.setLabelFor(txtZip);
		txtZip.setColumns(10);
		txtZip.setName("Kunde.PLZ");
		txtZip.setAction(actEnterPressed);
		ValidationComponentUtils.setMandatory(txtZip, true);

		txtCity = new JTextField();
		lblCity.setLabelFor(txtCity);
		txtCity.setColumns(10);
		txtCity.setName("Kunde.Stadt");
		txtCity.setAction(actEnterPressed);
		ValidationComponentUtils.setMandatory(txtCity, true);

		btnSave = new JButton(actSave);
		actSave.setEnabled(false);

		JTextField[] fields = { txtName, txtCity, txtStreet, txtSurname, txtZip };
		formValidator = new FormValidator<Customer>(frmCustomerForm, fields, new CustomerValidator(), btnSave, actSave) {
			@Override
			public Customer createObject() {
				Integer zip = null;
				try {
					zip = Integer.parseInt(txtZip.getText());
				} catch (NumberFormatException ex) {
				}
				return new Customer(txtName.getText(), txtSurname.getText(), txtStreet.getText(), txtCity.getText(), zip);
			}
		};

		btnCancel = new JButton("Abbrechen");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				frmCustomerForm.dispose();
			}
		});
	}
	
	private Component getButtonsPanel() {
		FormLayout layout = new FormLayout("pref:grow, 5dlu, pref:grow", "pref");
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(btnSave, cc.xy(1, 1));
		panel.add(btnCancel, cc.xy(3, 1));
		return panel;
	}

	private class ActSave extends AbstractAction {
		private static final long serialVersionUID = 7524200258063461521L;

		public ActSave() {
			putValue(NAME, getSaveButtonTitle());
			putValue(MNEMONIC_KEY, KeyEvent.VK_S);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			putValue(SHORT_DESCRIPTION, getSaveButtonTitle());
			putValue(SMALL_ICON, new ImageIcon("data/icons/save.png"));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Customer c = formValidator.validateForm(null);
			if (c == null) {
				assert false; // Execution should never reach this point
			}
			saveCustomer(c);
			frmCustomerForm.dispose();
		}
	}
}
