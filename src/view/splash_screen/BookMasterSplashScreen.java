package view.splash_screen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.EmptyBorder;

import com.jgoodies.animation.Animation;
import com.jgoodies.animation.AnimationAdapter;
import com.jgoodies.animation.AnimationEvent;
import com.jgoodies.animation.AnimationListener;
import com.jgoodies.animation.Animations;
import com.jgoodies.animation.Animator;
import com.jgoodies.animation.animations.FanAnimation;
import com.jgoodies.animation.animations.GlyphAnimation;
import com.jgoodies.animation.components.FanComponent;
import com.jgoodies.animation.components.GlyphLabel;

public class BookMasterSplashScreen {

	private static final long serialVersionUID = 8914198159717627458L;

	final static int DURATION = 5200;
	final static int FRAME_RATE = 50;
	private static final int GLYPH_DELAY = 60;

	private JWindow winMain;
	private FanComponent fan;
	private GlyphLabel glyphLabel;
	private Animator fanAnimator;

	public BookMasterSplashScreen() {
		try {
			showSplashScreen();
		} catch (Exception ex) {
			exitSplashScreen();
		}
	}

	public void showSplashScreen() {
		winMain = new JWindow();
		winMain.setAlwaysOnTop(true);

		JPanel pnlMainContent = (JPanel) winMain.getContentPane();
		pnlMainContent.setLayout(new BorderLayout());
		pnlMainContent.setBackground(new Color(245, 245, 245));

		int width = 500;
		int height = 400;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		winMain.setBounds(x, y, width, height);

		glyphLabel = new GlyphLabel("Willkommen Bei BookMasterPro", DURATION, GLYPH_DELAY);
		glyphLabel.setForeground(Color.DARK_GRAY);
		glyphLabel.setFont(new Font("Tahoma", Font.BOLD, 24));
		glyphLabel.setPreferredSize(new Dimension(300, 100));
		pnlMainContent.add(glyphLabel, BorderLayout.NORTH);

		fan = new FanComponent(10, new Color(143, 173, 255));
		pnlMainContent.add(fan, BorderLayout.CENTER);

		JLabel label = new JLabel("Intro überspringen");
		Font f = label.getFont();
		label.setFont(new Font(f.getName(), Font.BOLD, f.getSize()));
		label.setBorder(new EmptyBorder(2, 5, 2, 5));
		label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		pnlMainContent.add(label, BorderLayout.SOUTH);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				exitSplashScreen();
			}
		});

		winMain.setVisible(true);

		FanAnimation a4 = FanAnimation.defaultFan(fan, 40500);
		fanAnimator = new Animator(a4, 1000);
		fanAnimator.start();

		Animation animation = createAnimation();
		animation.addAnimationListener(new AnimationListener() {
			@Override
			public void animationStarted(AnimationEvent e) {
			}

			@Override
			public void animationStopped(AnimationEvent e) {
				exitSplashScreen();
			}
		});

		Animator ani1 = new Animator(animation, 30);
		ani1.start();
	}

	private Animation createAnimation() {
		GlyphAnimation a1 = new GlyphAnimation(glyphLabel, DURATION, GLYPH_DELAY, glyphLabel.getText());
		a1.addAnimationListener(new StartStopHandler());
		Animation allSeq = Animations.sequential(new Animation[] { Animations.pause(100), a1 });
		return allSeq;
	}

	private class StartStopHandler extends AnimationAdapter {
		private String text;

		@Override
		public void animationStarted(AnimationEvent e) {
			text = glyphLabel.getText();
		}

		@Override
		public void animationStopped(AnimationEvent e) {
			glyphLabel.setText(text);
		}
	}

	public boolean isSplashScreenActive() {
		try {
			return winMain.isVisible();
		} catch (Throwable t) {
			// pass
		}
		exitSplashScreen();
		return false;
	}

	public synchronized void exitSplashScreen() {
		try {
			fanAnimator.stop();
		} catch (Throwable t) {
			// pass
		}
		try {
			winMain.setAlwaysOnTop(false);
			winMain.setVisible(false);
			winMain.dispose();
		} catch (Throwable t) {
			// pass
		}
	}

}