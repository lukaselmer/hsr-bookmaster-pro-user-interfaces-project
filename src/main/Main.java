package main;

import javax.swing.UIManager;

import view.book_master.BookMaster;
import view.splash_screen.BookMasterSplashScreen;
import application.LibraryLoader;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean showSplashScreen = getShowSplashScreen(args);
		LibraryLoader ll = new LibraryLoader();
		ll.start();
		setLookAndFeel();
		if (showSplashScreen) {
			try {
				showSplashScreenAndWait();
			} catch (Throwable t) {
				// If anything with the animation goes wrong: no problem. Pass.
			}
		}
		try {
			ll.join();
		} catch (InterruptedException e) {
		}
		new BookMaster(ll.getLibrary());
	}

	private static void setLookAndFeel() {
		// Use system look and feel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
		}
		// Whenever possible, use the jgoodies look and feel
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
	}

	private static void showSplashScreenAndWait() {
		BookMasterSplashScreen s = new BookMasterSplashScreen();
		// Wait while splash screen is active
		while (s.isSplashScreenActive()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// pass
			}
		}
		s.exitSplashScreen();
	}

	private static boolean getShowSplashScreen(String[] args) {
		if (args.length > 0) {
			if (args[0].equals("--no-splashscreen")) {
				return false;
			}
		}
		return true;
	}

}
