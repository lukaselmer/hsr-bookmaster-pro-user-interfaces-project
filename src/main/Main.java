package main;

import javax.swing.UIManager;

import view.book_master.BookMaster;
import view.splash_screen.BookMasterSplashScreen;
import application.LibraryApp;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean showSplashScreen = getShowSplashScreen(args);
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		} catch (Exception e) {
			// Under Linux the com.jgoodies.looks.windows.WindowsLookAndFeel is
			// not implemented. Pass.
		}
		if (showSplashScreen) {
			showSplashScreenAndWait();
		}
		new BookMaster(LibraryApp.inst());
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
