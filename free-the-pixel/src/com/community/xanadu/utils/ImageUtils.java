package com.community.xanadu.utils;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.Point;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.jdesktop.swingx.JXImagePanel;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.image.ColorTintFilter;
import org.pushingpixels.substance.api.DecorationAreaType;
import org.pushingpixels.substance.api.SubstanceColorScheme;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.utils.SubstanceImageCreator;
import org.pushingpixels.substance.internal.utils.SubstanceSizeUtils;

import com.jhlabs.image.GaussianFilter;

public class ImageUtils {

	public static void showImage(final Image img) {
		final JFrame f = new JFrame();
		f.getContentPane().setLayout(new BorderLayout());
		f.setSize(800, 600);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final JXImagePanel panel = new JXImagePanel();
		panel.setImage(img);
		f.getContentPane().add(panel);

		f.setVisible(true);
	}

	public static BufferedImage getShadowedImage(final Image img) {
		return getShadowedImage(img, 2, 2, 5, Color.BLACK, 1f, 0.6f, true);
	}

	public static BufferedImage getShadowedImage(final Image img, final int xoffset, final int yoffset, final int blurrRadius,
			final Color shadowColor, final float shadowColorLikeness, final float alpha, final boolean increaseSize) {
		BufferedImage shadow;
		if (increaseSize) {
			shadow = GraphicsUtilities.createCompatibleTranslucentImage(img.getWidth(null) + xoffset + blurrRadius, img.getHeight(null) + yoffset
					+ blurrRadius);
		} else {
			shadow = GraphicsUtilities.createCompatibleTranslucentImage(img.getWidth(null), img.getHeight(null));
		}

		// create the shadow
		Graphics2D g2 = (Graphics2D) shadow.getGraphics();
		g2.setComposite(AlphaComposite.SrcOver.derive(alpha));
		g2.drawImage(img, xoffset, yoffset, null);
		g2.dispose();

		new ColorTintFilter(shadowColor, shadowColorLikeness).filter(shadow, shadow);
		if (blurrRadius > 0) {
			new GaussianFilter(blurrRadius).filter(shadow, shadow);
		}

		// paint the original image
		g2 = (Graphics2D) shadow.getGraphics();
		g2.drawImage(img, 0, 0, null);
		g2.dispose();

		return shadow;
	}

	/**
	 * 
	 * @param direction
	 *            SwingConstant.South/North/East/West
	 * @param color
	 * @return
	 */
	public static ImageIcon getArrowIcon(final int direction, final Color color) {
		final SubstanceColorScheme mainActiveScheme = SubstanceLookAndFeel.getCurrentSkin().getMainDefaultColorScheme(DecorationAreaType.GENERAL);
		final Icon icon = SubstanceImageCreator.getArrowIcon(SubstanceSizeUtils.getControlFontSize(), direction, mainActiveScheme);
		final BufferedImage buff = GraphicsUtilities.createCompatibleTranslucentImage(icon.getIconWidth(), icon.getIconHeight());
		final Graphics2D g = (Graphics2D) buff.getGraphics();
		icon.paintIcon(null, g, 0, 0);
		g.setComposite(AlphaComposite.SrcAtop);
		g.setColor(color);
		g.fillRect(0, 0, buff.getWidth(), buff.getHeight());
		g.dispose();

		return new ImageIcon(buff);
	}

	public static BufferedImage createRandomColorCirlceImage() {
		return createRandomColorCirlceImage(100);
	}

	public static BufferedImage createRandomColorCirlceImage(final int size) {

		final BufferedImage image = GraphicsUtilities.createCompatibleTranslucentImage(size, size);
		final Graphics g = image.createGraphics();
		final Random r = new Random();

		PaintUtils.fillCircle(g, size, new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)), 0, 0);

		g.dispose();
		return image;
	}

	public static BufferedImage createRandomStrippedImage() {
		return createRandomStrippedImage(100);
	}

	public static BufferedImage createRandomStrippedImage(final int size) {

		final BufferedImage image = GraphicsUtilities.createCompatibleTranslucentImage(size, size);
		final Graphics2D g = image.createGraphics();
		final Random r = new Random();
		g.setColor(Color.BLACK);
		final Color c = new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255));

		final Point start = new Point(0, 0);
		final Point end = new Point(10, 10);
		final float[] fractions = new float[] { 0, 0.5f, 1 };
		final Color[] colors = new Color[] { new Color(226, 244, 253), c, new Color(226, 244, 253) };

		final LinearGradientPaint lgp = new LinearGradientPaint(start, end, fractions, colors, CycleMethod.REFLECT);

		g.setPaint(lgp);
		g.fillRect(0, 0, image.getWidth(), image.getHeight());

		g.dispose();
		return image;
	}

	public static BufferedImage getImageFromRessource(String path) {
		URL url = ClassLoader.getSystemResource(path);
		if (url != null) {
			try {
				return GraphicsUtilities.loadCompatibleImage(url);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
