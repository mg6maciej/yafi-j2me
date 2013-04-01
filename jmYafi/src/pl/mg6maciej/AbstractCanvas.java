
package pl.mg6maciej;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;

/**
 * @author mg6maciej
 */
public abstract class AbstractCanvas extends Canvas implements CommandListener, Runnable {

	private Display display;

	public AbstractCanvas(Display display) {
		this.display = display;
		super.setCommandListener(this);
	}

	public Display getDisplay() {
		return display;
	}

	public void commandAction(Command cmd, Displayable d) {
		if (listener != null) {
			listener.commandAction(cmd, d);
		}
	}
	
	private CommandListener listener;

	public void setCommandListener(CommandListener l) {
		listener = l;
	}

	public static final Command GAME_A_COMMAND = new Command("", Command.SCREEN, 0);
	public static final Command GAME_B_COMMAND = new Command("", Command.SCREEN, 0);
	public static final Command GAME_C_COMMAND = new Command("", Command.SCREEN, 0);
	public static final Command GAME_D_COMMAND = new Command("", Command.SCREEN, 0);

//	private int fontFace = Font.getDefaultFont().getFace();
//	private int fontSize = Font.getDefaultFont().getSize();
	
	protected void paint(Graphics g) {
//		Font f = g.getFont();
//		g.setFont(Font.getFont(getFontFace(), f.getStyle(), getFontSize()));
		paintBackground(g);
	}

	protected void paintBackground(Graphics g) {
	}

//	public int getFontFace() {
//		return fontFace;
//	}
//
//	public void setFontFace(int fontFace) {
//		if (!(fontFace == Font.FACE_MONOSPACE
//				|| fontFace == Font.FACE_PROPORTIONAL
//				|| fontFace == Font.FACE_SYSTEM)) {
//			throw new IllegalArgumentException("face");
//		}
//		if (this.fontFace != fontFace) {
//			this.fontFace = fontFace;
//			repaint();
//		}
//	}
//
//	public int getFontSize() {
//		return fontSize;
//	}
//
//	public void setFontSize(int fontSize) {
//		if (!(fontSize == Font.SIZE_LARGE
//				|| fontSize == Font.SIZE_MEDIUM
//				|| fontSize == Font.SIZE_SMALL)) {
//			throw new IllegalArgumentException("size");
//		}
//		if (this.fontSize != fontSize) {
//			this.fontSize = fontSize;
//			repaint();
//		}
//	}
//
//	public boolean increaseFontSize() {
//		if (fontSize == Font.SIZE_LARGE) {
//			return false;
//		} else if (fontSize == Font.SIZE_MEDIUM) {
//			fontSize = Font.SIZE_LARGE;
//		} else if (fontSize == Font.SIZE_SMALL) {
//			fontSize = Font.SIZE_MEDIUM;
//		}
//		repaint();
//		return true;
//	}
//
//	public boolean decreaseFontSize() {
//		if (fontSize == Font.SIZE_SMALL) {
//			return false;
//		} else if (fontSize == Font.SIZE_MEDIUM) {
//			fontSize = Font.SIZE_SMALL;
//		} else if (fontSize == Font.SIZE_LARGE) {
//			fontSize = Font.SIZE_MEDIUM;
//		}
//		repaint();
//		return true;
//	}

	protected final void keyPressed(int keyCode) {
		int gameAction = getGameAction(keyCode);
		switch (gameAction) {
			case UP:
				upKeyPressed();
				break;
			case LEFT:
				leftKeyPressed();
				break;
			case RIGHT:
				rightKeyPressed();
				break;
			case DOWN:
				downKeyPressed();
				break;
			case FIRE:
				fireKeyPressed();
				break;
			case GAME_A:
				game_aKeyPressed();
				break;
			case GAME_B:
				game_bKeyPressed();
				break;
			case GAME_C:
				game_cKeyPressed();
				break;
			case GAME_D:
				game_dKeyPressed();
				break;
		}
	}

	protected void keyReleased(int keyCode) {
		int gameAction = getGameAction(keyCode);
		switch (gameAction) {
			case UP:
				upKeyReleased();
				break;
			case LEFT:
				leftKeyReleased();
				break;
			case RIGHT:
				rightKeyReleased();
				break;
			case DOWN:
				downKeyReleased();
				break;
			case FIRE:
				fireKeyReleased();
				break;
			case GAME_A:
				game_aKeyReleased();
				break;
			case GAME_B:
				game_bKeyReleased();
				break;
			case GAME_C:
				game_cKeyReleased();
				break;
			case GAME_D:
				game_dKeyReleased();
				break;
		}
	}

	protected final void keyRepeated(int keyCode) {
		int gameAction = getGameAction(keyCode);
		switch (gameAction) {
			case UP:
				upKeyRepeated();
				break;
			case LEFT:
				leftKeyRepeated();
				break;
			case RIGHT:
				rightKeyRepeated();
				break;
			case DOWN:
				downKeyRepeated();
				break;
			case FIRE:
				fireKeyRepeated();
				break;
			case GAME_A:
				game_aKeyRepeated();
				break;
			case GAME_B:
				game_bKeyRepeated();
				break;
			case GAME_C:
				game_cKeyRepeated();
				break;
			case GAME_D:
				game_dKeyRepeated();
				break;
		}
	}

	protected void downKeyPressed() {
	}

	protected void fireKeyPressed() {
	}

	protected void game_aKeyPressed() {
	}

	protected void game_bKeyPressed() {
	}

	protected void game_cKeyPressed() {
	}

	protected void game_dKeyPressed() {
	}

	protected void leftKeyPressed() {
	}

	protected void rightKeyPressed() {
	}

	protected void upKeyPressed() {
	}

	protected void downKeyReleased() {
	}

	protected void fireKeyReleased() {
	}

	protected void game_aKeyReleased() {
		commandAction(GAME_A_COMMAND, this);
	}

	protected void game_bKeyReleased() {
		commandAction(GAME_B_COMMAND, this);
	}

	protected void game_cKeyReleased() {
		commandAction(GAME_C_COMMAND, this);
	}

	protected void game_dKeyReleased() {
		commandAction(GAME_D_COMMAND, this);
	}

	protected void leftKeyReleased() {
	}

	protected void rightKeyReleased() {
	}

	protected void upKeyReleased() {
	}

	protected void downKeyRepeated() {
	}

	protected void fireKeyRepeated() {
	}

	protected void game_aKeyRepeated() {
	}

	protected void game_bKeyRepeated() {
	}

	protected void game_cKeyRepeated() {
	}

	protected void game_dKeyRepeated() {
	}

	protected void leftKeyRepeated() {
	}

	protected void rightKeyRepeated() {
	}

	protected void upKeyRepeated() {
	}

	private int pressX;
	private int pressY;
	private volatile boolean pressing;
	private volatile boolean dragging;
	private volatile Thread thread;

	protected final void pointerDragged(int x, int y) {
		if (!pressing) {
			return;
		}
		if (Math.abs(x - pressX) >= 4 || Math.abs(y - pressY) >= 4) {
			dragging = true;
		}
		if (dragging) {
			dragging(pressX, pressY, x, y);
		}
	}

	protected final void pointerPressed(int x, int y) {
		pressX = x;
		pressY = y;
		dragging = false;
		boolean consumed = pressed(x, y);
		if (!consumed) {
			pressing = true;
			if (getGenerateLongPressEvent()) {
				thread = new Thread(this);
				thread.start();
			}
		}
	}

	protected final void pointerReleased(int x, int y) {
		if (!pressing) {
			return;
		}
		pressing = false;
		if (dragging) {
			dropped(pressX, pressY, x, y);
		} else {
			clicked(pressX, pressY);
		}
	}

	public void run() {
		try {
			Thread current = Thread.currentThread();
			long start = System.currentTimeMillis();
			long end = System.currentTimeMillis();
			while (end - start < 990) {
				Thread.sleep(20);
				if (!pressing || dragging || thread != current) {
					return;
				}
				end = System.currentTimeMillis();
				pressing(end - start);
			}
			getDisplay().callSerially(new Runnable() {
				public void run() {
					if (!pressing || dragging) {
						return;
					}
					pressing = false;
					longClicked(pressX, pressY);
				}
			});
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	protected boolean getGenerateLongPressEvent() {
		return false;
	}

	protected boolean pressed(int x, int y) {
		return false;
	}

	protected void clicked(int x, int y) {
	}

	protected void longClicked(int x, int y) {
	}

	protected void pressing(long milliseconds) {
	}

	protected void dragging(int fromX, int fromY, int toX, int toY) {
	}

	protected void dropped(int fromX, int fromY, int toX, int toY) {
	}
}
