package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.jgoodies.animation.Animation;
import com.jgoodies.animation.Animator;
import com.jgoodies.animation.animations.GlyphAnimation;
import com.jgoodies.animation.components.GlyphLabel;

public final class Bublublub {

	private static final long serialVersionUID = 8914198159717627458L;
	private GlyphLabel glyphLabel;
	private static final int DURATION = 4000;
	private static final int GLYPH_DELAY = 150;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		new Bublublub();
	}

	public Bublublub() {
		initComponents();
		startAnimation();
		JFrame frame = new JFrame();
		frame.setTitle("Animation Glyph Label");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(buildPanel());
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
	}

	private void startAnimation() {
		Animation animation = new GlyphAnimation(glyphLabel, DURATION, GLYPH_DELAY, glyphLabel.getText());
		int fps = 30;
		new Animator(animation, fps).start();
	}

	private void initComponents() {
		glyphLabel = new GlyphLabel("Hello Remo! ;)", DURATION, GLYPH_DELAY);
		glyphLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
	}

	private JComponent buildPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		panel.add(glyphLabel);
		panel.setPreferredSize(new Dimension(500, 250));
		return panel;
	}

}
