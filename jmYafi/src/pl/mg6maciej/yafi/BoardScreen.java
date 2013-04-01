/*
 * Copyright (C) 2013 Maciej Górski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.mg6maciej.yafi;

import java.io.IOException;
import java.util.Hashtable;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import pl.mg6maciej.AbstractCanvas;
import pl.mg6maciej.ArrayUtils;
import pl.mg6maciej.DateUtils;
import pl.mg6maciej.GraphicsUtils;
import pl.mg6maciej.StringUtils;
import pl.mg6maciej.chess.Position;
import pl.mg6maciej.chess.PositionWrapper;

/**
 * @author mg6maciej
 */
public class BoardScreen extends AbstractCanvas {

	private PositionWrapper wrapper;
	private int x, y;
	private boolean wrap;
	private boolean nextOnWrap;
	private int xFrom, yFrom;
	private boolean moving;
	private boolean flip;
	private long startTime;
	private String comment;
	
	private static final int margin = 1;

	public BoardScreen(Display display) {
		super(display);
		this.wrapper = PositionWrapper.initial();
		this.x = Position.SIZE / 2;
		this.y = Position.SIZE / 2;
		this.wrap = true;
		this.nextOnWrap = true;
	}
	
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public PositionWrapper getPositionWrapper() {
		return wrapper;
	}

	public void setPositionWrapper(PositionWrapper position) {
		this.wrapper = position;
		this.startTime = System.currentTimeMillis();
		repaint();
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
		repaint();
	}

	public boolean getFlip() {
		return flip ^ wrapper.flip;
	}

	public void flip() {
		flip = !flip;
		repaint();
	}

	protected void paintBackground(Graphics g) {
		g.setColor(0xffffff);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

//	private int fontHeight;

	protected void paint(Graphics g) {
		super.paint(g);
//		{
//			Font f = g.getFont();
//			g.setFont(Font.getFont(Font.FACE_MONOSPACE, f.getStyle(), f.getSize()));
//		}
//		fontHeight = g.getFont().getHeight();
		char active = wrapper.getPosition().getActivePlayer();
		{
			String handle = getFlip() ? wrapper.whiteName : wrapper.blackName;
			long time = getFlip() ? wrapper.whiteTime : wrapper.blackTime;
			int lag = (getFlip() ^ active == 'w') ? wrapper.lag : 0;
			boolean running = wrapper.clockRunning && active == (getFlip() ? 'w' : 'b');
			paintInfo(g, handle, time, lag, running);
		}
		paintBoard(g);
		{
			String handle = getFlip() ? wrapper.blackName : wrapper.whiteName;
			long time = getFlip() ? wrapper.blackTime : wrapper.whiteTime;
			int lag = (getFlip() ^ active == 'b') ? wrapper.lag : 0;
			boolean running = wrapper.clockRunning && active == (getFlip() ? 'b' : 'w');
			paintInfo(g, handle, time, lag, running);
		}
		if (!StringUtils.isNullOrEmpty(wrapper.result)) {
			String str = wrapper.result + " | " + wrapper.description;
			g.setColor(0, 0, 0);
			GraphicsUtils.drawString(g, str, margin, 0, Graphics.TOP | Graphics.LEFT);
		} else if (!StringUtils.isNullOrEmpty(getComment())) {
			String str = getComment();
			g.setColor(0x000000);
			GraphicsUtils.drawString(g, str, margin, 0, Graphics.TOP | Graphics.LEFT);
		}
		if (wrapper.clockRunning && t == null) {
			t = new Thread(new Runnable() {
			 	public void run() {
					try {
						Thread.sleep(33);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					t = null;
					repaint();
				}
			});
			t.start();
		}
//		g.translate(-g.getTranslateX(), -g.getTranslateY());
//		Enumeration e;
//		e = clicked.elements();
//		while (e.hasMoreElements()) {
//			Point p = (Point) e.nextElement();
//			g.setColor(255, 255, 0);
//			g.fillRect(p.x - 10, p.y - 10, 21, 21);
//		}
//		e = longClicked.elements();
//		while (e.hasMoreElements()) {
//			Point p = (Point) e.nextElement();
//			g.setColor(255, 0, 255);
//			g.fillRect(p.x - 10, p.y - 10, 21, 21);
//		}
//		e = dragged.elements();
//		while (e.hasMoreElements()) {
//			Line l = (Line) e.nextElement();
//			g.setColor(0, 255, 255);
//			for (int x = -1; x <= 1; x++) {
//				for (int y = -1; y <= 1; y++) {
//					g.drawLine(l.fromX + x, l.fromY + y, l.toX + x, l.toY + y);
//				}
//			}
//		}
//		if (dragging != null) {
//			g.setColor(0, 0, 0);
//			for (int x = -1; x <= 1; x++) {
//				for (int y = -1; y <= 1; y++) {
//					g.drawLine(dragging.fromX + x, dragging.fromY + y,
//							dragging.toX + x, dragging.toY + y);
//				}
//			}
//		}
	}
	
	private Thread t;
	
	private int lightSquareColor = -1;
	
	private int darkSquareColor = -1;

	public int getLightSquareColor() {
		return lightSquareColor < 0 ? 0xffce9e : lightSquareColor;
	}

	public void setLightSquareColor(int lightSquareColor) {
		this.lightSquareColor = lightSquareColor;
	}

	public int getDarkSquareColor() {
		return darkSquareColor < 0 ? 0xd18b47 : darkSquareColor;
	}

	public void setDarkSquareColor(int darkSquareColor) {
		this.darkSquareColor = darkSquareColor;
	}

	private void paintBoard(Graphics g) {
		Position pos = wrapper.getPosition();
		int s = getSize();
		Image[] set = getPieceSet(s);
		for (int rank = 0; rank < Position.SIZE; rank++) {
			for (int file = 0; file < Position.SIZE; file++) {
				if ((file + rank) % 2 == 0) {
					g.setColor(getLightSquareColor());
				} else {
					g.setColor(getDarkSquareColor());
				}
				GraphicsUtils.fillRect(g, file * s, rank * s, s, s);
				char piece;
				if (getFlip()) {
					piece = pos.getPieceAt(7 - file, 7 - rank);
				} else {
					piece = pos.getPieceAt(file, rank);
				}
				Character pieceObj = new Character(piece);
				int index;
				Integer indexObj = (Integer) map.get(pieceObj);
				if (indexObj != null) {
					if (set != null) {
						index = indexObj.intValue();
						Image img = set[index];
						g.drawImage(img, file * s, rank * s, 0);
					} else {
						g.setFont(Font.getFont(0, 0, Font.SIZE_SMALL));
						g.drawChar(piece, file * s + s / 2, rank * s + (s - g.getFont().getHeight()) / 2, 0);
					}
				}
			}
		}
		if (wrapper.movePretty != null && !"none".equals(wrapper.movePretty)) {
			g.setColor(127, 0, 127);
			int x1, y1, x2, y2;
			if (ArrayUtils.contains(new String[] {"o-o", "o-o-o"}, wrapper.moveVerbose)) {
				x1 = getCoord(4);
				x2 = getCoord("o-o".equals(wrapper.moveVerbose) ? 6 : 2);
				char active = wrapper.getPosition().getActivePlayer();
				y1 = y2 = getCoord(active == 'w' ? 0 : 7);
			} else {
				x1 = getCoord(wrapper.moveVerbose.charAt(2) - 'a');
				y1 = getCoord('8' - wrapper.moveVerbose.charAt(3));
				x2 = getCoord(wrapper.moveVerbose.charAt(5) - 'a');
				y2 = getCoord('8' - wrapper.moveVerbose.charAt(6));
			}
			for (int i = -1; i < 1; i++) {
				for (int j = -1; j < 1; j++) {
					g.drawLine(x1 + i, y1 + j, x2 + i, y2 + j);
				}
			}
		}
		{
			int file = getFlip() ? 7 - x : x;
			int rank = getFlip() ? 7 - y : y;
			g.setColor(0, 0, 0);
			GraphicsUtils.drawRect(g, file * s, rank * s, s, s, 1);
		}
		if (moving) {
			int file = getFlip() ? 7 - xFrom : xFrom;
			int rank = getFlip() ? 7 - yFrom : yFrom;
			if (x != xFrom || y != yFrom) {
				g.setColor(0, 0, 255);
			} else {
				g.setColor(255, 0, 0);
			}
			GraphicsUtils.drawRect(g, file * s, rank * s, s, s, 1);
		}
		g.translate(0, Position.SIZE * s);
	}

	private int getCoord(int id) {
		int s = getSize();
		if (getFlip()) {
			id = 7 - id;
		}
		return id * s + s / 2;
	}
	
	private Font handleFont;
	
	private Font getHandleFont() {
		if (handleFont == null) {
			handleFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, getFontSize());
		}
		return handleFont;
	}

	public void setHandleFont(Font handleFont) {
		this.handleFont = handleFont;
	}
	
	private Font timeFont;

	public Font getTimeFont() {
		if (timeFont == null) {
			timeFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, getFontSize());
		}
		return timeFont;
	}

	public void setTimeFont(Font timeFont) {
		this.timeFont = timeFont;
	}

//	public void setFontFace(int fontFace) {
//		super.setFontFace(fontFace);
//		setHandleFont(null);
//		setTimeFont(null);
//	}
	
	private int fontSize;

	private int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
		setHandleFont(null);
		setTimeFont(null);
	}

	private void paintInfo(Graphics g, String handle, long time, int lag, /*int rating,*/ boolean running) {
		int boardSize = 8 * getSize();
		int height = getInfoHeight();
		g.setColor(0, 0, 0);
		if (running) {
			GraphicsUtils.fillRect(g, 0, 0, boardSize, height);
			g.setColor(255, 255, 255);
			time -= System.currentTimeMillis() - startTime;
		}
		String strTime = DateUtils.formatTime(time);
		g.setFont(getHandleFont());
		int strTimeWidth = getTimeFont().stringWidth(strTime);
		int handleWidth = boardSize - strTimeWidth - 3 * margin;
		GraphicsUtils.drawString(g, handle, margin, 0, Graphics.TOP | Graphics.LEFT, handleWidth);
		//System.out.println("clip " + clipX + " " + clipY + " " + clipW + " " + clipH);
		g.setFont(getTimeFont());
		GraphicsUtils.drawString(g, strTime, boardSize - margin, 0, Graphics.TOP | Graphics.RIGHT);
		if (getShowAdditionalInfo() && lag > 0) {
			//GraphicsUtils.drawString(g, Integer.toString(rating), margin, fontHeight, Graphics.TOP | Graphics.LEFT);
			GraphicsUtils.drawString(g, lag + "ms lag", boardSize - margin, getHandleFont().getHeight(), Graphics.TOP | Graphics.RIGHT);
		}
		g.translate(0, height);
	}
	
	private int getInfoHeight() {
		return getShowAdditionalInfo() ? 2 * getHandleFont().getHeight() : getHandleFont().getHeight();
	}

	protected void fireKeyPressed() {
		selectOrMove();
	}

	private void selectOrMove() {
		repaint();
		if (moving) {
			moving = false;
			if (x != xFrom || y != yFrom) {
				fireMove(xFrom, yFrom, x, y);
			}
		} else {
			moving = true;
			xFrom = x;
			yFrom = y;
		}
	}

	protected void leftKeyPressed() { if (getFlip()) moveCursorRight(); else moveCursorLeft(); }
	protected void leftKeyRepeated() { if (getFlip()) moveCursorRight(); else moveCursorLeft(); }

	public void moveCursorLeft() {
		if (x > 0) {
			x--;
			repaint();
		} else if (wrap) {
			x = Position.SIZE - 1;
			if (nextOnWrap) {
				if (y > 0) {
					y--;
				} else {
					y = Position.SIZE - 1;
				}
			}
			repaint();
		}
	}

	protected void rightKeyPressed() { if (getFlip()) moveCursorLeft(); else moveCursorRight(); }
	protected void rightKeyRepeated() { if (getFlip()) moveCursorLeft(); else moveCursorRight(); }

	public void moveCursorRight() {
		if (x < Position.SIZE - 1) {
			x++;
			repaint();
		} else if (wrap) {
			x = 0;
			if (nextOnWrap) {
				if (y < Position.SIZE - 1) {
					y++;
				} else {
					y = 0;
				}
			}
			repaint();
		}
	}

	protected void upKeyPressed() { if (getFlip()) moveCursorDown(); else moveCursorUp(); }
	protected void upKeyRepeated() { if (getFlip()) moveCursorDown(); else moveCursorUp(); }

	public void moveCursorUp() {
		if (y > 0) {
			y--;
			repaint();
		} else if (wrap) {
			y = Position.SIZE - 1;
			if (nextOnWrap) {
				if (x > 0) {
					x--;
				} else {
					x = Position.SIZE - 1;
				}
			}
			repaint();
		}
	}

	protected void downKeyPressed() { if (getFlip()) moveCursorUp(); else moveCursorDown(); }
	protected void downKeyRepeated() { if (getFlip()) moveCursorUp(); else moveCursorDown(); }

	public void moveCursorDown() {
		if (y < Position.SIZE - 1) {
			y++;
			repaint();
		} else if (wrap) {
			y = 0;
			if (nextOnWrap) {
				if ( x < Position.SIZE - 1) {
					x++;
				} else {
					x = 0;
				}
			}
			repaint();
		}
	}

	public void moveCursorTo(int x, int y) {
		if (x < 0 || x >= Position.SIZE) {
			throw new IllegalArgumentException("x");
		}
		if (y < 0 || y >= Position.SIZE) {
			throw new IllegalArgumentException("y");
		}
		if (this.x != x || this.y != y) {
			this.x = x;
			this.y = y;
			repaint();
		}
	}

	private static final int[] sizes = new int[] {
		//#if ScreenWidth == 128
//# 		11, 12, 13, 14, 15, 16,
		//#elif ScreenWidth == 176
//# 		17, 18, 19, 20, 21, 22,
		//#elif ScreenWidth == 240
//# 		20, 22, 24, 26, 28, 30,
		//#elif ScreenWidth == 480
//#			40, 44, 48, 52, 56, 60,
		//#else
		10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30,
		//#endif
	};
	
	public static int getSizesCount() {
		return sizes.length;
	}
	
	public static int getIndex(int size) {
		for (int index = 0; index < sizes.length; index++) {
			if (sizes[index] == size) {
				return index;
			}
		}
		return -1;
	}
	
	public static int getSize(int index) {
		if (index < 0) {
			return SIZE_UNDEFINED;
		}
		return sizes[index];
	}

	public static final int SIZE_UNDEFINED = -1;

	private Image[] pieces;
	private int currentSize;

	private static final Hashtable map;

	static {
		map = new Hashtable();
		map.put(new Character('K'), new Integer(0));
		map.put(new Character('Q'), new Integer(1));
		map.put(new Character('R'), new Integer(2));
		map.put(new Character('B'), new Integer(3));
		map.put(new Character('N'), new Integer(4));
		map.put(new Character('P'), new Integer(5));
		map.put(new Character('k'), new Integer(6));
		map.put(new Character('q'), new Integer(7));
		map.put(new Character('r'), new Integer(8));
		map.put(new Character('b'), new Integer(9));
		map.put(new Character('n'), new Integer(10));
		map.put(new Character('p'), new Integer(11));
	}

	private int size = SIZE_UNDEFINED;

	public int getSize() {
		if (size == SIZE_UNDEFINED) {
			int max;
			if (getWidth() < getHeight() - 2 * getInfoHeight() - getHandleFont().getHeight()) {
				max = getWidth() / Position.SIZE;
			} else {
				max = (getHeight() - 2 * getInfoHeight() - getHandleFont().getHeight()) / Position.SIZE;
			}
			int s = sizes[0];
			for (int i = 1; i < sizes.length; i++) {
				if (sizes[i] <= max && s < sizes[i]) {
					s = sizes[i];
				}
			}
			return s;
		}
		return size;
	}

	public void setSize(int size) {
		if (!ArrayUtils.contains(sizes, size) && size != SIZE_UNDEFINED) {
			throw new IllegalArgumentException("size");
		}
		this.size = size;
		repaint();
	}

//	public boolean increaseSize() {
//		if (size == sizes[sizes.length - 1]) {
//			return false;
//		}
// 		for (int i = sizes.length - 2; i >= 0; i--) {
//			if (size == sizes[i]) {
//				size = sizes[i + 1];
//				break;
//			}
//		}
//		repaint();
//		return true;
//	}
//
//	public boolean decreaseSize() {
//		if (size == sizes[0]) {
//			return false;
//		}
//		for (int i = 1; i < sizes.length; i++) {
//			if (size == sizes[i]) {
//				size = sizes[i - 1];
//				break;
//			}
//		}
//		repaint();
//		return true;
//	}

	public int[] getSizes() {
		int[] ret = new int[sizes.length];
		System.arraycopy(sizes, 0, ret, 0, sizes.length);
		return ret;
	}

	private Image[] getPieceSet(int size) {
		Integer sizeObj = new Integer(size);
		if (currentSize != size) {
			pieces = null;
			try {
				String[] names = {
					"wk", "wq", "wr", "wb", "wn", "wp",
					"bk", "bq", "br", "bb", "bn", "bp",
				};
				Image[] images = new Image[names.length];
				for (int i = 0; i < images.length; i++) {
					String path = "/img/" + size + "/" + names[i] + ".png";
					images[i] = Image.createImage(path);
				}
				pieces = images;
				currentSize = size;
			} catch (IOException ex) {
				currentSize = 0;
				ex.printStackTrace();
				//throw new RuntimeException(ex.toString());
			}
		}
		return pieces;
	}

	private boolean useDragAndDrop;

	public boolean getUseDragAndDrop() {
		return useDragAndDrop;
	}

	public void setUseDragAndDrop(boolean useDragAndDrop) {
		this.useDragAndDrop = useDragAndDrop;
	}
	
	private boolean showAdditionalInfo;
	
	public boolean getShowAdditionalInfo() {
		return showAdditionalInfo;
	}
	
	public void setShowAdditionalInfo(boolean showAdditionalInfo) {
		this.showAdditionalInfo = showAdditionalInfo;
	}

	public interface MoveListener {

		void move(int xFrom, int yFrom, int xTo, int yTo);
	}

	private MoveListener moveListener;

	public void setMoveListener(MoveListener l) {
		moveListener = l;
	}

	private void fireMove(int xFrom, int yFrom, int xTo, int yTo) {
		if (moveListener != null) {
			moveListener.move(xFrom, yFrom, xTo, yTo);
		}
	}

	boolean startedDragging;

	protected boolean pressed(int x, int y) {
		if (y < getInfoHeight()) {
			return true;
		}
		int file = x / getSize();
		int rank = (y - getInfoHeight()) / getSize();
		if (file < 0 || file >= Position.SIZE
				|| rank < 0 || rank >= Position.SIZE) {
			return true;
		}
		if (getFlip()) {
			file = 7 - file;
			rank = 7 - rank;
		}
		moveCursorTo(file, rank);
		startedDragging = true;
		return false;
	}

	protected void clicked(int x, int y) {
		if (!getUseDragAndDrop()) {
			selectOrMove();
		}
	}

	protected void dragging(int fromX, int fromY, int toX, int toY) {
		if (!getUseDragAndDrop()) {
			return;
		}
		if (startedDragging) {
			startedDragging = false;
			moving = false;
			selectOrMove();
		}
		if (toY < getInfoHeight()) {
			return;
		}
		int file = toX / getSize();
		int rank = (toY - getInfoHeight()) / getSize();
		if (file < 0 || file >= Position.SIZE
				|| rank < 0 || rank >= Position.SIZE) {
			return;
		}
		if (getFlip()) {
			file = 7 - file;
			rank = 7 - rank;
		}
		moveCursorTo(file, rank);
	}

	protected void dropped(int fromX, int fromY, int toX, int toY) {
		if (!getUseDragAndDrop()) {
			return;
		}
		if (!moving) {
			return;
		}
		if (toY < getInfoHeight()) {
			moving = false;
			return;
		}
		int file = toX / getSize();
		int rank = (toY - getInfoHeight()) / getSize();
		if (file < 0 || file >= Position.SIZE
				|| rank < 0 || rank >= Position.SIZE) {
			moving = false;
			return;
		}
		if (getFlip()) {
			file = 7 - file;
			rank = 7 - rank;
		}
		moveCursorTo(file, rank);
		selectOrMove();
	}

//	private class Point {
//		public Point(int x, int y) {
//			this.x = x;
//			this.y = y;
//		}
//		int x;
//		int y;
//	}
//
//	private class Line {
//		public Line(int fromX, int fromY, int toX, int toY) {
//			this.fromX = fromX;
//			this.fromY = fromY;
//			this.toX = toX;
//			this.toY = toY;
//		}
//		int fromX;
//		int fromY;
//		int toX;
//		int toY;
//	}
//
//	private Vector clicked = new Vector();
//
//	protected void clicked(int x, int y) {
//		clicked.addElement(new Point(x, y));
//		repaint();
//	}
//
//	private Vector longClicked = new Vector();
//
//	protected void longClicked(int x, int y) {
//		longClicked.addElement(new Point(x, y));
//		repaint();
//	}
//
//	protected void pressing(long milliseconds) {
//		System.out.println("ms: " + milliseconds);
//	}
//
//	private Line dragging;
//
//	private int q;
//
//	protected void dragging(int fromX, int fromY, int toX, int toY) {
//		System.out.println(fromX + " " + fromY + " " + toX + " " + toY);
//		dragging = new Line(fromX, fromY, toX, toY);
//		q++;
//		if (q == 5) {
//			q = 0;
//			repaint();
//		}
//	}
//
//	private Vector dragged = new Vector();
//
//	protected void dragged(int fromX, int fromY, int toX, int toY) {
//		dragging = null;
//		dragged.addElement(new Line(fromX, fromY, toX, toY));
//		repaint();
//	}
}
