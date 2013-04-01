
package pl.mg6maciej;

import java.util.Vector;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;

/**
 * @author mg6maciej
 */
public abstract class ListCanvas extends AbstractCanvas {

	private Command selectCommand;
	private CommandListener listener;
	
	private Vector objects = new Vector();
	private int selectedIndex = -1;
	
	private int separatorColor = -1;
	
	public ListCanvas(Display display) {
		super(display);
		super.setCommandListener(this);
	}

	protected void paint(Graphics g) {
		super.paint(g);
		paintBody(g);
		paintTitle(g);
	}

	protected void paintBackground(Graphics g) {
		g.setColor(0xffffff);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	protected void paintBody(Graphics g) {
		int titleHeight = getTitleHeight();
		int y = -topY + titleHeight;
		for (int i = 0; i < objects.size(); i++) {
			Object object = objects.elementAt(i);
			int height = getElementHeight(object);
			if (y + height <= titleHeight) {
				y += height + 1;
				continue;
			}
			boolean selected = i == selectedIndex;
			if (selected) {
				g.setColor(0x99ccff);
				GraphicsUtils.fillRect(g, 0, y, getWidth(), height);
			}
			paintElement(g, y, object, selected);
			y += height;
			g.setColor(getSeparatorColor());
			g.drawLine(0, y, getWidth(), y);
			y++;
			if (y >= getHeight()) {
				break;
			}
		}
	}

	protected int getTitleHeight() {
		return 0;
	}
	
	protected void paintTitle(Graphics g) {
	}

	protected abstract void paintElement(Graphics g, int y, Object object, boolean selected);
	
	protected abstract int getElementHeight(Object object);

	public int getSeparatorColor() {
		return separatorColor < 0 ? 0xcccccc : separatorColor;
	}

	public void setSeparatorColor(int separatorColor) {
		this.separatorColor = separatorColor;
	}
	
	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		this.selectedIndex = selectedIndex;
		updateTopY();
		repaint();
	}
	
	public void append(Object object) {
		objects.addElement(object);
		if (getSelectedIndex() == -1) {
			setSelectedIndex(0);
		}
		repaint();
	}
	
	public void insert(Object object, int index) {
		objects.insertElementAt(object, index);
		if (getSelectedIndex() == -1) {
			setSelectedIndex(0);
		} else if (getSelectedIndex() >= index) {
			setSelectedIndex(getSelectedIndex() + 1);
		}
		repaint();
	}
	
	public void remove(int index) {
		objects.removeElementAt(index);
		if (index < getSelectedIndex()) {
			setSelectedIndex(getSelectedIndex() - 1);
		}
		if (getSelectedIndex() == objects.size()) {
			setSelectedIndex(getSelectedIndex() - 1);
		}
		repaint();
	}
	
	public void removeAll() {
		//System.out.println("remove all");
		objects.removeAllElements();
		setSelectedIndex(-1);
	}
	
	public Object get(int index) {
		return objects.elementAt(index);
	}
	
	public void set(Object object, int index) {
		objects.setElementAt(object, index);
		repaint();
	}
	
	public int size() {
		return objects.size();
	}

	public Command getSelectCommand() {
		return selectCommand;
	}

	public void setSelectCommand(Command selectCommand) {
		this.selectCommand = selectCommand;
	}

//	public void setCommandListener(CommandListener l) {
//		this.listener = l;
//	}
//
//	public void commandAction(Command cmd, Displayable d) {
//		if (listener != null) {
//			listener.commandAction(cmd, d);
//		}
//	}

	protected void fireKeyReleased() {
		callSelectCommandAction();
	}

	protected void clicked(int x, int y) {
		y -= getTitleHeight();
		int clickedIndex = -1;
		int tmpY = y + topY;
		for (int i = 0; i < size(); i++) {
			int height = getElementHeight(get(i)) + 1;
			if (tmpY - height < 0) {
				clickedIndex = i;
				break;
			}
			tmpY -= height;
		}
		if (clickedIndex != -1) {
			setSelectedIndex(clickedIndex);
			callSelectCommandAction();
		}
	}
	
	private void callSelectCommandAction() {
		if (getSelectedIndex() != -1) {
			if (getSelectCommand() != null) {
				if (listener != null) {
					listener.commandAction(getSelectCommand(), this);
				}
			}
		}
	}

	protected void downKeyPressed() {
		selectNextObject();
	}

	protected void downKeyRepeated() {
		selectNextObject();
	}

	protected void upKeyPressed() {
		selectPreviousObject();
	}

	protected void upKeyRepeated() {
		selectPreviousObject();
	}

	protected void leftKeyPressed() {
	}

	protected void leftKeyRepeated() {
	}

	protected void rightKeyPressed() {
	}

	protected void rightKeyRepeated() {
	}
	
	protected void selectNextObject() {
		if (getSelectedIndex() != -1 && getSelectedIndex() < objects.size() - 1) {
			setSelectedIndex(getSelectedIndex() + 1);
		}
	}
	
	protected void selectPreviousObject() {
		if (getSelectedIndex() > 0) {
			setSelectedIndex(getSelectedIndex() - 1);
		}
	}
	
	private int topY;
	
	private void updateTopY() {
		if (getSelectedIndex() == -1) {
			topY = 0;
		} else {
			int maxTopY = 0;
			for (int i = 0; i < getSelectedIndex(); i++) {
				maxTopY += getElementHeight(get(i)) + 1;
			}
			int minTopY = maxTopY + getElementHeight(get(getSelectedIndex())) - (getHeight() - getTitleHeight());
			if (minTopY < 0) {
				minTopY = 0;
			}
			if (topY < minTopY) {
				topY = minTopY;
			}
			if (topY > maxTopY) {
				topY = maxTopY;
			}
		}
	}
	
	private int prevY;

	protected boolean pressed(int x, int y) {
		if (y < getTitleHeight()) {
			return true;
		}
		y -= getTitleHeight();
		prevY = y;
		int pressedIndex = -1;
		int tmpY = y + topY;
		for (int i = 0; i < size(); i++) {
			int height = getElementHeight(get(i)) + 1;
			if (tmpY - height < 0) {
				pressedIndex = i;
				break;
			}
			tmpY -= height;
		}
		if (pressedIndex != -1 && getSelectedIndex() != pressedIndex) {
			setSelectedIndex(pressedIndex);
		}
		return super.pressed(x, y);
	}

	protected void dragging(int fromX, int fromY, int toX, int toY) {
		scroll(toY);
	}

	protected void dropped(int fromX, int fromY, int toX, int toY) {
		scroll(toY);
	}
	
	private void scroll(int y) {
		y -= getTitleHeight();
		if (prevY != y) {
			int prevTopY = topY;
			topY += prevY - y;
			prevY = y;
			updateTopY();
			if (prevTopY != topY) {
				repaint();
			}
		}
	}
}
