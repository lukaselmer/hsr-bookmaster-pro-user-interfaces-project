package domain;

import java.util.Observable;
import java.util.Observer;

public class Book extends Observable implements Observer {

	private String name, author, publisher;
	private Shelf shelf;

//	public Book(String name) {
//		this(name, null, null, null);
//	}

	public Book(String name, String author, String publisher, Shelf shelf) {
		this.name = name;
		this.author = author;
		this.publisher = publisher;
		this.shelf = shelf;
	}

	public void updateValues(Book b) {
		this.name = b.name;
		this.author = b.author;
		this.publisher = b.publisher;
		this.shelf = b.shelf;
		setChanged();
		notifyObservers();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setChanged();
		notifyObservers();
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String autor) {
		this.author = autor;
		setChanged();
		notifyObservers();
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
		setChanged();
		notifyObservers();
	}

	public Shelf getShelf() {
		return shelf;
	}

	public void setShelf(Shelf shelf) {
		this.shelf = shelf;
		setChanged();
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
}
