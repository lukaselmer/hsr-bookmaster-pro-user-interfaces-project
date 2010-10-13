package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class BookDetail {

	private JFrame frmBuchDetailansicht;
	private JTextField txtTitle;
	private JTextField txtAuthor;
	private JTextField txtPublisher;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookDetail window = new BookDetail();
					window.frmBuchDetailansicht.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BookDetail() {
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
		pnlBookInformation.setBorder(new TitledBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Buch Informationen:", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmBuchDetailansicht.getContentPane().add(pnlBookInformation, BorderLayout.NORTH);
		
		JLabel lblTitel = new JLabel("Titel:");
		
		JLabel lblAuthor = new JLabel("Author:");
		
		JLabel lblPublisher = new JLabel("Verlag:");
		
		JLabel lblShelf = new JLabel("Regal:");
		
		txtTitle = new JTextField();
		txtTitle.setColumns(10);
		
		txtAuthor = new JTextField();
		txtAuthor.setColumns(10);
		
		txtPublisher = new JTextField();
		txtPublisher.setColumns(10);
		
		JComboBox cmbShelf = new JComboBox();
		cmbShelf.setModel(new DefaultComboBoxModel(domain.Shelf.values()));
		GroupLayout gl_pnlBookInformation = new GroupLayout(pnlBookInformation);
		gl_pnlBookInformation.setHorizontalGroup(
			gl_pnlBookInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlBookInformation.createSequentialGroup()
					.addGroup(gl_pnlBookInformation.createParallelGroup(Alignment.LEADING)
						.addComponent(lblTitel)
						.addComponent(lblAuthor)
						.addComponent(lblPublisher)
						.addComponent(lblShelf))
					.addGap(23)
					.addGroup(gl_pnlBookInformation.createParallelGroup(Alignment.LEADING)
						.addComponent(txtTitle, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
						.addComponent(txtAuthor, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
						.addComponent(txtPublisher, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
						.addComponent(cmbShelf, 0, 331, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_pnlBookInformation.setVerticalGroup(
			gl_pnlBookInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlBookInformation.createSequentialGroup()
					.addGroup(gl_pnlBookInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTitel)
						.addComponent(txtTitle, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlBookInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAuthor)
						.addComponent(txtAuthor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlBookInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblPublisher)
						.addComponent(txtPublisher, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlBookInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblShelf)
						.addComponent(cmbShelf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlBookInformation.setLayout(gl_pnlBookInformation);
		
		JPanel pnlCopies = new JPanel();
		pnlCopies.setBorder(new TitledBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)), "Exemplare", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		frmBuchDetailansicht.getContentPane().add(pnlCopies, BorderLayout.CENTER);
		
		JLabel lblQuantity = new JLabel("Anzahl: ");
		
		JButton btnRemoveSelected = new JButton("Ausgew\u00E4hlte Entfernen");
		
		JButton btnAddCopy = new JButton("Exemplar Hinzuf\u00FCgen");
		
		JScrollPane scrBooks = new JScrollPane();
		
		JLabel lblQuantityNumber = new JLabel("0");
		GroupLayout gl_pnlCopies = new GroupLayout(pnlCopies);
		gl_pnlCopies.setHorizontalGroup(
			gl_pnlCopies.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlCopies.createSequentialGroup()
					.addGroup(gl_pnlCopies.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlCopies.createSequentialGroup()
							.addComponent(lblQuantity)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblQuantityNumber)
							.addPreferredGap(ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
							.addComponent(btnRemoveSelected)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnAddCopy))
						.addGroup(gl_pnlCopies.createSequentialGroup()
							.addGap(10)
							.addComponent(scrBooks, GroupLayout.DEFAULT_SIZE, 392, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_pnlCopies.setVerticalGroup(
			gl_pnlCopies.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlCopies.createSequentialGroup()
					.addGroup(gl_pnlCopies.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblQuantity)
						.addComponent(btnRemoveSelected)
						.addComponent(btnAddCopy)
						.addComponent(lblQuantityNumber))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrBooks, GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
					.addGap(1))
		);
		
		JList lstBooks = new JList();
		scrBooks.setViewportView(lstBooks);
		pnlCopies.setLayout(gl_pnlCopies);
	}
}
