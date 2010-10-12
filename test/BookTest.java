package test;

import junit.framework.TestCase;
import domain.Book;
import domain.Shelf;

public class BookTest extends TestCase {

	public void testBookCreation() {
		Book b = new Book("The Definitive ANTLR Reference");
		b.setName("The Definitive ANTLR Reference");
		b.setAuthor("Terence Parr");
		b.setPublisher("The Pragmatic Programmers");
		b.setShelf(Shelf.A1);

		assertEquals("The Definitive ANTLR Reference", b.getName());
		b.setName("NewName");
		assertEquals("NewName", b.getName());
		assertEquals("Terence Parr", b.getAuthor());
		assertEquals("The Pragmatic Programmers", b.getPublisher());

	}
}
