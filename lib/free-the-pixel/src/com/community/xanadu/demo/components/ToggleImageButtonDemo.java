package com.community.xanadu.demo.components;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;

import com.community.xanadu.components.buttons.toggleButtons.ShrinkingToggleImageAndTextButton;
import com.community.xanadu.components.buttons.toggleButtons.ShrinkingToggleImageButton;
import com.community.xanadu.utils.ImageUtils;

public class ToggleImageButtonDemo {
	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				try {
					UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
					JFrame.setDefaultLookAndFeelDecorated(true);
				} catch (final Exception e) {
				}

				final JFrame frame = new JFrame();
				frame.getContentPane().setLayout(new MigLayout("fill"));
				final ButtonGroup bg = new ButtonGroup();
				frame.getContentPane().add(new JLabel(), "spanx, split 5");
				for (int i = 0; i < 4; i++) {
					final ShrinkingToggleImageButton tib = new ShrinkingToggleImageButton();
					bg.add(tib);
					tib.setImage(ImageUtils.createRandomStrippedImage());
					frame.getContentPane().add(tib, "h 118! , w 118!,grow");
				}

				frame.getContentPane().add(new ShrinkingToggleImageAndTextButton("OPTION 1"), "newline,gaptop 50,grow");
				frame.getContentPane().add(new ShrinkingToggleImageAndTextButton("OPTION 1\nand ICON", ImageUtils.createRandomStrippedImage(100)),
						"gaptop 50,grow");

				frame.getContentPane().add(new ShrinkingToggleImageAndTextButton("OPTION 2"), "newline,grow");
				frame.getContentPane().add(new ShrinkingToggleImageAndTextButton("OPTION 2\nand ICON", ImageUtils.createRandomStrippedImage(100)),
						"grow");

				frame.getContentPane().add(new ShrinkingToggleImageAndTextButton("OPTION 3"), "newline,grow");
				frame.getContentPane().add(new ShrinkingToggleImageAndTextButton("OPTION 3\nand ICON", ImageUtils.createRandomStrippedImage(100)),
						"grow");

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
