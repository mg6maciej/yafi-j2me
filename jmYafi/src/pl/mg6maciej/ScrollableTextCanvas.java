
package pl.mg6maciej;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * @author mg6maciej
 */
public class ScrollableTextCanvas extends ScrollableCanvas {

	public ScrollableTextCanvas(Display display) {
		super(display);
		clear();
	}

	protected void paintBackground(Graphics g) {
		g.setColor(0xFFFFFF);
		GraphicsUtils.fillRect(g, 0, 0, getWidth(), getHeight());
	}

	protected void paint(Graphics g) {
		super.paint(g);
		g.setFont(getFont());
//		if (!calculated) {
//			String[] lines = text;
//			contentWidth = 0;
//			for (int i = 0; i < lines.length; i++) {
//				int width = getFont().stringWidth(lines[i]);
//				if (contentWidth < width) {
//					contentWidth = width;
//				}
//			}
//			calculated = true;
//		}
		g.setColor(0x000000);
		GraphicsUtils.drawStringArray(g, text, margin, 0, 0, getHeight());
	}

	private String[] text;

	private static final int margin = 3;

	private int contentWidth;
	//private int fontHeight;
	//private boolean calculated;

	private Font font;

	private Font getFont() {
		if (font == null) {
			font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
		}
		return font;
	}

	private void setFont(Font font) {
		Font old = getFont();
		this.font = font;
		if (old.getSize() != font.getSize()) {
			int newY = getY() * font.getHeight() / old.getHeight();
			setY(newY);
			if (getY() > getMaxY()) {
				setY(getMaxY());
			}
		}
		contentWidth = 0;
		for (int i = 0; i < this.text.length; i++) {
			int width = getFont().stringWidth(this.text[i]);
			if (contentWidth < width) {
				contentWidth = width;
			}
		}
		repaint();
	}

	public boolean increaseFontSize() {
		//boolean b = super.increaseFontSize();
		int size = getFont().getSize();
		if (size == Font.SIZE_LARGE) {
			return false;
		}
		if (size == Font.SIZE_MEDIUM) {
			size = Font.SIZE_LARGE;
		} else if (size == Font.SIZE_SMALL) {
			size = Font.SIZE_MEDIUM;
		}
		setFontSize(size);
		return true;
	}

	public boolean decreaseFontSize() {
		int size = getFont().getSize();
		if (size == Font.SIZE_SMALL) {
			return false;
		}
		if (size == Font.SIZE_MEDIUM) {
			size = Font.SIZE_SMALL;
		} else if (size == Font.SIZE_LARGE) {
			size = Font.SIZE_MEDIUM;
		}
		setFontSize(size);
		return true;
	}

	public int getFontSize() {
		return getFont().getSize();
	}

	public void setFontSize(int fontSize) {
//		super.setFontSize(fontSize);
//		if (fontHeight == 0) {
//			fontHeight = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, getFontSize()).getHeight();
//		}
		setFont(Font.getFont(getFont().getFace(), getFont().getStyle(), fontSize));
	}

//	public String getText() {
//		return StringUtils.join(text, '\n');
//	}

	public void clear() {
		text = new String[] { StringUtils.EMPTY };
		//calculated = true;
		contentWidth = 0;
		reset();
		repaint();
	}

	public void append(String text) {
		if (!StringUtils.isNullOrEmpty(text)) {
			int currY = getY();
			int maxY = getMaxY();
			String[] appended = StringUtils.split(text, '\n');
			String[] old = this.text;
			this.text = new String[old.length + appended.length - 1];
			System.arraycopy(old, 0, this.text, 0, old.length - 1);
			this.text[old.length - 1] = old[old.length - 1] + appended[0];
			System.arraycopy(appended, 1, this.text, old.length, appended.length - 1);
			//calculated = false;
			for (int i = old.length - 1; i < this.text.length; i++) {
				int width = getFont().stringWidth(this.text[i]);
				if (contentWidth < width) {
					contentWidth = width;
				}
			}
			if (currY == maxY) {
				scrollDown(getMaxY() - currY);
			}
			repaint();
		}
	}

	protected int getContentWidth() {
		return contentWidth + 2 * margin;
	}

	protected int getContentHeight() {
		return getFont().getHeight() * text.length;
	}
	
	private int state = 0;
	private int[] states = { '4', '8', '4', '8', '5', '2', '2', '2', '2', '5' };
	public boolean debug;

	protected void keyReleased(int keyCode) {
		super.keyReleased(keyCode);
		if (states[state] == keyCode) {
			state++;
			if (state == states.length) {
				debug = !debug;
				state = 0;
				append("debug " + (debug ? "on" : "off") + "\n");
			}
		} else {
			state = 0;
		}
	}
}
