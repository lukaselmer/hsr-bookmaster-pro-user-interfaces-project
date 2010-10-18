package view;

import application.LibraryApp;

import com.jgoodies.animation.*;
import com.jgoodies.animation.animations.*;
import com.jgoodies.animation.components.*;
import java.awt.*;

import javax.swing.*;

public class SplashScreen {
	public SplashScreen() {
		win = new JWindow();
	}

	final static int DURATION = 30000;
	final static int FRAME_RATE = 30;

	int duration;

	BasicTextLabel label;
	JWindow win;

	FanComponent fan;

	public void showSplashScreen() {
		JPanel content = (JPanel) win.getContentPane();
		content.setLayout(new BorderLayout());
		content.setBackground(Color.WHITE);
		// content.setBorder(BorderFactory.createLineBorder(Color., 10));

		int width = 500;
		int height = 400;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		win.setBounds(x, y, width, height);

		label = new BasicTextLabel("");
		label.setBackground(Color.WHITE);
		label.setFont(new Font("Tahoma", Font.BOLD, 18));
		label.setPreferredSize(new Dimension(100, 100));
		content.add(label, BorderLayout.NORTH);

		fan = new FanComponent(10, Color.green);
		content.add(fan, BorderLayout.CENTER);

		win.setVisible(true);

		FanAnimation a4 = FanAnimation.defaultFan(fan, 40500);
		final Animator ani2 = new Animator(a4, 1000);
		ani2.start();

		Animation animation = createAnimation();

		AnimationListener al = new AnimationListener() {
			public void animationStarted(AnimationEvent e) {
			}

			public void animationStopped(AnimationEvent e) {
				ani2.stop();
				win.dispose();
				new BookMaster(LibraryApp.inst());
			}
		};
		animation.addAnimationListener(al);

		Animator ani1 = new Animator(animation, 1000);
		ani1.start();
	}

	private Animation createAnimation() {
		Animation a1 = BasicTextAnimation.defaultFade(label, 2500, "Willkommen Bei BookMaster", Color.orange);
		Animation allSeq = Animations.sequential(new Animation[] { Animations.pause(500), a1, Animations.pause(500), });
		return allSeq;
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel("com.jgoodies.looks.windows.WindowsLookAndFeel");
		new SplashScreen().showSplashScreen();
	}
}