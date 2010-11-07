package view.splash_screen;

import application.LibraryApp;

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

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Method;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import view.book_master.BookMaster;

public class SplashScreen {

	private static final long serialVersionUID = 8914198159717627458L;

	final static int DURATION = 5200;
	final static int FRAME_RATE = 50;
	private static final int GLYPH_DELAY = 60;

	private JWindow winMain;
	private FanComponent fan;
	private boolean bookMasterCreated;
	private GlyphLabel glyphLabel;
	private Animator fanAnimator;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		} catch (Exception e) {
		}
		new SplashScreen();
	}

	public SplashScreen() {
		try {
			showSplashScreen();
		} catch (Exception ex) {
			initBookMaster();
		}
	}

	public void showSplashScreen() {
		winMain = new JWindow();
		winMain.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				initBookMaster();
			}
		});
		JPanel pnlMainContent = (JPanel) winMain.getContentPane();
		pnlMainContent.setLayout(new BorderLayout());
		pnlMainContent.setBackground(Color.WHITE);

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

		fan = new FanComponent(10, Color.GREEN);
		pnlMainContent.add(fan, BorderLayout.CENTER);

		JLabel label = new JLabel("Intro überspringen");
		Font f = label.getFont();
		label.setFont(new Font(f.getName(), Font.BOLD, f.getSize()));
		label.setBorder(new EmptyBorder(2, 5, 2, 5));
        label.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		pnlMainContent.add(label, BorderLayout.SOUTH);
		
		winMain.setVisible(true);

		FanAnimation a4 = FanAnimation.defaultFan(fan, 40500);
		fanAnimator = new Animator(a4, 1000);
		fanAnimator.start();
		
		Animation animation = createAnimation();
		animation.addAnimationListener(new AnimationListener() {
			public void animationStarted(AnimationEvent e) {
			}

			public void animationStopped(AnimationEvent e) {
				initBookMaster();
			}
		});

		Animator ani1 = new Animator(animation, 30);
		ani1.start();
	}

	protected synchronized void initBookMaster() {
		try {
			fanAnimator.stop();
		} catch (Throwable t) {
			// pass
		}
		try {
			winMain.dispose();
		} catch (Throwable t) {
			// pass
		}
		if (!bookMasterCreated) {
			new BookMaster(LibraryApp.inst());
			bookMasterCreated = true;
		}
	}

	private Animation createAnimation() {
		GlyphAnimation a1 = new GlyphAnimation(glyphLabel, DURATION, GLYPH_DELAY, glyphLabel.getText());
		a1.addAnimationListener(new StartStopHandler());
		Animation allSeq = Animations.sequential(new Animation[] { Animations.pause(100), a1 });
		return allSeq;
	}

	private class StartStopHandler extends AnimationAdapter {

		private String text;

		public void animationStarted(AnimationEvent e) {
			text = glyphLabel.getText();
		}

		public void animationStopped(AnimationEvent e) {
			glyphLabel.setText(text);
		}
	}

}