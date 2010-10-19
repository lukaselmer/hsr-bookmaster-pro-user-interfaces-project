package com.community.xanadu.demo.components;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;

import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;
import org.pushingpixels.trident.TridentConfig;
import org.pushingpixels.trident.TridentConfig.PulseSource;

import com.community.xanadu.components.text.ScrollingText;
import com.community.xanadu.components.text.ScrollingText.AbstractCosineScrollTextUtils;
import com.community.xanadu.components.text.ScrollingText.DefaultBottomToTopScrollTextUtils;
import com.community.xanadu.components.text.ScrollingText.DefaultLeftToRightScrollTextUtils;
import com.community.xanadu.components.text.ScrollingText.DefaultRightToLeftCosineScrollTextUtils2;
import com.community.xanadu.components.text.ScrollingText.DefaultRightToLeftScrollTextUtils;
import com.community.xanadu.components.text.ScrollingText.DefaultTopToBottomScrollTextUtils;
import com.community.xanadu.utils.PaintUtils;
import com.community.xanadu.utils.ThreadUtils;

public class ScrollingTextDemo {
	public static void main(final String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				TridentConfig.getInstance().setPulseSource(new PulseSource() {

					@Override
					public void waitUntilNextPulse() {
						ThreadUtils.sleepQuietly(30);
					}
				});

				try {
					UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
				} catch (UnsupportedLookAndFeelException e) {
				}

				final JFrame f = new JFrame();
				f.setSize(600, 500);
				f.getContentPane().setLayout(new MigLayout("fill"));
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				ScrollingText st1 = new ScrollingText();
				st1.setBackground(Color.WHITE);
				st1.setOpaque(true);

				f.getContentPane().add(st1, "grow,span");

				ScrollingText st2 = new ScrollingText();
				st2.setScrollTextUtils(new DefaultRightToLeftScrollTextUtils(st2));
				f.getContentPane().add(st2, "grow,span");

				ScrollingText st22 = new ScrollingText();
				st22.setScrollTextUtils(new DefaultLeftToRightScrollTextUtils(st22));
				f.getContentPane().add(st22, "grow,span");
				//
				ScrollingText st3 = new ScrollingText();
				st3.setScrollTextUtils(new DefaultTopToBottomScrollTextUtils(st3));
				f.getContentPane().add(st3, "east, w 50!");
				//
				ScrollingText st4 = new ScrollingText();
				st4.setScrollTextUtils(new DefaultBottomToTopScrollTextUtils(st4));
				f.getContentPane().add(st4, "east, w 50!");

				ScrollingText st5 = new ScrollingText();
				st5.setScrollTextUtils(new DefaultRightToLeftCosineScrollTextUtils2(st5));
				f.getContentPane().add(st5, "grow,spanx");

				ScrollingText st6 = new ScrollingText();
				st6.setScrollTextUtils(new AbstractCosineScrollTextUtils(st6) {
					{
						this.spaceBetweenLetter = 25;
					}

					@Override
					public double getRotate(final float animProgress, final int charnum) {
						double t = ((2 * Math.PI) / this.scrollingText.getWidth()) * getXpos(animProgress, charnum);
						double cos = -Math.cos(t);
						double angle = cos / 4;
						return angle;
					}

					@Override
					public int getXpos(final float animProgress, final int charnum) {
						int x = this.scrollingText.getWidth() + this.textWidth;
						x = (int) ((1f - animProgress) * x) + charnum * this.spaceBetweenLetter - this.textWidth;
						return x;
					}

					@Override
					public int getYpos(final float animProgress, final int charnum) {
						int x = getXpos(animProgress, charnum);
						double t = ((2 * Math.PI) / this.scrollingText.getWidth()) * x;
						double cos = -Math.cos(t);
						int h = this.scrollingText.getHeight() - this.yOffset * 2;
						int res = (int) (cos * (h / 2 - this.scrollingText.getFont().getSize()) + h / 2);
						return res;
					};

					@Override
					public float getTransformedFontSize(final float animProgress, final int fontSize, final int charnum) {
						int x = getXpos(animProgress, charnum);
						double t = ((2 * Math.PI) / this.scrollingText.getWidth()) * x;
						double cos = Math.abs(Math.cos(t / 2 + Math.PI / 2));
						int res = (int) ((cos * fontSize)) + fontSize / 2;
						return res;
					}

					@Override
					public void paintCharacter(final Graphics2D g, final String s) {
						PaintUtils.drawHighLightText(g, s, 0, 0, Color.RED, Color.BLACK);
					}

					@Override
					public int getDuration() {
						int pxSec = 50;
						int duration = (this.textWidth + this.scrollingText.getWidth()) / pxSec * 1000;
						return duration;
					}

				});
				f.getContentPane().add(st6, "grow,spanx, h 150");

				f.setVisible(true);

				String text = "azertyuiop qsdfghjklm wxcvbn";
				st1.setText(text);
				st2.setText(text);
				st22.setText(text);
				st3.setText(text);
				st4.setText(text);
				st5.setText(text);
				st6.setText(text);

			}
		});
	}
}
