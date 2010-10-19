package com.community.xanadu.components.buttons.toggleButtons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;

import com.community.xanadu.utils.ImageUtils;
import com.community.xanadu.utils.PaintUtils;
import com.community.xanadu.utils.TextUtils;

public class ShrinkingToggleImageAndTextButton extends ShrinkingToggleImageButton {

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
					JFrame.setDefaultLookAndFeelDecorated(true);
				} catch (final UnsupportedLookAndFeelException e1) {
				}

				final JFrame f = new JFrame();
				f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				f.getContentPane().setLayout(new MigLayout("fill"));
				f.getContentPane().add(new ShrinkingToggleImageAndTextButton("MMMM\nnnnn\nooooo", ImageUtils.createRandomStrippedImage(100)), "grow");
				f.pack();
				f.setVisible(true);
			}
		});
	}

	private final String text;

	public ShrinkingToggleImageAndTextButton(final String text) {
		this(text, null, 0.7f, 0.7f, true);
	}

	public ShrinkingToggleImageAndTextButton(final String text, final BufferedImage image) {
		this(text, image, 0.7f, 0.7f, true);
	}

	public ShrinkingToggleImageAndTextButton(final String text, final BufferedImage icon, final float alphaMin, final float ratioMin,
			final boolean withSelectionRing) {
		super(icon, alphaMin, ratioMin, withSelectionRing);
		setSelectionRingColor(new Color(80, 143, 32));
		this.text = text;
		setFont(getFont().deriveFont(22f));

		final BufferedImage dummy = GraphicsUtilities.createCompatibleImage(1, 1);
		final Graphics g = dummy.createGraphics();

		g.setFont(getFont());
		final Rectangle2D textBounds = TextUtils.getMultiLineStringBounds(text, g);
		g.dispose();
		Dimension d;

		if (icon != null) {
			d = new Dimension((int) (icon.getWidth() + textBounds.getWidth()) + 60 + getSelectionRingThickness() * 2, (int) (Math.max(icon
					.getHeight(), textBounds.getHeight())
					+ getSelectionRingThickness() * 2 + 20));
		} else {
			d = new Dimension((int) textBounds.getWidth() + 60 + getSelectionRingThickness() * 2, (int) textBounds.getHeight() + 20
					+ getSelectionRingThickness() * 2);
		}
		setPreferredSize(d);
		setMinimumSize(d);

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(final ComponentEvent e) {
				setImage(getLocImage(icon));
			}

			@Override
			public void componentResized(final ComponentEvent e) {
				componentShown(e);
			}
		});
	}

	private BufferedImage getLocImage(final BufferedImage icon) {
		final BufferedImage img = GraphicsUtilities.createCompatibleTranslucentImage(getWidth(), getHeight());

		final Graphics2D g2 = img.createGraphics();
		g2.setFont(getFont());
		PaintUtils.turnOnAntialias(g2);

		final Point start = new Point(0, 0);
		final Point end = new Point(0, img.getHeight());
		final float[] fractions = new float[] { 0, 0.5f, 1 };
		final Color[] colors = new Color[] { new Color(180, 220, 255), new Color(180, 220, 255).darker(), new Color(180, 220, 255) };

		final LinearGradientPaint lgp = new LinearGradientPaint(start, end, fractions, colors);
		g2.setPaint(lgp);
		g2.fillRoundRect(getSelectionRingThickness() + getBlurRadius() - 2, getSelectionRingThickness() + getBlurRadius() - 2, img.getWidth()
				- getSelectionRingThickness() * 2 - getBlurRadius() * 2 + 4, img.getHeight() - getSelectionRingThickness() * 2 - getBlurRadius() * 2
				+ 4, 7, 7);

		if (icon != null) {
			g2.drawImage(icon, null, 20, getHeight() / 2 - icon.getHeight() / 2);
		}

		int xText;
		final Rectangle textBounds = TextUtils.getMultiLineStringBounds(this.text, g2);

		if (icon == null) {
			xText = getSelectionRingThickness();
		} else {
			xText = getSelectionRingThickness() + 20 + icon.getWidth();
		}
		final int textMaxWidth = getWidth() - xText - getSelectionRingThickness() * 2;
		final int yText = (int) (getHeight() / 2 - textBounds.getHeight() / 2) - getSelectionRingThickness() / 2;

		PaintUtils.drawMultiLineText(g2, this.text, textMaxWidth, true, xText, yText, Color.BLACK, Color.GRAY);

		g2.setColor(Color.BLACK);
		g2.drawRoundRect(2, 2, img.getWidth() - getBlurRadius() / 2 - 2, img.getHeight() - getBlurRadius() / 2 - 2, 20, 20);

		g2.drawRoundRect(getSelectionRingThickness() + getBlurRadius() - 2, getSelectionRingThickness() + getBlurRadius() - 2, img.getWidth()
				- getSelectionRingThickness() * 2 - getBlurRadius() * 2 + 4, img.getHeight() - getSelectionRingThickness() * 2 - getBlurRadius() * 2
				+ 4, 7, 7);
		return img;
	}
}
