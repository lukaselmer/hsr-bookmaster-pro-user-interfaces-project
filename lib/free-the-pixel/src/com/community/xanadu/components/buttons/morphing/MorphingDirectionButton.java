package com.community.xanadu.components.buttons.morphing;

import java.awt.Shape;
import java.awt.geom.GeneralPath;

import com.community.xanadu.components.buttons.shape.Direction;

public class MorphingDirectionButton extends AbstractMorphingButton {
	private Direction direction;

	public MorphingDirectionButton(String text, Direction direction) {
		super(text);
		this.direction = direction;
	}

	@Override
	protected void createDestinationShape() {
		destinationShape = getShape();
	}

	protected Shape getShape() {
		if (this.direction == null)
			return null;
		GeneralPath shape = new GeneralPath();
		float w = getWidth() / 6;
		float h = getHeight() / 6;
		switch (this.direction) {
		case DOWN:
			shape.moveTo(3f * w, 6f * h);
			shape.lineTo(6f * w, 4f * h);
			shape.lineTo(5f * w, 4f * h);
			shape.lineTo(5f * w, 0f * h);
			shape.lineTo(1f * w, 0f * h);
			shape.lineTo(1f * w, 4f * h);
			shape.lineTo(0f * w, 4f * h);
			break;
		case UP:
			shape.moveTo(3f * w, 0f * h);
			shape.lineTo(6f * w, 2f * h);
			shape.lineTo(5f * w, 2f * h);
			shape.lineTo(5f * w, 6f * h);
			shape.lineTo(1f * w, 6f * h);
			shape.lineTo(1f * w, 2f * h);
			shape.lineTo(0f * w, 2f * h);
			break;
		case LEFT:
			shape.moveTo(6f * w, 1.5f * h);
			shape.lineTo(2f * w, 1.5f * h);
			shape.lineTo(2f * w, 0f * h);
			shape.lineTo(0f * w, 3f * h);
			shape.lineTo(2f * w, 6f * h);
			shape.lineTo(2f * w, 4.5 * h);
			shape.lineTo(6f * w, 4.5 * h);
			break;
		case RIGHT:
			shape.moveTo(0f * w, 1.5f * h);
			shape.lineTo(4f * w, 1.5f * h);
			shape.lineTo(4f * w, 0f * h);
			shape.lineTo(6f * w, 3f * h);
			shape.lineTo(4f * w, 6f * h);
			shape.lineTo(4f * w, 4.5 * h);
			shape.lineTo(0f * w, 4.5 * h);
			break;
		}

		shape.closePath();

		return shape;
	}

}
