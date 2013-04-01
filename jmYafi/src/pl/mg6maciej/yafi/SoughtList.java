
package pl.mg6maciej.yafi;

import java.util.Date;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import pl.mg6maciej.GraphicsUtils;
import pl.mg6maciej.ListCanvas;

/**
 * @author mg6maciej
 */
public class SoughtList extends ListCanvas {
	
	public SoughtList(Display display) {
		super(display);
	}
	
	private Font font = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	private Font titleFont = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);

	protected void paintElement(Graphics g, int y, Object object, boolean selected) {
		if (object instanceof SeekInfo) {
			SeekInfo seekInfo = (SeekInfo) object;
			g.setColor(0x000000);
			g.setFont(font);
			String str = seekInfo.getHandle()
					+ (seekInfo.getRating() > 0 ? "(" + seekInfo.getRating() + ")" : "")
					+ " " + seekInfo.getTime()
					+ " " + seekInfo.getIncrement()
					+ " " + (seekInfo.getRated() ? "r" : "u")
					+ " " + seekInfo.getType();
			GraphicsUtils.drawString(g, str, 5, y + 3, 0);
		}
	}

	protected int getElementHeight(Object object) {
		int height = font.getHeight();
		return height + 6;
	}

	protected void paintTitle(Graphics g) {
		int height = getTitleHeight();
		g.setColor(0xeeeeee);
		g.fillRect(0, 0, getWidth(), height);
		g.setColor(0x000000);
		g.setFont(titleFont);
		String str = "handle(rating) time inc rated type";
		GraphicsUtils.drawString(g, str, 5, 3, 0);
	}

	protected int getTitleHeight() {
		int height = titleFont.getHeight();
		return height + 6;
	}

	public void addSeek(SeekInfo seekInfo) {
		long currentTime = System.currentTimeMillis();
		for (int i = 0; i < size(); i++) {
			if (super.get(i) instanceof Date) {
				long removeTime = ((Date) super.get(i)).getTime();
				if (currentTime - removeTime >= 1024) {
					set(seekInfo, i);
					return;
				}
			}
		}
		append(seekInfo);
	}

	public void removeSeeks(int[] seekNumbers) {
		for (int i = size() - 1; i >= 0; i--) {
			if (super.get(i) instanceof SeekInfo) {
				SeekInfo seekInfo = (SeekInfo) super.get(i);
				for (int j = 0; j < seekNumbers.length; j++) {
					if (seekInfo.getSeekNumber() == seekNumbers[j]) {
						set(new Date(), i);
						break;
					}
				}
			}
		}
	}
	
	public Object get(int index) {
		if (super.get(index) instanceof SeekInfo) {
			return super.get(index);
		}
		return null;
	}

	public void clean() {
		for (int i = size() - 1; i >= 0; i--) {
			if (get(i) == null) {
				remove(i);
			}
		}
	}
}
