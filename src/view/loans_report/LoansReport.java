package view.loans_report;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;

import view.BookMasterUiManager;
import view.ViewUtil;
import application.LibraryApp;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.view.ValidationComponentUtils;

import domain.Library;
import domain.Loan;

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

		FormLayout layout = new FormLayout("5dlu, pref:grow, 5dlu", "4dlu, pref, 4dlu, fill:pref:grow, 5dlu, pref, 5dlu");
		// TODO: Fix scrollbar size
		// layout.setRowGroups(new int[][] { { 2, 4, 6, 8, 10 } });
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
		// Report
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

		// Cancel button
		btnCancel = new JButton("Schliessen");
		btnCancel.setMnemonic(KeyEvent.VK_S);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmLoansReportForm.dispose();
			}
		});

		// Undo button
		btnUndo = new JButton("Rückgängig Und Schliessen");
		btnUndo.setMnemonic(KeyEvent.VK_R);
		btnUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Loan l : loans) {
					l.undoReturnCopy();
				}
				frmLoansReportForm.dispose();
			}
		});
	}
}
