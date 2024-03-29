package application;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import domain.Book;
import domain.Copy;
import domain.Customer;
import domain.IllegalLoanOperationException;
import domain.Library;
import domain.Loan;
import domain.Shelf;

/**
 * The library application is responsible for loading the library data from the
 * XML files
 */
public class LibraryApp {

	public static Library inst() {
		Library library = new Library();
		try {
			initLibrary(library);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalLoanOperationException e) {
			e.printStackTrace();
		}
		return library;
	}

	public static void initLibrary(Library library) throws ParserConfigurationException, SAXException, IOException,
			IllegalLoanOperationException {

		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

		loadCustomersFromXml(library, builder, new File("data/customers.xml"));

		loadBooksFromXml(library, builder, new File("data/books.xml"));

		// create pseudo random books and loans
		createBooksAndLoans(library);
	}

	private static void createBooksAndLoans(Library library) throws IllegalLoanOperationException {
		for (int i = 0; i < library.getBooks().size(); i++) {
			switch (i % 4) {
			case 0:
				Copy c1 = library.createAndAddCopy(library.getBooks().get(i));
				c1.setCondition(Copy.Condition.GOOD);
				createLoansForCopy(library, c1, i, 5);
				Copy c2 = library.createAndAddCopy(library.getBooks().get(i));
				c2.setCondition(Copy.Condition.DAMAGED);
				createLoansForCopy(library, c2, i, 2);
				Copy c3 = library.createAndAddCopy(library.getBooks().get(i));
				c3.setCondition(Copy.Condition.WASTE);
				break;
			case 1:
				Copy c4 = library.createAndAddCopy(library.getBooks().get(i));
				createLoansForCopy(library, c4, i, 4);
				library.createAndAddCopy(library.getBooks().get(i));
				break;
			case 2:
				Copy c5 = library.createAndAddCopy(library.getBooks().get(i));
				createLoansForCopy(library, c5, i, 2);
				break;
			case 3:
				Copy c6 = library.createAndAddCopy(library.getBooks().get(i));
				createOverdueLoanForCopy(library, c6, i);
				break;
			}
		}
	}

	private static void loadBooksFromXml(Library library, DocumentBuilder builder, File file) throws SAXException, IOException {
		Document doc2 = builder.parse(file);
		NodeList titles = doc2.getElementsByTagName("title");
		for (int i = 0; i < titles.getLength(); i++) {
			Node title = titles.item(i);
			Random r = new Random();
			Shelf[] vals = Shelf.values();
			Book b = new Book(getTextContentOf(title, "name"), getTextContentOf(title, "author"), getTextContentOf(title, "publisher"),
					vals[r.nextInt(vals.length)]);
			library.addBook(b);
		}
	}

	private static void loadCustomersFromXml(Library library, DocumentBuilder builder, File file) throws SAXException, IOException {
		Document doc = builder.parse(file);
		NodeList customers = doc.getElementsByTagName("customer");
		for (int i = 0; i < customers.getLength(); i++) {
			Node customer = customers.item(i);
			/* Customer c = */library.addCustomer(new Customer(getTextContentOf(customer, "name"), getTextContentOf(customer, "surname"),
					getTextContentOf(customer, "street"), getTextContentOf(customer, "city"), Integer.parseInt(getTextContentOf(customer,
							"zip"))));
		}
	}

	private static void createLoansForCopy(Library library, Copy copy, int position, int count) throws IllegalLoanOperationException {
		// Create Loans in the past
		for (int i = count; i > 1; i--) {
			Loan l = library.createAndAddLoan(getCustomer(library, position + i), copy);
			GregorianCalendar pickup = l.getPickupDate();
			pickup.add(Calendar.MONTH, -i);
			pickup.add(Calendar.DAY_OF_MONTH, position % 10);
			l.setPickupDate(pickup);
			GregorianCalendar ret = (GregorianCalendar) pickup.clone();
			ret.add(Calendar.DAY_OF_YEAR, position % 10 + i * 2);
			l.returnCopy(ret);
		}
		// Create actual open loans
		if (position % 2 == 0) {
			Loan l = library.createAndAddLoan(getCustomer(library, position), copy);
			GregorianCalendar pickup = l.getPickupDate();
			pickup.add(Calendar.DAY_OF_MONTH, -position % 10);
			l.setPickupDate(pickup);
		}
	}

	private static void createOverdueLoanForCopy(Library library, Copy copy, int position) throws IllegalLoanOperationException {
		Loan l = library.createAndAddLoan(getCustomer(library, position), copy);
		GregorianCalendar pickup = l.getPickupDate();
		pickup.add(Calendar.MONTH, -1);
		pickup.add(Calendar.DAY_OF_MONTH, -position % 15);
		l.setPickupDate(pickup);
	}

	private static Customer getCustomer(Library library, int position) {
		return library.getCustomers().get(position % library.getCustomers().size());
	}

	private static String getTextContentOf(Node element, String name) {
		NodeList attributes = element.getChildNodes();
		for (int r = 0; r < attributes.getLength(); r++) {
			if (attributes.item(r).getNodeName().equals(name)) {
				return attributes.item(r).getTextContent();
			}
		}
		return "";
	}
}
