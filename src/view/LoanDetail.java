package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Color;
import java.io.IOException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import application.LibraryApp;

import domain.IllegalLoanOperationException;
import domain.Library;

public class LoanDetail {

	private JFrame frame;
	private JTextField txtReturnDate;
	private JTextField txtCopyID;
	private JTextField txtIdentifier;
	private Library library;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Library l = new Library();
				try {
					LibraryApp.initLibrary(l);
				} catch (ParserConfigurationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SAXException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IllegalLoanOperationException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					LoanDetail window = new LoanDetail(l);
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
	public LoanDetail(Library library) {
		this.library = library;
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
		
		JPanel pnlLoanInformation = new JPanel();
		pnlLoanInformation.setBorder(new TitledBorder(null, "Ausleihe Informationen", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		frame.getContentPane().add(pnlLoanInformation, BorderLayout.NORTH);
		
		JLabel lblIdentifier = new JLabel("Kennung:");
		
		JLabel lblCustomer = new JLabel("Kunde:");
		
		JLabel lblReturnDate = new JLabel("Zurück am:");
		
		JComboBox cmbCustomer = new JComboBox(library.getCustomers().toArray());
		
		txtReturnDate = new JTextField();
		txtReturnDate.setColumns(10);
		
		txtIdentifier = new JTextField();
		txtIdentifier.setColumns(10);
		GroupLayout gl_pnlLoanInformation = new GroupLayout(pnlLoanInformation);
		gl_pnlLoanInformation.setHorizontalGroup(
			gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLoanInformation.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING)
						.addComponent(lblReturnDate)
						.addComponent(lblIdentifier)
						.addComponent(lblCustomer))
					.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
					.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING, false)
							.addComponent(cmbCustomer, Alignment.TRAILING, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(txtReturnDate, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 315, GroupLayout.PREFERRED_SIZE))
						.addComponent(txtIdentifier, GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_pnlLoanInformation.setVerticalGroup(
			gl_pnlLoanInformation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLoanInformation.createSequentialGroup()
					.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblIdentifier)
						.addComponent(txtIdentifier, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCustomer)
						.addComponent(cmbCustomer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_pnlLoanInformation.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblReturnDate)
						.addComponent(txtReturnDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlLoanInformation.setLayout(gl_pnlLoanInformation);
		
		JPanel pnlLoanAndLoanInformation = new JPanel();
		frame.getContentPane().add(pnlLoanAndLoanInformation, BorderLayout.CENTER);
		pnlLoanAndLoanInformation.setLayout(new BorderLayout(0, 0));
		
		JPanel pnlLoanNewCopy = new JPanel();
		pnlLoanNewCopy.setBorder(new TitledBorder(null, "Neues Exemplar ausleihen", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlLoanAndLoanInformation.add(pnlLoanNewCopy, BorderLayout.NORTH);
		
		JLabel lblCopyID = new JLabel("Exemplar-ID:");
		
		txtCopyID = new JTextField();
		txtCopyID.setColumns(10);
		
		JButton btnExemplarAusleihen = new JButton("Exemplar Ausleihen");
		GroupLayout gl_pnlLoanNewCopy = new GroupLayout(pnlLoanNewCopy);
		gl_pnlLoanNewCopy.setHorizontalGroup(
			gl_pnlLoanNewCopy.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLoanNewCopy.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblCopyID)
					.addGap(18)
					.addComponent(txtCopyID, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnExemplarAusleihen, GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_pnlLoanNewCopy.setVerticalGroup(
			gl_pnlLoanNewCopy.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_pnlLoanNewCopy.createSequentialGroup()
					.addGroup(gl_pnlLoanNewCopy.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblCopyID)
						.addComponent(btnExemplarAusleihen)
						.addComponent(txtCopyID, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		pnlLoanNewCopy.setLayout(gl_pnlLoanNewCopy);
		
		JPanel pnlLoanInformation_1 = new JPanel();
		pnlLoanInformation_1.setBorder(new TitledBorder(null, "Ausleihen von", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		pnlLoanAndLoanInformation.add(pnlLoanInformation_1, BorderLayout.CENTER);
	}

}
