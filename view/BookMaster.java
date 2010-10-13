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
import java.awt.Color;

public class BookMaster {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel pnlLoans = new JPanel();
		tabbedPane.addTab("Ausleihen", null, pnlLoans, null);
		
		JPanel pnlBooks = new JPanel();
		tabbedPane.addTab("Bücher", null, pnlBooks, null);
		pnlBooks.setLayout(new BorderLayout(0, 0));
		
		JPanel pnlInventoryStatistics = new JPanel();
		pnlInventoryStatistics.setBorder(new TitledBorder(null, "InventarStatistik", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlBooks.add(pnlInventoryStatistics, BorderLayout.NORTH);
		
		JLabel lblBooksAmount = new JLabel("Anzahl Bücher:");
		
		JLabel lblBooksAmountNum = new JLabel("777");
		
		JLabel lblExemplarAmount = new JLabel("Anzahl Exemplare:");
		
		JLabel lblExemplarAmountNum = new JLabel("777");
		GroupLayout gl_pnlInventoryStatistics = new GroupLayout(pnlInventoryStatistics);
		gl_pnlInventoryStatistics.setHorizontalGroup(
			gl_pnlInventoryStatistics.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInventoryStatistics.createSequentialGroup()
					.addGap(5)
					.addComponent(lblBooksAmount)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblBooksAmountNum)
					.addGap(21)
					.addComponent(lblExemplarAmount)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblExemplarAmountNum)
					.addGap(178))
		);
		gl_pnlInventoryStatistics.setVerticalGroup(
			gl_pnlInventoryStatistics.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlInventoryStatistics.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_pnlInventoryStatistics.createParallelGroup(Alignment.LEADING)
						.addComponent(lblBooksAmount)
						.addGroup(gl_pnlInventoryStatistics.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblBooksAmountNum)
							.addComponent(lblExemplarAmountNum)
							.addComponent(lblExemplarAmount))))
		);
		pnlInventoryStatistics.setLayout(gl_pnlInventoryStatistics);
	}

}
