package domain;

import java.util.Observable;
import java.util.Observer;

public class Book extends Observable implements Observer {

	private String name, author, publisher;
	private Shelf shelf;

	// public Book(String name) {
	// this(name, null, null, null);
	// }

	public Book(String name, String author, String publisher, Shelf shelf) {
		this.name = name;
		this.author = author;
		this.publisher = publisher;
		this.shelf = shelf;
	}

	public Book(Book b) {
		this(b.name, b.author, b.publisher, b.shelf);
	}

	public void updateValues(Book b) {
		if (!this.name.equals(b.name)) {
			this.name = b.name;
			setChanged();
		}
		if (!this.author.equals(b.author)) {
			this.author = b.author;
			setChanged();
		}
		if (!this.publisher.equals(b.publisher)) {
			this.publisher = b.publisher;
			setChanged();
		}
		if (!this.shelf.equals(b.shelf)) {
			this.shelf = b.shelf;
			setChanged();
		}
		notifyObservers();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (!this.name.equals(name)) {
			this.name = name;
			setChanged();
		}
		notifyObservers();
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		if (!this.author.equals(author)) {
			this.author = author;
			setChanged();
		}
		notifyObservers();
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		if (!this.publisher.equals(publisher)) {
			this.publisher = publisher;
			setChanged();
		}
		notifyObservers();
	}

	public Shelf getShelf() {
		return shelf;
	}

	public void setShelf(Shelf shelf) {
		if (!this.shelf.equals(shelf)) {
			this.shelf = shelf;
			setChanged();
		}
		notifyObservers();
	}

	@Override
	public String toString() {
		return name + ", " + author + ", " + publisher;
	}

	@Override
	public void update(Observable o, Object arg) {
		setChanged();
		notifyObservers();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Book) {
			Book b = (Book) o;
			return b.name.equals(name) && b.publisher.equals(publisher) && b.author.equals(author) && b.shelf.equals(shelf);
		}
		return super.equals(o);
	}
}
