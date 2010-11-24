package view.loans_report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import view.BookMasterActions;
import view.BookMasterUiManager;
import view.ViewUtil;
import application.LibraryApp;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import domain.Library;
import domain.Loan;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class LoansReport {

	protected JFrame frmLoansReportForm;
	protected JTextArea txtAreaReport;
	protected JButton btnCancel;
	protected JButton btnUndo;
	private BookMasterUiManager uimanager;
	private String report;
	private JScrollPane sclPane;
	private List<Loan> loans;
	private Library library;
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntClose;
	private JMenu mnEdit;
	private JMenuItem mntUndoAndClose;
	private final Action actUndo = new ActUndo();
	private final Action actClose = new BookMasterActions.ActClose() {
		private static final long serialVersionUID = 5525544072589710482L;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			frmLoansReportForm.dispose();
		}
	};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
					Library l = LibraryApp.inst();
					new LoansReport(new BookMasterUiManager(l), l.getLoans());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoansReport(BookMasterUiManager uimanager, List<Loan> loans) {
		this.uimanager = uimanager;
		this.library = uimanager.getLibrary();
		this.loans = loans;
		this.report = uimanager.getLibrary().generateReportForLoansReturn(loans);
		initialize();
		frmLoansReportForm.setLocationByPlatform(true);
		frmLoansReportForm.setVisible(true);
	}

	protected void initMenu() {
		menuBar = new JMenuBar();
		frmLoansReportForm.setJMenuBar(menuBar);

		mnFile = new JMenu("Datei");
		menuBar.add(mnFile);

		mntClose = new JMenuItem(actClose);
		mnFile.add(mntClose);

		mnEdit = new JMenu("Bearbeiten");
		menuBar.add(mnEdit);

		mntUndoAndClose = new JMenuItem(actUndo);
		mnEdit.add(mntUndoAndClose);
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws ParseException
	 */
	private void initialize() {
		frmLoansReportForm = new JFrame();
		frmLoansReportForm.setTitle("Ausleihe Rückgabe Report");
		frmLoansReportForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmLoansReportForm.setSize(new Dimension(700, 600));
		frmLoansReportForm.setMinimumSize(new Dimension(350, 242));

		initComponents();
		initMenu();

		FormLayout layout = new FormLayout("5dlu, pref:grow, 5dlu", "4dlu, pref, 4dlu, fill:pref:grow, 5dlu, pref, 5dlu");
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(ViewUtil.getSeparator("Ausleihe Rückgabe Report"), cc.xy(2, 2));
		panel.add(sclPane, cc.xy(2, 4));
		panel.add(getButtonsPanel(), cc.xy(2, 6));

		frmLoansReportForm.getContentPane().add(panel, BorderLayout.CENTER);
	}

	private Component getButtonsPanel() {
		FormLayout layout = new FormLayout("pref:grow, 5dlu, pref, 5dlu, pref", "pref");
		JPanel panel = new JPanel(layout);
		CellConstraints cc = new CellConstraints();
		panel.add(btnUndo, cc.xy(3, 1));
		panel.add(btnCancel, cc.xy(5, 1));
		return panel;
	}

	private void initComponents() {
		txtAreaReport = new JTextArea();
		txtAreaReport.setColumns(10);
		txtAreaReport.setName("Ausleihen.Report");
		txtAreaReport.setText(report);
		txtAreaReport.setLineWrap(true);
		txtAreaReport.setEditable(false);
		txtAreaReport.setBackground(Color.WHITE);
		txtAreaReport.setCaretPosition(0);
		
		sclPane = new JScrollPane(txtAreaReport);
		sclPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		sclPane.setPreferredSize(new Dimension(10, 10));
		
		btnCancel = new JButton(actClose);
		btnUndo = new JButton(actUndo);
	}

	private class ActUndo extends AbstractAction {
		private static final long serialVersionUID = 7524200252063261221L;

		public ActUndo() {
			putValue(MNEMONIC_KEY, KeyEvent.VK_R);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
			putValue(NAME, "Rückgängig Und Schliessen");
			putValue(SHORT_DESCRIPTION, "Macht die Rückgabe der Ausleihen rückgängig und schliesst das Fenster");
		}

		public void actionPerformed(ActionEvent e) {
			for (Loan l : loans) {
				l.undoReturnCopy();
			}
			frmLoansReportForm.dispose();
		}
	}
}
