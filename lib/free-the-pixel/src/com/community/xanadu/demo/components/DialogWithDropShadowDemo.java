package com.community.xanadu.demo.components;

import java.awt.Window;
import java.lang.reflect.Method;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;

import org.pushingpixels.substance.api.skin.SubstanceDustCoffeeLookAndFeel;

import com.community.xanadu.components.windows.dropShadow.DialogWithDropShadow;
import com.community.xanadu.utils.WindowsUtils;

public class DialogWithDropShadowDemo {
	public static void main(final String[] args) {
		// test();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(new SubstanceDustCoffeeLookAndFeel());
				} catch (final UnsupportedLookAndFeelException e) {
				}

				final DialogWithDropShadow f = new DialogWithDropShadow(null);
				WindowsUtils.setOpaque(f, false);
				f.setSize(300, 200);

				f.setWithCloseButton(true);
				f.setTitle("Person info");
				f.getContentPane().setLayout(new MigLayout("wrap 2"));

				f.getContentPane().add(new JLabel("Name:"), "");
				f.getContentPane().add(new JTextField(15), "growx,pushx");
				f.getContentPane().add(new JLabel("Adress"), "");
				f.getContentPane().add(new JTextField(15), "growx");
				f.startShowAnim();
			}
		});
	}

	public static void test() {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(new SubstanceDustCoffeeLookAndFeel());
				} catch (final UnsupportedLookAndFeelException e) {
				}
				JFrame f = new JFrame();
				f.setUndecorated(true);
				WindowsUtils.setOpaque(f, false);

				try {
					Class<?> c = Class.forName("com.sun.awt.AWTUtilities");
					Method m = c.getMethod("setWindowOpaque", Window.class, boolean.class);
					m.invoke(null, f, false);
				} catch (Exception e) {
					e.printStackTrace();
				}

				f.setSize(300, 200);
				f.getContentPane().add(new JLabel("some label"));

				f.setVisible(true);
			}
		});
	}
}
