package com.community.xanadu.components.buttons.toggleButtons;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.image.ColorTintFilter;
import org.pushingpixels.substance.api.ComponentState;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlueSteelLookAndFeel;
import org.pushingpixels.substance.internal.utils.SubstanceColorSchemeUtilities;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.ease.Spline;

import com.community.xanadu.utils.ImageUtils;
import com.jhlabs.image.GaussianFilter;

/**
 *Button that behave like a JToggleButton/JRadioButton/JCheckBox<br>
 *when selected it shows the full image + a selection ring(optional)<br>
 *when not selected it shows a small transparent image<br>
 *unselected size= ratio*size<br>
 *unselected alpha= alphaMin<br>
 *prefered size=image size
 * 
 * @author DIelsch
 * 
 */

public class ShrinkingToggleImageButton extends JToggleButton {
	private static final long serialVersionUID = 1L;

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				try {
					UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
					JFrame.setDefaultLookAndFeelDecorated(true);
				} catch (final UnsupportedLookAndFeelException e1) {
				}

				final JFrame frame = new JFrame();
				frame.getContentPane().setLayout(new FlowLayout());
				final ButtonGroup bg = new ButtonGroup();
				for (int i = 0; i < 4; i++) {
					final ShrinkingToggleImageButton is = new ShrinkingToggleImageButton();
					bg.add(is);
					frame.getContentPane().add(is);
				}

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(900, 300);
				frame.setVisible(true);
			}
		});
	}

	private BufferedImage image;
	private float alphaMin;
	private float alpha;

	private float currentRatio;
	private float ratioMin;

	private Timeline timeline;

	private boolean oldSelected;

	private int selectionRingThickness;
	private Color selectionRingColor;
	private boolean withSelectionRing;
	private BufferedImage selectionRingImage;
	private float alphaSelectionRing;

	private final int blurRadius = 5;

	public ShrinkingToggleImageButton(final BufferedImage image) {
		this(image, .5f, .5f, true);
	}

	public ShrinkingToggleImageButton(final BufferedImage image, final float alphaMin, final float ratioMin, final boolean withSelectionRing) {
		if (alphaMin > 1 || alphaMin < 0) {
			throw new IllegalArgumentException("alphaMin not in range: [0,1]");
		}
		if (ratioMin <= 0 || ratioMin > 1) {
			throw new IllegalArgumentException("ratio not in range: ]0,1]");
		}
		this.image = image;
		this.alphaMin = alphaMin;
		this.currentRatio = ratioMin;
		this.ratioMin = ratioMin;
		this.alpha = alphaMin;
		this.withSelectionRing = withSelectionRing;
		this.oldSelected = false;
		this.selectionRingThickness = 5;
		if (SubstanceLookAndFeel.isCurrentLookAndFeel()) {
			this.selectionRingColor = SubstanceColorSchemeUtilities.getColorScheme(this, ComponentState.SELECTED).getMidColor();
		} else {
			this.selectionRingColor = new Color(80, 143, 32);
		}
		initGUI();
	}

	public ShrinkingToggleImageButton() {
		this(ImageUtils.createRandomColorCirlceImage());
	}

	private void initGUI() {
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setOpaque(false);
		if (this.image != null) {
			setPreferredSize(new Dimension(this.image.getWidth() + this.blurRadius + this.selectionRingThickness * 2, this.image.getHeight()
					+ this.blurRadius + this.selectionRingThickness * 2));
		}
		addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(final ChangeEvent e) {
				thisStateChanged();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent e) {
				thisComponentResized();
			}
		});
	}

	private void thisComponentResized() {
		this.selectionRingImage = null;
		repaint();
	}

	private void thisStateChanged() {

		if (this.oldSelected == isSelected()) {
			return;
		}
		if (this.timeline != null) {
			this.timeline.abort();
		}
		this.alphaSelectionRing = 0;

		this.timeline = new Timeline(this);
		this.timeline.setDuration(500);
		this.timeline.setEase(new Spline(0.7f));
		if (isWithSelectionRing()) {
			this.timeline.addPropertyToInterpolate("alphaSelectionRing", 0f, 1f);
		}
		this.timeline.addPropertyToInterpolate("currentRatio", getCurrentRatio(), isSelected() ? 1f : this.ratioMin);
		this.timeline.addPropertyToInterpolate("alpha", getAlpha(), isSelected() ? 1f : this.alphaMin);
		this.timeline.play();
		this.oldSelected = isSelected();
	}

	@Override
	protected void paintComponent(final Graphics g) {

		if (this.image == null) {
			return;
		}

		Graphics2D g2 = null;
		BufferedImage buff = null;
		if (isEnabled()) {
			g2 = (Graphics2D) g.create();
		} else {
			buff = GraphicsUtilities.createCompatibleTranslucentImage(getWidth(), getHeight());
			g2 = buff.createGraphics();
		}
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		final int w = (int) (this.currentRatio * this.image.getWidth());
		final int h = (int) (this.currentRatio * this.image.getHeight());
		final int x = (getWidth() - w) / 2;
		final int y = (getHeight() - h) / 2;

		g2.setComposite(AlphaComposite.SrcOver.derive(getAlpha()));
		g2.drawImage(this.image, x, y, w, h, null);

		if (isSelected() && this.withSelectionRing) {
			g2.setComposite(AlphaComposite.SrcOver.derive(getAlphaSelectionRing()));
			g2.drawImage(getSelectionRing(), 0, 0, null);
		}
		g2.dispose();

		if (!isEnabled()) {
			g2 = (Graphics2D) g.create();
			new ColorTintFilter(Color.GRAY, 0.9f).filter(buff, buff);
			g2.drawImage(buff, 0, 0, getWidth(), getHeight(), null);
			g2.dispose();
		}
	}

	private BufferedImage getSelectionRing() {
		if (this.selectionRingImage == null) {
			this.selectionRingImage = GraphicsUtilities.createCompatibleTranslucentImage(getWidth(), getHeight());
			final Graphics2D g = this.selectionRingImage.createGraphics();

			g.setColor(this.selectionRingColor);
			g.setStroke(new BasicStroke(this.selectionRingThickness));
			g.drawRoundRect(this.blurRadius, this.blurRadius, getWidth() - this.selectionRingThickness - this.blurRadius, getHeight()
					- this.selectionRingThickness - this.blurRadius, this.selectionRingThickness * 2, this.selectionRingThickness * 2);
			g.dispose();

			new GaussianFilter(this.blurRadius).filter(this.selectionRingImage, this.selectionRingImage);
		}
		return this.selectionRingImage;
	}

	public float getCurrentRatio() {
		return this.currentRatio;
	}

	public void setCurrentRatio(final float currentRatio) {
		this.currentRatio = currentRatio;
		repaint();
	}

	public float getAlpha() {
		return this.alpha;
	}

	public void setAlpha(final float alpha) {
		this.alpha = alpha;
		repaint();
	}

	@Override
	public boolean contains(final int x, final int y) {
		if (this.image == null) {
			return false;
		}
		final int w = (int) (this.currentRatio * this.image.getWidth());
		final int h = (int) (this.currentRatio * this.image.getHeight());
		final int x1 = (getWidth() - w) / 2;
		final int y1 = (getHeight() - h) / 2;

		return new Rectangle(x1, y1, w, h).contains(x, y);
	}

	public boolean isWithSelectionRing() {
		return this.withSelectionRing;
	}

	public void setWithSelectionRing(final boolean withSelectionRing) {
		this.withSelectionRing = withSelectionRing;
	}

	public float getAlphaSelectionRing() {
		return this.alphaSelectionRing;
	}

	public void setAlphaSelectionRing(final float alphaSelectionRing) {
		this.alphaSelectionRing = alphaSelectionRing;
	}

	public void setSelectionRingThickness(final int selectionRingThickness) {
		this.selectionRingThickness = selectionRingThickness;
		this.selectionRingImage = null;
		repaint();
	}

	public void setSelectionRingColor(final Color selectionRingColor) {
		this.selectionRingColor = selectionRingColor;
		this.selectionRingImage = null;
		repaint();
	}

	public Color getSelectionRingColor() {
		return this.selectionRingColor;
	}

	public int getSelectionRingThickness() {
		return this.selectionRingThickness;
	}

	public void setImage(final BufferedImage image) {
		this.image = image;
		repaint();
	}

	public void setRatio(final float ratio) {
		this.ratioMin = ratio;
		this.currentRatio = ratio;
		repaint();
	}

	public void setAlphaMin(final float alphaMin) {
		this.alphaMin = alphaMin;
		this.alpha = alphaMin;
		repaint();
	}

	public BufferedImage getImage() {
		return this.image;
	}

	public int getBlurRadius() {
		return this.blurRadius;
	}
}
