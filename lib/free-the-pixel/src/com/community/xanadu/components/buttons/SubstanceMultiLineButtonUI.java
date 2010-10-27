package com.community.xanadu.components.buttons;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.internal.ui.SubstanceButtonUI;
import org.pushingpixels.substance.internal.utils.ButtonBackgroundDelegate;
import org.pushingpixels.substance.internal.utils.SubstanceCoreUtilities;

import com.community.xanadu.demo.components.SubstanceMultiLineButtonUIDemo;

public class SubstanceMultiLineButtonUI extends SubstanceButtonUI {

	public static void main(final String[] args) {
		SubstanceMultiLineButtonUIDemo.main(args);
	}

	private final ButtonBackgroundDelegate delegate;

	public static ComponentUI createUI(final JComponent comp) {
		SubstanceCoreUtilities.testComponentCreationThreadingViolation(comp);
		return new SubstanceMultiLineButtonUI((JButton) comp);
	}

	// cache the lines
	private String[] splittedText = null;

	public SubstanceMultiLineButtonUI(final JButton button) {
		super(button);
		this.delegate = new ButtonBackgroundDelegate();
	}

	@Override
	protected void installListeners(final AbstractButton b) {
		super.installListeners(b);
		this.textChangeListener = new TextChangeListener();
		b.addPropertyChangeListener("text", this.textChangeListener);
	}

	private TextChangeListener textChangeListener;

	private class TextChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(final PropertyChangeEvent evt) {
			if (evt.getNewValue() != null && !evt.getNewValue().toString().isEmpty()) {
				SubstanceMultiLineButtonUI.this.splittedText = evt.getNewValue().toString().split("\n");
			} else {
				SubstanceMultiLineButtonUI.this.splittedText = null;
			}
			SubstanceMultiLineButtonUI.this.button.repaint();
		}
	};

	@Override
	protected void uninstallListeners(final AbstractButton b) {
		super.uninstallListeners(b);
		b.removePropertyChangeListener(this.textChangeListener);
		this.textChangeListener = null;
	}

	@Override
	public void paint(final Graphics g, final JComponent c) {
		if (!SubstanceLookAndFeel.isCurrentLookAndFeel()) {
			return;
		}

		final AbstractButton b = (AbstractButton) c;

		final FontMetrics fm = g.getFontMetrics();

		final Insets i = c.getInsets();

		final Rectangle viewRect = new Rectangle();
		final Rectangle iconRect = new Rectangle();
		final Rectangle trueIconRect = new Rectangle();
		final Rectangle textRect = new Rectangle();

		viewRect.x = i.left;
		viewRect.y = i.top;
		viewRect.width = b.getWidth() - (i.right + viewRect.x);
		viewRect.height = b.getHeight() - (i.bottom + viewRect.y);

		textRect.x = textRect.y = textRect.width = textRect.height = 0;
		iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

		final Font f = c.getFont();

		final Graphics2D g2 = (Graphics2D) g.create();
		g2.setFont(f);

		this.delegate.updateBackground(g2, b);

		final String message = b.getText();

		if (this.splittedText == null && b.getText() != null && !b.getText().isEmpty()) {
			this.splittedText = b.getText().split("\n");
		}

		if (this.splittedText != null) {
			int lineNumber = 0;
			if (this.splittedText.length > 0 && message.length() > 0) {
				final View v = (View) c.getClientProperty(BasicHTML.propertyKey);
				if (v != null) {// HTML paint
					v.paint(g2, textRect);
				} else {
					for (final String s : this.splittedText) {
						viewRect.x = i.left;
						viewRect.y = i.top;
						viewRect.width = b.getWidth() - (i.right + viewRect.x);
						viewRect.height = b.getHeight() - (i.bottom + viewRect.y);

						textRect.x = textRect.y = textRect.width = textRect.height = 0;
						iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

						final String line = SwingUtilities.layoutCompoundLabel(c, fm, s, b.getIcon(), b.getVerticalAlignment(), b
								.getHorizontalAlignment(), b.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect, iconRect, textRect,
								b.getText() == null ? 0 : b.getIconTextGap());

						// set icon location
						if (b.getIcon() != null) {
							if (trueIconRect.x != 0) {
								trueIconRect.x = Math.min(trueIconRect.x, iconRect.x);
							} else {
								trueIconRect.x = iconRect.x;
							}
							trueIconRect.y = iconRect.y;
							trueIconRect.width = iconRect.width;
							trueIconRect.height = iconRect.height;
						}

						// position the text
						final double part = fm.getStringBounds(line, g2).getHeight();
						double y = 0.5 * this.button.getHeight();
						y -= part * 0.5 * this.splittedText.length;
						y += part * lineNumber;
						textRect.y = (int) y;
						this.paintButtonText(g2, b, textRect, line);

						lineNumber++;
					}
				}
			}
		}
		// Paint the Icon
		if (b.getIcon() != null) {
			if (this.splittedText == null || this.splittedText.length == 0) {
				// if no text
				SwingUtilities.layoutCompoundLabel(c, fm, "", b.getIcon(), b.getVerticalAlignment(), b.getHorizontalAlignment(), b
						.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewRect, trueIconRect, textRect, b.getText() == null ? 0 : b
						.getIconTextGap());
			}
			paintIcon(g2, c, trueIconRect);
		}
		g2.dispose();
	}

	@Override
	public Dimension getPreferredSize(final JComponent c) {
		final AbstractButton b = (AbstractButton) c;

		if (this.splittedText == null && b.getText() != null && !b.getText().isEmpty()) {
			this.splittedText = b.getText().split("\n");
		}

		String text = "";
		final Font font = b.getFont();
		final FontMetrics fm = b.getFontMetrics(font);
		int maxWidth = 0;
		// find the max width of the text
		if (this.splittedText != null && this.splittedText.length > 0) {
			for (final String s : this.splittedText) {
				final int width = SwingUtilities.computeStringWidth(fm, s);
				if (width > maxWidth) {
					text = s;
					maxWidth = width;
				}
			}
		}

		final Rectangle iconR = new Rectangle();
		final Rectangle textR = new Rectangle();
		final Rectangle viewR = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
		SwingUtilities.layoutCompoundLabel(b, fm, text, b.getIcon(), b.getVerticalAlignment(), b.getHorizontalAlignment(), b
				.getVerticalTextPosition(), b.getHorizontalTextPosition(), viewR, iconR, textR, (text == null ? 0 : b.getIconTextGap()));

		// The preferred size of the button is the size of the text and icon rectangles plus the buttons insets.
		if (this.splittedText != null && this.splittedText.length > 0) {
			textR.height = (textR.height) * this.splittedText.length;
		}

		final Rectangle r = iconR.union(textR);

		final Insets insets = b.getInsets();
		r.width += insets.left + insets.right;
		r.height += insets.top + insets.bottom;

		return r.getSize();
	}
}