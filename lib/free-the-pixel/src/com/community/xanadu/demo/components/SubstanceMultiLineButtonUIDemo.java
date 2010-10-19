package com.community.xanadu.demo.components;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;

import org.pushingpixels.substance.api.skin.SubstanceMagellanLookAndFeel;

import com.community.xanadu.components.buttons.SubstanceMultiLineButtonUI;
import com.community.xanadu.utils.ImageUtils;

public class SubstanceMultiLineButtonUIDemo {
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(new SubstanceMagellanLookAndFeel());
					JFrame.setDefaultLookAndFeelDecorated(true);
				} catch (final UnsupportedLookAndFeelException e1) {
				}
				final JFrame f = new JFrame();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.getContentPane().setLayout(new MigLayout(""));

				f.getContentPane().add(new JLabel("CustomUI"), "spanx,split 2");
				f.getContentPane().add(new JSeparator(), "grow");

				JButton b = new JButton("Do this\nand that");
				b.setIcon(new ImageIcon(ImageUtils.createRandomColorCirlceImage(50)));
				b.setUI(new SubstanceMultiLineButtonUI(b));
				f.getContentPane().add(b, "");

				b = new JButton("Do this\nand that");
				b.setUI(new SubstanceMultiLineButtonUI(b));
				f.getContentPane().add(b, "wrap");

				f.getContentPane().add(new JLabel("Basic HTML"), "spanx,split 2");
				f.getContentPane().add(new JSeparator(), "grow");

				b = new JButton("<html>Do this<br>and that");
				b.setIcon(new ImageIcon(ImageUtils.createRandomColorCirlceImage(50)));
				f.getContentPane().add(b, "");

				b = new JButton("<html>Do this<br>and that");
				f.getContentPane().add(b, "wrap");

				f.getContentPane().add(new JLabel("Normal text"), "spanx,split 2");
				f.getContentPane().add(new JSeparator(), "grow");
				f.getContentPane().add(new JButton("Default button"), "h 35");

				f.pack();
				f.setVisible(true);
			}
		});
	}
}
