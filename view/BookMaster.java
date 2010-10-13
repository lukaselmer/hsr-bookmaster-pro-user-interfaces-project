package view;

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
import javax.swing.table.TableColumn;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class BookMaster {

	private JFrame frame;
	private JTable tblBooks;
	private JTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookMaster window = new BookMaster();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BookMaster() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 652, 477);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);

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

		tblBooks = new JTable();
		panel.add(tblBooks, BorderLayout.CENTER);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Buch Inventar", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.add(panel_1, BorderLayout.NORTH);

		JLabel lblBookTableDescription = new JLabel("Alle Bücher in der Bibliothek sind in der  unterstehenden Tabelle");

		textField = new JTextField();
		textField.setColumns(10);

		JLabel lblSearch = new JLabel("Suche:");

		JCheckBox chckbxAvailibleOnly = new JCheckBox("Nur Verfügbare");

		JButton btnShowSelected = new JButton("Selektierte Anzeigen");

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
												gl_panel_1
														.createSequentialGroup()
														.addComponent(lblSearch)
														.addGap(12)
														.addComponent(textField, GroupLayout.PREFERRED_SIZE, 157,
																GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(chckbxAvailibleOnly).addPreferredGap(ComponentPlacement.UNRELATED)
														.addComponent(btnShowSelected).addGap(12).addComponent(btnAddNewBook)))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_panel_1.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.LEADING).addGroup(
				gl_panel_1
						.createSequentialGroup()
						.addComponent(lblBookTableDescription)
						.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(
								gl_panel_1
										.createParallelGroup(Alignment.BASELINE)
										.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE).addComponent(lblSearch).addComponent(chckbxAvailibleOnly)
										.addComponent(btnShowSelected).addComponent(btnAddNewBook))));
		panel_1.setLayout(gl_panel_1);

		JPanel pnlLoans = new JPanel();
		tabbedPane.addTab("Ausleihen", null, pnlLoans, null);
	}
}
