package application;

import domain.Library;

/**
 * Loads the library in the background
 */
public class LibraryLoader extends Thread {

	private Library library;

	@Override
	public void run() {
		library = LibraryApp.inst();
	}

	public Library getLibrary() {
		return library;
	}

}
