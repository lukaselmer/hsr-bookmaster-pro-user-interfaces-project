package com.community.xanadu.components.text;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.pushingpixels.trident.Timeline;

import com.community.xanadu.utils.PaintUtils;

public class SpinningReelText extends JPanel {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame f = new JFrame();
				final SpinningReelText text = new SpinningReelText();
				f.getContentPane().add(text);
				f.setSize(400, 220);
				f.setVisible(true);

				text.setT1("azertyuiop");
				text.setT2("qsdfghjklm");
				JButton b = new JButton("go");
				b.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {

						text.t1 = text.t2;
						text.t2 = System.currentTimeMillis() + "";

						text.startAnim();
					}
				});
				f.getContentPane().add(b, BorderLayout.SOUTH);
			}
		});
	}

	public SpinningReelText() {
		setFont(getFont().deriveFont(25f));
	}

	private String t1;
	private String t2;
	private float animProgress;

	private int angle = 30;

	private int circleWidth = 800;

	@Override
	protected void paintComponent(Graphics arg) {
		super.paintComponent(arg);

		int h = getHeight();

		Graphics2D g = (Graphics2D) arg.create();

		g.translate(0, -circleWidth / 2 + h / 2);

		Rectangle2D b1 = g.getFontMetrics().getStringBounds(t1, g);

		PaintUtils.turnOnAntialias(g);

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, circleWidth, circleWidth);

		g.fillRect(0, circleWidth / 2 - h / 2, getWidth(), h);
		g.clipRect(0, circleWidth / 2 - h / 2, getWidth(), h);

		g.setColor(Color.BLACK);

		g.setComposite(AlphaComposite.SrcOver.derive(1f - animProgress));
		g.rotate(Math.toRadians(-angle) * animProgress, circleWidth / 2, circleWidth / 2);
		g.drawString(t1, 0, (int) (circleWidth / 2 + b1.getHeight() / 4));

		g.setComposite(AlphaComposite.SrcOver.derive(animProgress));
		g.rotate(Math.toRadians(angle), circleWidth / 2, circleWidth / 2);
		g.drawString(t2, 0, (int) (circleWidth / 2 + b1.getHeight() / 4));

		g.dispose();

	}

	private Timeline timeline;

	public void startAnim() {
		if (timeline != null) {
			timeline.cancel();
		}
		timeline = new Timeline(this);
		timeline.addPropertyToInterpolate("animProgress", 0f, 1f);
		timeline.play();
	}

	public void setAnimProgress(float animProgress) {
		this.animProgress = animProgress;
		repaint();
	}

	public void setT1(String t1) {
		this.t1 = t1;
	}

	public void setT2(String t2) {
		this.t2 = t2;
	}

}
