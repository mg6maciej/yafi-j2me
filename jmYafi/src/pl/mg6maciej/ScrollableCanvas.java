
package pl.mg6maciej;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

/**
 * @author mg6maciej
 */
public abstract class ScrollableCanvas extends AbstractCanvas {

	public ScrollableCanvas(Display display) {
		super(display);
	}
	
	protected void paint(Graphics g) {
		super.paint(g);
		g.translate(-x, -y);
	}

	private int x;
	private int y;
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	protected void setY(int y) {
		this.y = y;
	}
	
	public int getMaxX() {
		int max = getContentWidth() - getWidth();
		if (max < 0) {
			max = 0;
		}
		return max;
	}
	
	public int getMaxY() {
		int max = getContentHeight() - getHeight();
		if (max < 0) {
			max = 0;
		}
		return max;
	}

	public void reset() {
		if (x != 0 || y != 0) {
			x = 0;
			y = 0;
			repaint();
		}
	}

	protected boolean updateXY(Graphics g) {
		int tmpX = x;
		int tmpY = y;
		int maxX = getMaxX();
		if (x > maxX) {
			x = maxX;
		}
		int maxY = getMaxY();
		if (y > maxY) {
			y = maxY;
		}
		return x != tmpX || y != tmpY;
	}

	protected int getHorizontalPressShift() {
		return getWidth() / 2;
	}

	protected int getHorizontalRepeatShift() {
		return getWidth() - 20;
	}

	protected int getVerticalPressShift() {
		return getHeight() / 2;
	}

	protected int getVerticalRepeatShift() {
		return getHeight() - 20;
	}

	private static final int UNDEFINED = Integer.MAX_VALUE - 1048576;

	protected int getContentWidth() {
		return UNDEFINED;
	}

	protected int getContentHeight() {
		return UNDEFINED;
	}

	protected void leftKeyPressed() { scrollLeft(getHorizontalPressShift()); }
	protected void leftKeyRepeated() { scrollLeft(getHorizontalRepeatShift()); }

	public void scrollLeft(int shift) {
		if (x > 0) {
			x -= shift;
			if (x < 0) {
				x = 0;
			}
			repaint();
		}
	}

	protected void rightKeyPressed() { scrollRight(getHorizontalPressShift()); }
	protected void rightKeyRepeated() { scrollRight(getHorizontalRepeatShift()); }

	public void scrollRight(int shift) {
		int maxX = getMaxX();
		if (x < maxX) {
			x += shift;
			if (x > maxX) {
				x = maxX;
			}
			repaint();
		}
	}

	protected void upKeyPressed() { scrollUp(getVerticalPressShift()); }
	protected void upKeyRepeated() { scrollUp(getVerticalRepeatShift()); }

	public void scrollUp(int shift) {
		if (y > 0) {
			y -= shift;
			if (y < 0) {
				y = 0;
			}
			repaint();
		}
	}

	protected void downKeyPressed() { scrollDown(getVerticalPressShift()); }
	protected void downKeyRepeated() { scrollDown(getVerticalRepeatShift()); }

	public void scrollDown(int shift) {
		int maxY = getMaxY();
		if (y < maxY) {
			y += shift;
			if (y > maxY) {
				y = maxY;
			}
			repaint();
		}
	}

	private int prevX;
	private int prevY;

	protected boolean pressed(int x, int y) {
		prevX = x;
		prevY = y;
		return false;
	}

	protected void dragging(int fromX, int fromY, int toX, int toY) {
		scroll(toX, toY);
	}

	protected void dropped(int fromX, int fromY, int toX, int toY) {
		scroll(toX, toY);
	}

	private void scroll(int toX, int toY) {
		int tmpX = x;
		int tmpY = y;
		x += (prevX - toX);
		y += (prevY - toY);
		prevX = toX;
		prevY = toY;
		if (x < 0) {
			x = 0;
		}
		if (x > getMaxX()) {
			x = getMaxX();
		}
		if (y < 0) {
			y = 0;
		}
		if (y > getMaxY()) {
			y = getMaxY();
		}
		if (x != tmpX || y != tmpY) {
			repaint();
		}
	}
}
